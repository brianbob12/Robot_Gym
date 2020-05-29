package gameDynamics;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */


/**
 * Enemy 
 * 
 * Shoots and attacks Competitors
 *
 */

//WORK IN PROGRES
public class Enemy extends Destructible {

	public Enemy(float x, float y, float width, float height, Level level,
			float maxHealth) {
		super(x, y, width, height, true, true, level, maxHealth);
		this.type=objectType.ENEMY;
	}

}
