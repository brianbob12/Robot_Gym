/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */


/**
 * GameObject
 * 
 * This class holds a rectangular game object(like a box or player). It stores position level space and velocity.
 *
 */

public class GameObject {
	
	//dynamics
	float x;//x level-space position(this differs from postion on screen)
	float y;//y level-space position
	float vx;
	float vy;
	boolean gravity;//is it affected by gravity
	boolean colidable;
	int r;
	int g;
	int b;
	
	public GameObject(float x,float y,boolean gravity,boolean colidable) {
		this.x=0;
		this.y=0;
		this.vx=0;
		this.vy=0;
		this.gravity=gravity;
		this.colidable=colidable;
	}
	
	//configure the object to be viewed as a colored rectangle
	public void configView(int r,int g, int b) {
		this.r=r;
		this.g=g;
		this.b=b;
	}
}
