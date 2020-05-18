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
	//outputs the data point as a string
	//data is split by semicolons and then commas
	public String outputAsstring() {
		String out="";
		for(int i:this.state) {
			out+=Integer.toString(i);
			out+=",";
		}
		out=out.substring(0, out.length()-1);
		out+=";";
		for(int i:this.action) {
			out+=Integer.toString(i);
			out+=",";
		}
		out=out.substring(0, out.length()-1);
		out+=";";
		out+=Float.toString(this.reward);
		out+=";";
		for(int i:this.nextState) {
			out+=Integer.toString(i);
			out+=",";
		}
		out=out.substring(0, out.length()-1);
		out+=";";
		for(int i:this.nextAction) {
			out+=Integer.toString(i);
			out+=",";
		}
		out=out.substring(0, out.length()-1);
		return out;
	}
	
	//determines weather two dataPionts are similar in state or nextState
	public boolean similar(AgentDataPoint x) {
		if(this.state.equals(x.getNextState())) {
			return true;
		}
		if(this.nextState.equals(x.getState())) {
			return true;
		}
		if(this.state.equals(x.getState())) {
			return true;
		}
		if(this.nextState.equals(x.getNextState())) {
			return true;
		}
		return false;
	}
	
	public List<Integer> getState(){
		return this.state;
	}
	public List<Integer> getNextState(){
		return this.nextState;
	}
}
