package userInterface;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import gameDynamics.GameObject;
import gameDynamics.Level;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * GameArea
 * 
 * This class is responsible for importing a level and displaying it.
 *
 */

public class GameArea extends JPanel {
	
	private Level level;
	int viewX=100;//how far the gameArea is allowed to show in the Level-x direction
	int viewY=100;//how far the gameArea is allowed to show in the Level-y direction
	
	//the viewCenterX/Y store the *STATIC* view center
	public float viewCenterX=0;
	public float viewCenterY=0;
	
	public float viewCenterOffsetX=0;
	public float viewCenterOffsetY=0;
	public boolean dynamicViewCenter=false;//Weather the view center is fixed to an object
	public GameObject fixedViewCenter;//be careful that this is in the correct level
	
	//background textures
	boolean backroundTex=false;//if there is an active background texture
	private Image background;
	private int backgroundLen;//length of the background texture in level space
	private float backgroundLag=1f;//the proportion of movement in the backgound compared to movement in the camera
	
	
	public GameArea(Level level) {
		this.level=level;
		//this.setBackground(Color.black);
		repaint();
		
	}
	
	//for the level editor
	public void setLevel(Level level) {
		this.level=level;
	}
	
	public void setBackgroundLag(float f) {
		this.backgroundLag=f;
	}
	
	public void paint(Graphics g) {
		//establish bounds of vision in level space
		List<Float> viewBounds=getViewCenter();
		float xLOW=viewBounds.get(0);
		float yLOW=viewBounds.get(1);
		float xUP=xLOW+this.viewX;
		float yUP=yLOW+this.viewY;
		viewBounds=null;//needless space
		
		float convRatioX=this.getWidth()/this.viewX;
		float convRatioY=this.getHeight()/this.viewY;
		
		//background stuff
		if(this.backroundTex) {
			for(int x=(int)(xLOW*backgroundLag-(xLOW*backgroundLag)%this.backgroundLen);x<xUP+(this.backgroundLen-(xUP%this.backgroundLen));x+=this.backgroundLen) {
				for(int y=(int)(yLOW*backgroundLag-(yLOW*backgroundLag)%this.backgroundLen);y<yUP+(this.backgroundLen-yUP%this.backgroundLen);y+=this.backgroundLen) {
					g.drawImage(this.background,(int)((x-xLOW*backgroundLag)*convRatioX),(int)(this.getHeight()-(y-yLOW*backgroundLag)*convRatioY),(int) (this.backgroundLen*convRatioX),-(int) (this.backgroundLen*convRatioY),null);
				}
			}
		}
		
		//for each object
		for(int i=0;i<this.level.getObjects().size();i++) {
			
			GameObject sel=this.level.getObjects().get(i);//selected object
			
			//check if it is in bounds
			if((sel.x+sel.width>xLOW&&sel.y+sel.height>yLOW)||(sel.x<xUP&&sel.y<yUP)) {
				//draw sel
				
				sel.draw(g,this,xLOW,yLOW,xUP,yUP,convRatioX,convRatioY);
			}
		}
	}
	
	//Returns the view center of view in level space
	public List<Float> getViewCenter(){
		if(this.dynamicViewCenter) {
			if(this.fixedViewCenter.level==this.level) {
				return Arrays.asList(this.fixedViewCenter.x+this.viewCenterOffsetX,this.fixedViewCenter.y+this.viewCenterOffsetY);
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
	
	//set a background image
	public void setBackgroundImage(String path,int length) {
		this.backroundTex=true;
		try {
			this.background=ImageIO.read(new File(path));
			this.backgroundLen=length;
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	public int getViewX() {
		return this.viewX;
	}
	public int getViewY() {
		return this.viewY;
	}
	public Level getLevel() {
		return this.level;
	}

}
