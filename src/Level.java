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
	
	public List<GameObject> objects=new ArrayList<GameObject>();
	public float gravity=(float) 0.1;
	
	public Level() {
		//this represents the size of the co-ordinate space in this level. This is only positive and starts from the bottom left. 
	}
	public void addObject(GameObject obj) {
		this.objects.add(obj);
		if(obj.gravity) {
			obj.gravVal=gravity;
		}
	}
	public void step() {
		for(int i=0;i<this.objects.size();i++) {
			GameObject sel=this.objects.get(i);
			if(sel.moveable) {
				sel.move();
				for(int j=0;j<objects.size();j++) {
					if(i!=j) {//avoid self collisions
						sel.collide(this.objects.get(j));
					}
				}
			}
		}
	}
}
