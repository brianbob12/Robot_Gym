package gameDynamics;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Level
 * 
 * This class holds all objects and conducts collisions and game ticks. 
 *
 */

public class Level implements Serializable {
	
	private List<GameObject> objects=new ArrayList<GameObject>();//stores the gameobjects in the level
	private transient List<Agent> agents=new ArrayList<Agent>();
	private float gravity=(float) 0.1;
	private float endGoal;//the x value that shows the end of the level
	private float startPosition=0;
	int levelSGS=3;//SGS is the number of frames in the agent's state
	int agentUpdateTime=10;//the number of physics frames that the agents are updated
	private int counter=0;//Counts physics updates up to agentUpdateTime NOTE: this MUST start on 0
	public transient float epsilon=0F;
	
	public transient boolean trainingLevel=true;//if this level is bing played or if the agents are learning to play it.
	
	//getters and setters
	public void setEndGoal(float n) {
		endGoal=n;
	}
	public float getEndGoal() {
		return endGoal;
	}
	public void setGravity(float n) {
		gravity=n;
	}
	public float getGravity() {
		return gravity;
	}
	public List<GameObject> getObjects(){
		return objects;
	}
	public float getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(float startPosition) {
		this.startPosition = startPosition;
	}
	
	public Level() {
		
	}

	public void addObject(GameObject obj) {
		this.objects.add(obj);
	}
	
	public void addAgent(Agent a) {
		this.objects.add(a);
		this.agents.add(a);
		a.setSGS(this.levelSGS);
		a.clearFrames();
		a.training=this.trainingLevel;
		a.setEpsilon(this.epsilon);
	}
	
	//physics update
	public void step() {
		for(GameObject sel: this.objects) {
			if(sel.moveable) {
				sel.move();
			}
		}
		this.counter+=1;
		this.counter%=this.agentUpdateTime;
		//agent stuff
		if(this.counter==0) {
			//update agents
			for(Agent sel: this.agents) {
				if(!sel.lastExport) {
					sel.addFrame();
					sel.newAction();
				}
			}
		}
		//record actions approaching the next agent update
		for(int i=1;i<levelSGS;i++) {
			if(this.counter==this.agentUpdateTime-i) {
				for(Agent sel: this.agents) {
					if(!sel.lastExport) {
						sel.addFrame();
					}
				}
			}
		}
		
	}
	public void setUpAgents() {
		for(Agent sel: this.agents) {
			sel.setupGrid();
		}
	}
	
	public void endLevel() {
		//TODO work out hot to find a path for this data
		//TODO actually handle IOExceptoin
		for(Agent sel: this.agents) {
			try {
				sel.exportData("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void postImportSetup() {
		agents=new ArrayList<Agent>();
	}
	
	
}
