/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
/**
 * AgentDataPiont
 * Holds data points in the tuple(state,action,reward,nextstate,nextaction)
 * Later, on the python side the nextstate,nextaction is resolved to a sum of discounted future rewards(Q-value)
 * This is then fed as data to train the neural network.
 */

public class AgentDataPoint {
	
	private List<Integer> state;
	private List<Integer> action;
	private float reward;
	private List<Integer> nextState;
	private List<Integer> nextAction;
	
	public AgentDataPoint(List<Integer> state,List<Integer> action,float reward,List<Integer> nextState,List<Integer> nextAction){
		this.state=state;
		this.action=action;
		this.reward=reward;
		this.nextState=nextState;
		this.nextAction=nextAction;
	}
}
