package tools;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.event.*;

/**
 * Keyboard
 * 
 * An object to manage keypresses and relevant keyDowns.
 *
 */
public class Keyboard implements KeyListener {
	
	public boolean w=false;
	public boolean a=false;
	public boolean s=false;
	public boolean d=false;
	public boolean space=false;
	public boolean ctrl=false;
	
	public Keyboard() {
		
	}
	
	public void keyPressed(KeyEvent event) {
		switch(event.getKeyChar()) {
			case 'a':
				this.a=true;
				break;
			case 'A':
				this.a=true;
				break;
			case 'w':
				this.w=true;
				break;
			case 'W':
				this.w=true;
				break;
			case 's':
				this.s=true;
				break;
			case 'S':
				this.s=true;
				break;
			case 'd':
				this.d=true;
				break;
			case 'D':
				this.d=true;
				break;
			case 32:
				this.space=true;
				break;
		}
	}
	public void keyReleased(KeyEvent event){
		switch(event.getKeyChar()) {
		case 'a':
			this.a=false;
			break;
		case 'A':
			this.a=false;
			break;
		case 'w':
			this.w=false;
			break;
		case 'W':
			this.w=false;
			break;
		case 's':
			this.s=false;
			break;
		case 'S':
			this.s=false;
			break;
		case 'd':
			this.d=false;
			break;
		case 'D':
			this.d=false;
			break;
		case 32:
			this.space=false;
			break;
	}
	}
	public void keyTyped(KeyEvent event) {
		
	}
}
