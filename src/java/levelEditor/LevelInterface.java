package levelEditor;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.Color;
import java.awt.event.*;
import java.util.List;

import gameDynamics.Deadly;
import gameDynamics.Enemy;
import gameDynamics.GameObject;
import gameDynamics.Level;
import userInterface.GameArea;

/**
 * LevelInterface
 * 
 * Manages user interactions with the level in the level ediotr.
 *
 */
public class LevelInterface extends MouseAdapter{
	
	Selector sel;
	public GameObject selected=null;//used for the inspctor
	GameObject holding=null;//used for GameObject drag and drop
	float holdingOffsetX;//for dragging
	float holdingOffsetY;
	GameArea area;
	Inspector insp;
	
	public LevelInterface(Selector sel,GameArea area,Inspector insp) {
		this.sel=sel;
		this.area=area;
		this.insp=insp;
	}
	public void mouseClicked(MouseEvent e) {
		
		if(e.getX()>600||e.getY()>600) {
			//out of bounds
			return;
		}
		float[] o=this.levelSpaceMouse(e);
		float levelX=o[0];
		float levelY=o[1];
		if(e.getButton()==e.BUTTON3) {
			//add new object
			GameObject newObj;
			if(sel.sel==Selector.AddSelected.WALKABLE) {
				newObj=new GameObject((float)levelX,(float)levelY,10F,10F,false,false,this.area.getLevel());
				newObj.color=Color.GRAY;
			}
			else if (sel.sel==Selector.AddSelected.DEADLY){
				newObj=new Deadly((float)levelX,(float)levelY,10F,10F,false,false,this.area.getLevel(),1);
				newObj.color=Color.RED;
			}
			else {//as sel is enum sel =Selector.AddSelected.ENEMY
				newObj=new Enemy((float)levelX,(float)levelY,10F,10F,this.area.getLevel(),100);
				newObj.color=Color.BLUE;
			}
			newObj.collidable=false;
			this.area.getLevel().addObject(newObj);
		}
		
	}
	public void mousePressed(MouseEvent e) {
		
		if(e.getX()>600||e.getY()>600) {
			//out of bounds
		}
		else {
			
			float[] o=this.levelSpaceMouse(e);
			float levelX=o[0];
			float levelY=o[1];
			if(e.getButton()==e.BUTTON1) {
				//find gameobject
				for(GameObject sel:this.area.getLevel().objects) {
					if(levelX>sel.x&&levelX<sel.x+sel.width) {
						if(levelY>sel.y&&levelY<sel.y+sel.height){
							this.holding=sel;
							if(this.selected!=null) {
								this.selected.highlight=false;
							}
							this.selected=sel;
							this.selected.highlight=true;
							this.insp.setSelected(this.selected);
							this.holdingOffsetX=levelX-sel.x;
							this.holdingOffsetY=levelY-sel.y;
						}
					}
				}
			}
		}	
	}
	
	public void mouseDragged(MouseEvent e) {
		if(e.getX()>600||e.getY()>600) {
			//out of bounds
		}
		else {
			if(this.holding!=null) {
				
				float[] o=this.levelSpaceMouse(e);
				float levelX=o[0];
				float levelY=o[1];
				float oldX=this.holding.x;
				float oldY=this.holding.y;
				this.holding.x=levelX-this.holdingOffsetX;
				this.holding.y=levelY-this.holdingOffsetY;
				//block movement at zero
				if (this.holding.x<0) {
					this.holding.x=0;
				}
				if(this.holding.y<0) {
					this.holding.y=0;
				}
				
				//collision checks
				if(this.holding.collidable) {
					boolean collided=false;
					for(GameObject sel:this.area.getLevel().objects) {
						if(sel!=this.holding) {
							if(this.holding.colliding(sel)){
								collided=true;
								break;
							}
						}
					}
					if(collided) {
						this.holding.x=oldX;
						this.holding.y=oldY;
					}
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getX()>600||e.getY()>600) {
			//out of bounds
		}
		else {
			
			float[] o=this.levelSpaceMouse(e);
			float levelX=o[0];
			float levelY=o[1];
			if(e.getButton()==e.BUTTON1) {
				//find remove holding
				this.holding=null;
			}
		}
	}
	//returns the x and y of the mouse in level space
	private float[] levelSpaceMouse(MouseEvent e) {
		//check if on levelView
		int mouseX=e.getX()-8;
		int mouseY=e.getY()-30;
		
		//get level space coordinates
		float levelX;//level space X
		float levelY;//level space Y
		List<Float> viewBounds=this.area.getViewCenter();
		float xLOW=viewBounds.get(0);
		float yLOW=viewBounds.get(1);
		
		float convRatioX=this.area.getWidth()/this.area.getViewX();
		float convRatioY=this.area.getHeight()/this.area.getViewY();
		
		levelX=(mouseX/convRatioX)+xLOW;
		levelY=((this.area.getHeight()-mouseY)/convRatioY)+yLOW;
		float[] out= {levelX,levelY};
		return out;
	}

}
