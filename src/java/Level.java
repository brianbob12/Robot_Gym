/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.util.*;
import java.util.List;

/**
 * Level
 * 
 * This class holds all objects and conducts collisions and game ticks. 
 *
 */

public class Level {
	
	public List<GameObject> objects=new ArrayList<GameObject>();//stores the gameobjects in the level
	public List<Agent> agents=new ArrayList<Agent>();
	public float gravity=(float) 0.1;
	public float endGoal;//the x value that shows the end of the level
	public float startPosition;
	int levelSGS=3;//SGS is the number of frames in the agent's state
	int agentUpdateTime=10;//the number of physics frames that the agents are updated
	private int counter=0;//Counts physics updates up to agentUpdateTime NOTE: this MUST start on 0
	public float epsilon=0F;
	
	public boolean trainingLevel=true;//if this level is bing played or if the agents are learning to play it.
	
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
		for(Agent sel: this.agents) {
			sel.exportData("");
		}
	}
}
