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
	
	Level level;
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
	
	public GameArea(Level level) {
		this.level=level;
		//this.setBackground(Color.black);
		repaint();
		
	}
	
	public void paint(Graphics g) {
		//establish bounds of vision in level space
		List<Float> viewBounds=getViewCenter();
		float xLOW=viewBounds.get(0);
		float yLOW=viewBounds.get(1);
		float xUP=xLOW+this.viewX;
		float yUP=xLOW+this.viewY;
		viewBounds=null;//needless space
		
		float convRatioX=this.getWidth()/this.viewX;
		float convRatioY=this.getHeight()/this.viewY;
		
		//background stuff
		if(this.backroundTex) {
			for(int x=(int)(xLOW-xLOW%this.backgroundLen);x<xUP+(this.backgroundLen-xUP%this.backgroundLen);x+=this.backgroundLen) {
				for(int y=(int)(yLOW-yLOW%this.backgroundLen);y<yUP+(this.backgroundLen-yUP%this.backgroundLen);y+=this.backgroundLen) {
					g.drawImage(this.background,(int)((x-xLOW)*convRatioX),(int)(this.getHeight()-(y-yLOW)*convRatioY),(int) (this.backgroundLen*convRatioX),-(int) (this.backgroundLen*convRatioY),null);
				}
			}
		}
		
		//for each object
		for(int i=0;i<this.level.objects.size();i++) {
			
			GameObject sel=this.level.objects.get(i);//selected object
			
			//check if it is in bounds
			if((sel.x+sel.width>xLOW&&sel.y+sel.height>yLOW)||(sel.x<xUP&&sel.y<yUP)) {
				//draw sel
				if(sel.color!=null) {
					g.setColor(sel.color);
				}
				//if the color is null it will be drawn with the last previously drawn color
				//although this is not likely to occur.
				g.fillRect((int)((sel.x-xLOW)*convRatioX),(int) (this.getHeight()-(sel.y-yLOW)*convRatioY),(int)(sel.width*convRatioX),-(int)(sel.height*convRatioY));
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
