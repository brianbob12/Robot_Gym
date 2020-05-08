/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

/**
 * GameArea
 * 
 * This class is responsible for importing a level and displaying it.
 *
 */

public class GameArea extends JPanel {
	
	Level level;
	int viewX=100;//how far the gameArea is allowed to show in the Level-x direction
	int viewY=100;//how far the gameArea is allowed to show in the Level-y direction
	
	//the viewCenterX/Y store the *STATIC* view center
	int viewCenterX=0;
	int viewCenterY=0;
	
	int viewCenterOffsetX=0;
	int viewCenterOffsetY=0;
	boolean dynamicViewCenter=false;//Weather the view center is fixed to an object
	GameObject fixedViewCenter;//be careful that this is in the correct level
	
	public GameArea(Level level) {
		this.level=level;
		this.setBackground(Color.black);
		
		//establish bounds of vision in level space
		List<Integer> viewBounds=getViewCenter();
		int xLOW=viewBounds.get(0);
		int yLOW=viewBounds.get(1);
		int xUP=xLOW+viewX;
		int yUP=xLOW+viewY;
		viewBounds=null;//needless space
		
		//for each object
		for(int i=0;i<this.level.objects.size();i++) {
			
			GameObject sel=this.level.objects.get(i);//selected object
			
			//check if it is in bounds
			if(sel.x>xLOW&&sel.x<xUP&&sel.y>yLOW&&sel.y<yUP) {
				//draw sel
			}
		}
	}
	
	//Returns the view center of view in level space
	private List<Integer> getViewCenter(){
		if(this.dynamicViewCenter) {
			if(this.level.objects.contains(this.fixedViewCenter)) {
				return Arrays.asList((int) this.fixedViewCenter.x+this.viewCenterOffsetX,(int)this.fixedViewCenter.y+this.viewCenterOffsetY);
			}
			else{
				System.out.println("attempting to center object not in level");
				return Arrays.asList(this.viewCenterX,this.viewCenterY);
			}
		}
		else {
			return Arrays.asList(this.viewCenterX,this.viewCenterY);
		}
	}
}
