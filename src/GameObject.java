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
	
	//hitting
	//These store if the object is colliding in any directions
	public boolean hitUp=false;//e.g. if this is colliding with an object from above
	public boolean hitDown=false;
	public boolean hitLeft=false;
	public boolean hitRight=false;
	
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
		
		if (this.gravity&&!this.hitDown) {
			this.vy-=level.gravity;
		}
		if(this.vx<0) {
			if(this.hitLeft) {
				this.vx=0;
			}
		}
		else{
			if(this.hitRight) {
				this.vx=0;
			}
		}
		if(this.vy<0) {
			if(this.hitDown) {
				this.vy=0;
			}
		}
		else{
			if(this.hitUp) {
				this.vy=0;
			}
		}
		this.x+=this.vx;
		this.y+=this.vy;
		this.hitUp=false;
		this.hitDown=false;
		this.hitLeft=false;
		this.hitRight=false;
	}
	
	//reverses position by one frame used for resolving collisions
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
				if(!this.movingTowards(hit)) {
					hit.collide(this);
				}
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
	
	public boolean pointInBox(float x,float y,GameObject hit) {
		return (x<hit.x+hit.width&&x>hit.x&&y<hit.y+hit.height&&y>hit.y);
	}
	public boolean movingX(GameObject hit) {
		if(this.vx>0) {
			if(hit.x<this.x) {
				return false;
			}
		}
		else if(this.vx<0) {
			if(hit.x>this.x) {
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
			if(hit.y<this.y) {
				return false;
			}
		}
		else if(this.vy<0) {
			if(hit.y>this.y) {
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
