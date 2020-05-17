/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
import java.lang.Math.*;

/**
 * Grid
 * 
 * A grid of the level that stores level information for input into neural networks.
 *
 */
public class Grid {
	
	//Properties shared across all grids
	private final float sG=7;// level-coordinate space length of each grid cell
	private final float aG=(float)0.25;//cell activation, the amount of the cell that needs to be covered for it to activate the cell
	
	
	private float offsetX;//the offset of the grid from integers multiples of sG
	private float offsetY;//these exist for locating relevant GridCells
	
	Level level;
	//grid info
	//maps [x,y] to cell
	//x and y in level-coordinates
	private HashMap<String,GridCell> cells=new HashMap<String,GridCell>();//note default size of 16
	private HashMap<GameObject,List<String>> attributing=new HashMap<GameObject,List<String>>();//stores what cell keys gameobjects are attributing
	
	public Grid(Level level,float cX,float cY) {
		this.offsetX=cX-(sG*(int)(cX/this.sG));
		this.offsetY=cY-(sG*(int)(cY/this.sG));
		this.level=level;
	}
	
	//generates a grid by iterating over GameObjects and creating relevant cells.
	//only used for the first creation of grid.
	public void generateGrid() {
		//tracer parameters are used to move around a selected object
		//this enables the entire object to be converted into cells;
		for(int i=0;i<this.level.objects.size();i++) {//iterate over all objects
			GameObject selected=this.level.objects.get(i);
			//check if the object is invisible/irrelevant
			if(selected.objectClass!="walkable"&&selected.objectClass!="deadly"&&selected.objectClass!="enemy") {
				continue;
			}
			this.traceObject(selected);
			
		}
	}
	
	//updates the grid for a given frame
	public void updateGrid() {
		
		//remove cells only attributed by objects that moved
		//done by iterating over objects that moved
		for(int i=0;i<this.level.objects.size();i++) {//iterate over all objects
			GameObject selected=this.level.objects.get(i);
			if(selected.moveable) {
				//find cells attributing
				List<String> toDo=attributing.get(selected);
				if(toDo!=null) {
					for(int j=0;j<toDo.size();j++) {
						this.cells.get(toDo.get(j)).getAttributers().remove(selected);
						//check for no attributer
						if(this.cells.get(toDo.get(j)).getAttributers().size()==0) {
							//delete toDo in x-y map
							this.cells.remove(toDo.get(j));
						}
						//note: the list in the attributing map will get overwritten later
					}
				}
				//retrace objet
				this.traceObject(selected);
			}
		}
		
		//retrace all moved objects
		for(int i=0;i<this.level.objects.size();i++) {//iterate over all objects
			GameObject selected=this.level.objects.get(i);
			if(selected.moveable) {
				this.traceObject(selected);
			}
		}
	}
	
	//traces an object encoding it into cells.
	private void traceObject(GameObject selected) {
		
		//find closest gridCell
		/**
		 * Steps to find closest grid cell:
		 * Find closest grid cell for a sGxsG grid with no offset.
		 * This is done by finding the number of complete cells and multiplying by cell length.
		 * Then add the offset.
		 * After the offset is added check that the cell is smaller than the selected coordinate.
		 * If not move one cell back.
		 * 
		 * This process has to be reliable and consistent because of the hash table.
		 */
		float startX=((int)(selected.x/this.sG))*this.sG+this.offsetX;
		float startY=((int)(selected.y/this.sG))*this.sG+this.offsetY;
		if(startX>selected.x) {startX-=sG;}
		if(startY>selected.y) {startY-=sG;}
		
		List<String> cellList=new ArrayList<String>();//the list of cell keys that selected is attributing
		
		//tracer starts in the bottom left and moves to the top right
		for(float tracerX=startX;tracerX<selected.x+selected.width+this.sG;tracerX+=this.sG) {
			for(float tracerY=startY;tracerY<selected.y+selected.height+this.sG;tracerY+=this.sG) {
				//evaluate cell
				float leftEdge=0;
				float rightEdge=this.sG;
				float topEdge=this.sG;
				float bottomEdge=0;
				
				//check if cell is at any edges
				if(tracerX==startX) {
					leftEdge=selected.x%this.sG;
				}
				if(tracerX>selected.x+selected.width) {
					rightEdge=(selected.x+selected.width)%this.sG;
				}
				if(tracerY==startY) {
					bottomEdge=selected.y%this.sG;
				}
				if(tracerY>selected.y+selected.height) {
					topEdge=(selected.y+selected.height)%this.sG;;
				}
				//object is entirely in cell
				//check for activation by calculating area in cell
				if((rightEdge-leftEdge)*(topEdge-bottomEdge)<this.sG*this.sG*this.aG) {
					continue;
				}
				//update cell
				GridCell cell=this.getCell(tracerX, tracerY);
				//add attributer
				cell.getAttributers().add(selected);
				cellList.add(this.getKey(tracerX, tracerY));
				
				if(selected.objectClass=="walkable") {
					cell.setWalkable(1);
				}
				else if(selected.objectClass=="deadly") {
					cell.setDeadly(1);
				}
				else if(selected.objectClass=="enemy"){
					cell.setEnemy(1);
				}
			}
		}
		//map GameObject to List<GridCell>
		if(selected.moveable) {
			this.attributing.put(selected,cellList);
		}
	}
	//produces the list of [walkable,deadly,enemy] for the given cell
	//also deals with empty cells
	public int[] getCellExport(float x,float y) {
		GridCell selected=this.cells.get(this.getKey(x, y));
		if(selected!=null) {
			return this.cells.get(this.getKey(x, y)).export();
		}
		else {
			return new int[] {0,0,0};
		}
	}
	
	//GridCell
	//gets pointer to GridCell for the given co-ordinates
	//adds GridCell there if none exists
	private GridCell getCell(float x,float y) {
		GridCell selected=this.cells.get(this.getKey(x, y));
		if(selected==null) {
			selected=new GridCell();
			this.cells.put(this.getKey(x, y),selected);
		}
		return selected;
	}
	
	//returns the hashmap key for two floats
	public String getKey(float x,float y) {
		//rounding to avoid base-2 float math errors
		float newX=Math.round(x*1000F)/1000F;
		float newY=Math.round(y*1000F)/1000F;
		return(Float.toString(newX)+"-"+Float.toString(newY));
	}
	
	//method for testing purposes only
	public void debug() {
		Set<String> k=this.cells.keySet();
		for(String i: k){
			System.out.println(i);
			
		}
		System.out.println(this.cells.size());
	}
}
