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
	float x;//x level-space position(this differs from position on screen)
	float y;//y level-space position
	float width;
	float height;
	float vx;
	float vy;
	float gravVal;//value of the acceleration due to gravity
	boolean gravity;//is it affected by gravity
	boolean collidable=true;
	boolean moveable;//Weather this object is able to be moved
	Color color=null;//null if object is immovable
	
	//hitting
	//These store if the object is colliding in any directions
	boolean hitUp=false;//e.g. if this is colliding with an object from above
	boolean hitDown=false;
	boolean hitLeft=false;
	boolean hitRight=false;
	
	public GameObject(float x,float y,float width,float height,boolean gravity,boolean moveable) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.vx=0;
		this.vy=0;
		this.gravity=gravity;
		this.moveable=moveable;
	}
	
	//update position
	public void move() {
		
		if (this.gravity) {
			this.vy-=gravVal;
		}
		if(this.vx<0) {
			if(hitLeft) {
				this.vx=0;
			}
		}
		else{
			if(hitRight) {
				this.vx=0;
			}
		}
		if(this.vy<0) {
			if(hitDown) {
				this.vy=0;
			}
		}
		else{
			if(hitUp) {
				this.vy=0;
			}
		}
		this.x+=this.vx;
		this.y+=this.vy;
		hitUp=false;
		hitDown=false;
		hitLeft=false;
		hitRight=false;
	}
	
	//reverses position by one frame used for resolving collisions
	public void backstep(float fraction) {
		float newvx=-this.vx/fraction;
		float newvy=-this.vy/fraction;
		
		if (this.gravity) {
			newvy+=gravVal;
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
				if (Math.abs(this.vx)<Math.abs(this.vy)){
					colFract=Math.abs(vy)*20;
				}
				else {
					colFract=Math.abs(vx)*20;
				}
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
