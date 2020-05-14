/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
/**
 * GridCell
 * A class that holds information about each grid cell. Also holds location info and methods for evaluating the grid cell.
 */
public class GridCell {
	
	//these are 1 for true and 0 for false
	private int enemy;//has enemy activated cell
	private int walkable;//has walkable activated cell
	private int deadly;//has a deadly object activated cell
	private List<GameObject> attributers;//list of GameObjects that are activate the cell
	public GridCell() {
		this.attributers=new ArrayList<GameObject>();
	}
	//export int[3] encoded cell info ready for neural nets
	public int[] export(){
		return new int[]{this.walkable,this.deadly,this.enemy};
	}
	//set values for private variables explicitly
	public void setEnemy(int newVal) {
		this.enemy=newVal;
	}
	public void setWalkable(int newVal) {
		this.walkable=newVal;
	}
	public void setDeadly(int newVal) {
		this.walkable=newVal;
	}
	public List<GameObject> getAttributers(){
		return this.attributers;
	}
	
}
