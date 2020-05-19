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
	GameObject holding=null;//used for GameObject drag and drop
	GameArea area;
	
	public LevelInterface(Selector sel,GameArea area) {
		this.sel=sel;
		this.area=area;
	}
	public void mouseClicked(MouseEvent e) {
		
		//check if on levelView
		int mouseX=e.getX()-8;
		int mouseY=e.getY()+29;
		
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
		
		if(mouseX>600&&mouseY>600) {
			//out of bounds
			return;
		}
		
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
			this.area.getLevel().addObject(newObj);
		}
	}

}
