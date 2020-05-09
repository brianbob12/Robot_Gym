/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.awt.event.*;

/**
 * Player
 * 
 * Player controllable object.
 *
 */

public class Player extends Competitor {
	public Player(float x,float y,Level level) {
		//the values for player height width and maxHealth will eventually imported from a config file
		super(x, y,(float) 10,(float) 20,level,(float) 100);
	}
	
	public int choseActionA() {
		return 0;
	}
	public int choseActionB() {
		return 0;
	}
	public int choseActionC() {
		return 0;
	}
}
