/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * GameObject
 * 
 * This class holds a rectangular game object(like a box or player). It stores position level space and velocity.
 *
 */

public class GameObject {
	
	//dynamics
	public float x;//x level-space position(this differs from position on screen)
	public float y;//y level-space position
	public float width;
	public float height;
	public float vx;
	public float vy;
	public boolean gravity;//is it affected by gravity
	public boolean collidable=true;
	public boolean moveable;//Weather this object is able to be moved
	public Color color=null;//null if object is immovable
	public Level level;
	
	
	public GameObject(float x,float y,float width,float height,boolean gravity,boolean moveable,Level level) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.vx=0;
		this.vy=0;
		this.gravity=gravity;
		this.moveable=moveable;
		this.level=level;
	}
	
	//update position
	public void move() {
		
		if (this.gravity) {
			this.vy-=level.gravity;
		}
		this.x+=this.vx;
		this.y+=this.vy;
	}
	
	//reverses position by one frame used for resolving collisions
	//only reveres x if x needs to be  backsteped,same with y
	public void backstep(float fraction,boolean moveX,boolean moveY) {
		if(moveY) {
			float newvy=-this.vy/fraction;
			if (this.gravity) {
				newvy+=level.gravity;
			}
			this.y+=newvy;
		}
		if(moveX) {
			float newvx=-this.vx/fraction;
			this.x+=newvx;
		}
	}
	
	//check for collision and resolve collision
	//Returns whether the objects are in collision
	public void collide(GameObject hit) {
		if(this.collidable&&hit.collidable) {
			if(pointInBox(this.x,this.y,hit)||pointInBox(this.x+this.width,this.y,hit)||pointInBox(this.x,this.y+this.height,hit)||pointInBox(this.x+this.width,this.y+this.height,hit)) {
				//collision detected
				
				//determine responsibility for collision 
				if(!this.movingTowards(hit)) {
					hit.collide(this);//hit needs to backstep
				}
				
				//resolve collision
				float colFract;
				colFract=(float) 10;
				while(pointInBox(this.x,this.y,hit)||pointInBox(this.x+this.width,this.y,hit)||pointInBox(this.x,this.y+this.height,hit)||pointInBox(this.x+this.width,this.y+this.height,hit)) {
					this.backstep(colFract,this.movingX(hit),this.movingY(hit));
				}
				
				//find collided side
				float right=Math.abs((this.x+this.width)-hit.x);
				float left=Math.abs(this.x-(hit.x+hit.width));
				float up=Math.abs(this.y+this.height-hit.y);
				float down=Math.abs(this.y-(hit.y+hit.height));
				float side=Math.min(Math.min(right, left),Math.min(up,down));
				//remove velocity on the collided side
				if(right==side||left==side) {
					this.vx=0;
					if(left==side){
						this.x=hit.x+hit.width;
					}
					else {
						this.x=hit.x-this.width;
					}
				}
				else if(up==side||down==side){
					this.vy=0;
					if(down==side){
						this.y=hit.y+hit.height;
					}
					else {
						this.y=hit.y-this.height;
					}
				}
			}
		}
	}
	
	public boolean pointInBox(float x,float y,GameObject hit) {//tests a a corner is overlapping a GameObject
		return (x<hit.x+hit.width&&x>hit.x&&y<hit.y+hit.height&&y>hit.y);
	}
	public boolean movingX(GameObject hit) {//tests if this is moving towards hit in the x direction
		if(this.vx>0) {
			if(hit.x<this.x+this.width-this.vx) {
				return false;
			}
		}
		else if(this.vx<0) {
			if(hit.x+hit.width>this.x-this.vx) {
				return false;
			}
		}
		else {
			return false;
		}
		return true;
	}
	public boolean movingY(GameObject hit) {
		if(this.vy>0) {
			if(hit.y<this.y+this.height-this.vy) {
				return false;
			}
		}
		else if(this.vy<0) {
			if(hit.y+hit.height>this.y-this.vy) {
				return false;
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	public boolean movingTowards(GameObject hit) {
		return (this.movingX(hit)||this.movingY(hit));
	}
}
