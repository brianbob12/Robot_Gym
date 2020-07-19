package gameDynamics;

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
	public boolean harmable=true;
	public int team=0;//stores a marker of team for friendly fire purposes
	
	public Destructible(float x, float y, float width, float height, boolean gravity, boolean moveable,Level level,float maxHealth) {
		super(x, y, width, height, gravity, moveable,level);
		this.setMaxHealth(maxHealth);
		this.currentHealth=maxHealth;
		this.dead=false;
	}
	
	public void damage(float damage) {
		if(this.harmable) {
			this.currentHealth-=damage;
			if(this.currentHealth<0) {
				this.die();
			}
		}
	}
	
	public void heal(float h) {
		this.currentHealth+=h;
		if (this.currentHealth>this.getMaxHealth()) {
			this.currentHealth=this.getMaxHealth();
		}
	}
	
	public void die() {
		if(this.harmable) {
			this.dead=true;
			this.level.getObjects().remove(this);
		}
	}
	public void setMaxHealth(float h) {
		this.maxHealth=h;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

}
