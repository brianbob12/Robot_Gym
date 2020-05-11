/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

/**
 * Competitor
 * 
 * This rather small class holds information for Agents and the Player. This is mostly for holding score.
 * There are never going to be an instances of Competitor, only subclasses
 *
 */
public class Competitor extends Destructible {
	
	public float moveScore;//score derived from moving
	public float enemiesScore;//score derived from killing enemies
	public float xOffset;//holds the original x position of the Competitor for score calculations.
	public boolean finished;
	public float speed=(float)0.6;//walking speed of competitor
	public float jumpForce=(float) 2.0;//velocity added through jumping
	public int jumpDelay=5;
	private int jumpCount=0;
	
	public Competitor(float x, float y, float width, float height,Level level, float maxHealth) {
		super(x, y, width, height, true, true,level,maxHealth);
		this.xOffset=x;
		this.enemiesScore=0;
		this.moveScore=0;
	}
	
	//updates the movement score & check for finished
	public void move() {
		if(!this.finished) {
			int actionA=this.choseActionA();
			int actionB=this.choseActionB();
			int actionC=this.choseActionC();
			this.vx=0;
			if (actionA==1) {
				this.vx=this.speed;
			}
			else if(actionA==2){
				this.vx=-this.speed;
			}
			if(this.onPlatform()) {
				this.jumpCount+=1;
			}
			if(actionB==1&&this.jumpCount>=this.jumpDelay) {
				this.vy=this.jumpForce;
				this.jumpCount=0;
			}
			super.move();
			if(this.x>level.endGoal) {
				this.finished=true;
			}
		}
		this.moveScore=this.x-this.xOffset;
	}
	//returns the total score
	public float getTotalScore() {
		//very important parameters in determining score
		//These will later be imported from a config file
		float moveScoreWeight=(float) 0.01;
		float enemiesScoreWeight=1;
		float finishedScoreWeight=1;
		float out=this.moveScore*moveScoreWeight+this.enemiesScore+enemiesScoreWeight;
		
		if(this.finished) {
			out+=1000*finishedScoreWeight;
		}
		return out;
	}
	
	public boolean onPlatform() {
		for(int i=0;i<this.level.objects.size();i++) {//iterate over objects
			GameObject sub=this.level.objects.get(i);
			if((sub.x<this.x&&this.x<sub.x+sub.width)||(sub.x<this.x+this.width&&this.x+this.width<sub.x+sub.width)||(this.x<sub.x&&sub.x<this.x+this.width)||(this.x<sub.x+sub.width&&sub.x+sub.width<this.x+this.width)) {//check if on platform with respect to x
				if(this.y==sub.y+sub.height) {
					return true;
				}
			}
		}
		return false;
	}
	
	//these only exists for the subclasses
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
