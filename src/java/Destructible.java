/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */


/**
 * Destructible
 * 
 * This class is a GameObject with the ability to die.
 *
 */

public class Destructible extends GameObject {

	private float maxHealth;
	public float currentHealth;
	public boolean dead;
	public int team=0;//stores a marker of team for friendly fire purposes
	
	public Destructible(float x, float y, float width, float height, boolean gravity, boolean moveable,Level level,float maxHealth) {
		super(x, y, width, height, gravity, moveable,level);
		this.maxHealth=maxHealth;
		this.currentHealth=maxHealth;
		this.dead=false;
	}
	
	public void damage(float damage) {
		this.currentHealth-=damage;
		if(this.currentHealth<0) {
			this.die();
		}
	}
	
	public void heal(float h) {
		this.currentHealth+=h;
		if (this.currentHealth>this.maxHealth) {
			this.currentHealth=this.maxHealth;
		}
	}
	
	public void die() {
		this.dead=true;
		this.level.objects.remove(this);
	}

}
