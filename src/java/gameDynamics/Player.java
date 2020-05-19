package gameDynamics;
import tools.Keyboard;

/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */


/**
 * Player
 * 
 * Player controllable object.
 *
 */

public class Player extends Competitor{
	
	Keyboard keyboard;
	
	public Player(float x,float y,Level level,Keyboard keyboard) {
		//the values for player height width and maxHealth will eventually imported from a config file
		super(x, y,10F,20F,level,100F);
		this.keyboard=keyboard;
	}
	
	@Override
	public int choseActionA() {
		if (this.keyboard.d) {
			return 1;
		}
		if (this.keyboard.a) {
			return 2;
		}
		return 0;
	}
	@Override
	public int choseActionB() {
		if(this.keyboard.space) {
			return 1;
		}
		return 0;
	}
	@Override
	public int choseActionC() {
		return 0;
	}
}
