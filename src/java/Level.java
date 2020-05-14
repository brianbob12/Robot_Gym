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
	float startPosition;
	
	public Level() {
		
	}
	//I think I might need this later
	public void addObject(GameObject obj) {
		this.objects.add(obj);
	}
	//physics update
	public void step() {
		for(int i=0;i<this.objects.size();i++) {
			GameObject sel=this.objects.get(i);
			if(sel.moveable) {
				sel.move();
			}
		}
	}
}
