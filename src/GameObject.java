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
	private boolean hitUp=false;//e.g. if this is colliding with an object from above
	private boolean hitDown=false;
	private boolean hitLeft=false;
	private boolean hitRight=false;
	
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
	public void backstep(float fraction) {
		float newvx=-this.vx/fraction;
		float newvy=-this.vy/fraction;
		
		if (this.gravity) {
			newvy+=level.gravity;
		}
		if(newvx<0) {
			if(hitLeft) {
				newvx=0;
			}
		}
		else{
			if(hitRight) {
				newvx=0;
			}
		}
		if(this.vy<0) {
			if(hitDown) {
				newvy=0;
			}
		}
		else{
			if(hitUp) {
				newvy=0;
			}
		}
		this.x+=newvx;
		this.y+=newvy;
	}
	
	//check for collision and resolve collision
	//Returns whether the objects are in collision
	public void collide(GameObject hit) {
		if(this.collidable) {
			if(this.x<=hit.x+hit.width&&this.x>=hit.x&&this.y<=hit.y+hit.height&&this.y>=hit.y) {
				//collision detected
				float colFract;
				colFract=(float) 10;
				while((this.x<=hit.x+hit.width&&this.y<=hit.y+hit.height)&&(hit.x<=this.x+this.width&&hit.y<=this.y+this.height)) {
					this.backstep(colFract);
				}
				
				//update hits
				if(this.x>hit.x+hit.width) {
					this.hitLeft=true;
				}
				else if(this.x+this.width<hit.x) {
					this.hitRight=true;
				}
				else if(this.y>hit.y+hit.height) {
					this.hitDown=true;
				}
				else if(this.y+this.height<hit.y) {
					this.hitUp=true;
				}
			}
		}
	}
}
