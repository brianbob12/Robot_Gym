package gameDynamics;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */


/**
 * Deadly 
 * 
 * Kills Destructibles that come into contact with it
 *
 */

//WORK IN PROGRES
public class Deadly extends Destructible {
	
	public Deadly(float x, float y, float width, float height, boolean gravity, boolean moveable, Level level,
			float maxHealth) {
		super(x, y, width, height, gravity, moveable, level, maxHealth);
		this.harmable=false;//Default choice
		this.type=objectType.DEADLY;
	}

}
