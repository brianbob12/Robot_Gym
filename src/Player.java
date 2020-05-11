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

public class Player extends Competitor{
	
	Keyboard keyboard;
	
	public Player(float x,float y,Level level,Keyboard keyboard) {
		//the values for player height width and maxHealth will eventually imported from a config file
		super(x, y,(float) 10,(float) 20,level,(float) 100);
		this.keyboard=keyboard;
	}
	
	public int choseActionA() {
		if (this.keyboard.d) {
			return 1;
		}
		if (this.keyboard.a) {
			return 2;
		}
		return 0;
	}
	public int choseActionB() {
		if(this.keyboard.space) {
			return 1;
		}
		return 0;
	}
	public int choseActionC() {
		return 0;
	}
}
