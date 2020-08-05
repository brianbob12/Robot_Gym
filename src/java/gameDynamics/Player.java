package gameDynamics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	
	private Keyboard keyboard;
	
	//images
	private Image playerRight;
	private Image playerRightWalking;
	private Image playerLeft;
	private Image playerLeftWalking;
	
	private int walkTimer=0;//time since last walk switch
	private int walkDelay=10;//the number of frames between switching images whist walking
	
	public Player(float x,float y,Level level,Keyboard keyboard) {
		//the values for player height width and maxHealth will eventually imported from a config file
		super(x, y,10F,25F,level,100F);
		try {
			this.playerRight=ImageIO.read(new File("assets/images/PlayerRight.png"));
			this.playerRightWalking=ImageIO.read(new File("assets/images/PlayerRightWalk.png"));
			this.playerLeft=ImageIO.read(new File("assets/images/PlayerLeft.png"));
			this.playerLeftWalking=ImageIO.read(new File("assets/images/PlayerLeftWalk.png"));
			this.setDisplayImage(playerRight);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	@Override
	public void die() {
		
	}
	//this override deals strictly with the display of the player
	@Override
	public void move() {
		super.move();
		//check for moveing for 
		if(this.choseActionA()==1) {//move right
			if(!this.onPlatform()) {//this is slow
				this.displayImage=playerRight;
				this.walkTimer=0;
			}
			else if(this.displayImage!=this.playerRight&&this.displayImage!=this.playerRightWalking) {
				this.displayImage=playerRight;
				this.walkTimer=0;
			}
			else if(this.displayImage==playerRight&&this.walkTimer>this.walkDelay) {
				this.displayImage=playerRightWalking;
				this.walkTimer=0;
			}
			else if(this.walkTimer>this.walkDelay){
				this.displayImage=playerRight;
				this.walkTimer=0;
			}
			else {
				walkTimer+=1;
			}
		}
		else if(this.choseActionA()==2) {//move right
			if(!this.onPlatform()) {//this is slow
				this.displayImage=playerLeft;
				this.walkTimer=0;
			}
			else if(this.displayImage!=this.playerLeft&&this.displayImage!=this.playerLeftWalking) {
				this.displayImage=playerLeft;
				this.walkTimer=0;
			}
			else if(this.displayImage==playerLeft&&this.walkTimer>this.walkDelay) {
				this.displayImage=playerLeftWalking;
				this.walkTimer=0;
			}
			else if(this.walkTimer>this.walkDelay){
				this.displayImage=playerLeft;
				this.walkTimer=0;
			}
			else {
				walkTimer+=1;
			}
		}
	}
}
