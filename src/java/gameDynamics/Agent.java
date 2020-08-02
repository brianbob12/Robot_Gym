package gameDynamics;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;

import tools.FileManager;

import java.io.IOException;
import java.lang.Math;
/**
 * Agent
 * The class for the clone agents that play the game off a neural network.
 * IMPORTANT: Whilst the Agent handled by the python stuff has two neural networks. Any agent in the game will only
 * be acting with one.
 */

public class Agent extends Competitor {
	
	public String name;
	
	//Neural network parameters
	//Matrices are used here for easer computation
	private List<Matrix> weights;
	private List<Matrix> biases;
	private int stateSpace=864;//better to import this from a config file
	private int actionSpace=18;
	private List<Integer> nHidden;
	private List<String> activation;//stores activation functions for each layer
	
	//observation stuff
	private transient Grid myGrid;
	private int nGh=12;//horizontal view range(grid space)
	private int nGv=8;//vertical view range(grid space)
	private int sG=7;//grid cell size in level space
	private int SGS=3;//number of grid observations in the observed state , this is reasigned in the Level objet
	
	//I think this is redundant... not sure
	private List<List<Integer>> frames;//stores the grid outputs before evaluation max length of SGS
	
	//current action variables
	private int selectedActionA=0;
	private int selectedActionB=0;
	private int selectedActionC=0;
	
	private float epsilon=0F;//the proportion of completely random action taken 
	
	public boolean training=false;//this stores weather or not this agent is currently learning and collecting data
	public boolean lastExport=false;//ready to export
	
	private List<AgentDataPoint> data=new ArrayList<AgentDataPoint>();//used for training future agents
	
	private List<Integer> lastState;//stores the last state(made from observations) of the agent
	private float lastScore;
	//used in training
	
	//getters and setters
	protected void setWeights(List<Matrix> w) {
		this.weights=w;
	}
	protected void setBiases(List<Matrix> b) {
		this.biases=b;
	}
	protected void setNHidden(List<Integer> h) {
		this.nHidden=h;
	}
	protected void setActivation(List<String> a) {
		this.activation=a;
	}
	protected void setStateSpace(int s) {
		this.stateSpace=s;
	}
	protected void setActionSpace(int a) {
		this.actionSpace=a;
	}

	
	public Agent(float x, float y, Level level) {
		super(x, y, 10F, 20F, level, 100F);
	}
	
	//read network from csv
	public void importNetwork(String path) throws IOException {
		//read hyper.txt for general data
		List<String> hyperData=FileManager.readByLine(path+"/hyper.txt");
		
		String[]  hiddenData=hyperData.get(2).split(",");
		this.nHidden=new ArrayList<Integer>();
		for(String i: hiddenData) {
			this.nHidden.add(Integer.parseInt(i));
		}
		String[]  activationData=hyperData.get(3).split(",");
		this.activation=new ArrayList<String>();
		for(String i: activationData) {
			this.activation.add(i);
		}
		//import weights and biases as matrices.
		this.weights=new ArrayList<Matrix>();
		this.biases=new ArrayList<Matrix>();
		//first hidden layer
		List<List<Double>>tad=new ArrayList<List<Double>>();//working variable
		List<Float> raw=FileManager.readAsFloatList(path+"/w0.weights");
		
		this.weights.add(new Matrix(this.nHidden.get(0),this.stateSpace));//note: stateSpace is equivalent to input layer size
		for(int j=0;j<this.nHidden.get(0);j++) {//reshape list
			List<Double> tad2=new ArrayList<Double>();
			for(int k=0;k<this.stateSpace;k++) {
				tad2.add((double)raw.get(k*this.nHidden.get(0)+j));
			}
			tad.add(tad2);
		}
		this.weights.get(0).setData(tad);
		
		tad=new ArrayList<List<Double>>();//working variable
		raw=FileManager.readAsFloatList(path+"/b0.biases");
		this.biases.add(new Matrix(this.nHidden.get(0),1));
		for(int j=0;j<this.nHidden.get(0);j++) {
			List<Double> tad2=new ArrayList<Double>();
			tad2.add((double)raw.get(j));
			tad.add(tad2);
		}
		this.biases.get(0).setData(tad);
		//for each layer excluding the first hidden layer and the output layer
		for(int i=1;i<this.nHidden.size();i++) {
			//import weights
			tad=new ArrayList<List<Double>>();//working variable
			raw=FileManager.readAsFloatList(path+"/w"+Integer.toString(i)+".weights");
			this.weights.add(new Matrix(this.nHidden.get(i),this.nHidden.get(i-1)));//note: stateSpace is equivalent to input layer size
			for(int j=0;j<this.nHidden.get(i);j++) {//reshape the list
				List<Double> tad2=new ArrayList<Double>();
				for(int k=0;k<this.nHidden.get(i-1);k++) {
					tad2.add((double)raw.get(k*this.nHidden.get(i)+j));
				}
				tad.add(tad2);
			}
			this.weights.get(i).setData(tad);//add matrix to weights
			
			//import biases
			tad=new ArrayList<List<Double>>();//working variable
			raw=FileManager.readAsFloatList(path+"/b"+Integer.toString(i)+".biases");
			this.biases.add(new Matrix(this.nHidden.get(i),1));
			for(int j=0;j<this.nHidden.get(i);j++) {
				List<Double> tad2=new ArrayList<Double>();
				tad2.add((double)raw.get(j));
				tad.add(tad2);
			}
			this.biases.get(i).setData(tad);
		}
		//output layer
		//weights
		tad=new ArrayList<List<Double>>();//working variable
		raw=FileManager.readAsFloatList(path+"/w"+Integer.toString(this.nHidden.size())+".weights");
		this.weights.add(new Matrix(this.actionSpace,this.nHidden.get(this.nHidden.size()-1)));//note: actionSpace is equivalent to number of neurons in the output layer
		for(int j=0;j<this.actionSpace;j++) {//reshape list
			List<Double> tad2=new ArrayList<Double>();
			for(int k=0;k<this.nHidden.get(this.nHidden.size()-1);k++) {
				tad2.add((double)raw.get(k*this.actionSpace+j));
			}
			tad.add(tad2);
		}
		this.weights.get(this.nHidden.size()).setData(tad);
		//biases
		tad=new ArrayList<List<Double>>();//working variable
		raw=FileManager.readAsFloatList(path+"/b"+Integer.toString(this.nHidden.size())+".biases");
		this.biases.add(new Matrix(this.actionSpace,1));
		for(int j=0;j<this.actionSpace;j++) {
			List<Double> tad2=new ArrayList<Double>();
			tad2.add((double)raw.get(j));
			tad.add(tad2);
		}
		this.biases.get(this.nHidden.size()).setData(tad);
	}
	
	//Evaluates the neural network for a single input
	//keeps everything as float
	//returns output layer
	
	private List<Double> evaluateNetwork(List<Double> inp) throws IndexOutOfBoundsException{
		List<Matrix> layerVals=new ArrayList<Matrix>();//stores layer values
		
		//convert inp to matrix
		layerVals.add(new Matrix(this.stateSpace,1));
		List<List<Double>> inpData=new ArrayList<List<Double>>();
		for(double i: inp) {
			inpData.add(new ArrayList<Double>());
			inpData.get(inpData.size()-1).add(i);
		}
		layerVals.get(0).setData(inpData);
		//for each hidden layer and the output layer
		for(int i=1;i<this.nHidden.size()+2;i++) {
			layerVals.add(layerVals.get(i-1).multiply(this.weights.get(i-1)).add(this.biases.get(i-1)));//throws index out of bounds
			//apply activation function
			List<List<Double>> nums=layerVals.get(i).getData();
			for(int j=0;j<nums.size();j++) {
				for(int k=0;k<nums.get(j).size();k++) {
					if(this.activation.get(i-1).equals("sigmoid")) {
						nums.get(j).set(k, this.sigmoid(nums.get(j).get(k)));
					}
					else if(this.activation.get(i-1).equals("linear")) {
						continue;
					}
					else if(this.activation.get(i-1).equals("tanh")) {
						nums.get(j).set(k, Math.tanh(nums.get(j).get(k)));
					}
				}
			}
			
		}
		//return last layer
		return layerVals.get(this.nHidden.size()+1).getRow(0);
	}
	
	//grid stuff
	//note: grids are centred around(x+with,y)
	//function used to establish a new grid
	public void setupGrid() {
		this.myGrid=new Grid(this.level,this.x+this.width,this.y);
		this.myGrid.generateGrid();
	}
	//returns grid output for visible squares a flattened array
	private List<Integer> observe() {
		if(!this.moved) {
			//grid update is applicable
			this.myGrid.updateGrid();
		}
		else {
			//new grid needed
			//this process in inefficient and a grid-sharing system should be worked out at some point
			this.setupGrid();
		}
		//export relevant grid squares
		List<Integer> output=new ArrayList<Integer>();
		float startPointX=this.x+this.width-(this.nGh/2)*this.sG;
		float startPointY=this.y-(this.nGv/2)*this.sG;
		float endPointX=this.x+this.width+(this.nGh/2)*this.sG;
		float endPointY=this.y+(this.nGv/2)*this.sG;

		//for all of the relevant cells
		for(float tracerX=startPointX;tracerX<endPointX;tracerX+=this.sG) {
			for(float tracerY=startPointY;tracerY<endPointY;tracerY+=this.sG) {
				
				int[] tad=this.myGrid.getCellExport(tracerX, tracerY);
				for(int i:tad) {
					output.add(i);
				}
			}
		}
		//System.out.println(output);
		return output;
	}
	
	//the sigmoid function for evaluating neural networks
	//THIS MAY NOW BE REDUNDANR
	private double sigmoid(double x) {
		return (1/(1+Math.pow(Math.E,-1*x)));
	}
	
	//override action functions
	@Override
	public int choseActionA() {
		//possible random action
		//epsilon=0 in levels with the player
		if(Math.random()< this.epsilon) {
			//take random action
			return (int) Math.floor(Math.random()*3);
		}
		return this.selectedActionA;
	}
	@Override
	public int choseActionB() {
		//possible random action
		//epsilon=0 in levels with the player
		if(Math.random()< this.epsilon) {
			//take random action
			return (int) Math.floor(Math.random()*2);
		}
		return this.selectedActionB;
	}
	@Override
	public int choseActionC() {
		//possible random action
		//epsilon=0 in levels with the player
		if(Math.random()< this.epsilon) {
			//take random action
			return (int) Math.floor(Math.random()*3);
		}
		return this.selectedActionC;
	}
	
	//returns this.SGS
	public int getSGS() {
		return this.SGS;
	}
	public void setSGS(int val) {
		this.SGS=val;
	}
	//this.frames interactions
	public void clearFrames() {
		this.frames=new ArrayList<List<Integer>>();
	}
	//adds an observation to the frames
	public void addFrame() {
		this.frames.add(this.observe());
	}
	//removes the first item from this.frames
	public void popFrame() {
		this.frames.remove(0);
	}
	
	//requires this.frames.size()==SGS
	//evaluates the network and sets a new action for the agent
	public void newAction() {
		//safety check
		if(this.frames.size()!=SGS) {
			System.out.print(this.frames.size());
			System.out.println(" incorrect frames size");
			return;
		}
		//flatten frames
		List<Double> networkInput=new ArrayList<Double>();
		
		for(List<Integer> i:this.frames) {
			for(int j:i) {
				networkInput.add((double) j);
			}
		}
		//for potential data collection
		int oldActionA=this.selectedActionA;
		int oldActionB=this.selectedActionB;
		int oldActionC=this.selectedActionC;
		//evaluate network
		try {
			List<Double> rawOutput=this.evaluateNetwork(networkInput);
			//System.out.println(rawOutput);
			//take argmax of neural network
			int macroAction=this.argMax(rawOutput);
			this.selectedActionA=macroAction%3;
			macroAction-=this.selectedActionA;
			macroAction=macroAction/3;
			this.selectedActionB=macroAction%2;
			macroAction-=macroAction-this.selectedActionB;
			macroAction=macroAction/2;
			this.selectedActionC=macroAction;
			if (this.training) {
				//log history
				if(this.dead||this.finished) {
					this.lastExport=true;
				}
				List<Integer> stateForExport=new ArrayList<Integer>();//flattened int of this.frames
				for(List<Integer> i:this.frames) {
					for(int j:i) {
						stateForExport.add(j);
					}
				}
				if(stateForExport.size()==0) {
					//something has gone horribly wrong
					System.out.println("empy flat state");
					return;
				}
				//System.out.println(stateForExport);
				if(this.lastState!=null) {
					float reward=this.getTotalScore()-this.lastScore;
					this.saveData(stateForExport,oldActionA,oldActionB,oldActionC,reward,this.lastState,this.selectedActionA,this.selectedActionB,this.selectedActionC);
				}
				this.lastState=stateForExport;
				this.lastScore=this.getTotalScore();
			}
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("NETWORK EVALUATION ERROR");
			System.out.println(e);
			System.out.println(networkInput.size());
			this.selectedActionA=oldActionA;
			this.selectedActionB=oldActionB;
			this.selectedActionC=oldActionC;
		}

	}
	
	//exports the index of the highest value in input
	private int argMax(List<Double> x) {
		int maxIndex=0;
		for(int i=1;i<x.size();i++) {
			if(x.get(i)>x.get(maxIndex)) {
				maxIndex=i;
			}
		}
		return maxIndex;
	}
	
	//decides on the value of this (state,action,reward,nextState) for learning
	//next state is given as a state action pair and is evaluated by the python section later on in the training process
	//saves datapoint if it is generally unique
	private void saveData(List<Integer> state,int actionA,int actionB,int actionC,float reward,List<Integer> statePrime,int actionAPrime,int actionBPrime,int actionCPrime) {
		//flatten Actions
		//oneHot actions
		List<Integer> action= new ArrayList<Integer>();
		List<Integer> nextAction= new ArrayList<Integer>();
		for(int i=0;i<18;i++) {
			action.add(0);
			nextAction.add(0);
		}
		action.set(actionC*6+actionB*3+actionA, 1);
		nextAction.set(actionCPrime*6+actionBPrime*2+actionAPrime, 1);
		AgentDataPoint tad=new AgentDataPoint(state,action,reward,statePrime,nextAction);
		//decide if relevant
		//THIS IS VERY INFLUENTIAL on the training process
		//for(AgentDataPoint i: this.data) {
		//	if(tad.similar(i)) {
		//		return;
		//	}
		//}
		this.data.add(tad);
		
	}
	
	//export data upon death
	@Override
	public void die() {
		this.dead=true;
	}
	
	//exports the saved experience data
	public void exportData(String path) throws IOException{
		List<String> toExport = new ArrayList<String>();
		
		//iterate over datapoints
		for(AgentDataPoint point: data) {
			toExport.add(point.toString());
		}
		FileManager.writeToTxt(path, toExport);
	}
	
	//produces a copy of the agent sharing the same network and level but not the same grid.
	public Agent middleCopy() {
		Agent out= new Agent(this.x,this.y,this.level);
		//just passing pointers here
		out.setWeights(this.weights);
		out.setBiases(this.biases);
		out.setNHidden(this.nHidden);
		out.setActivation(this.activation);
		out.setStateSpace(this.stateSpace);
		out.setActionSpace(this.actionSpace);
		
		return out;
		
	}
	
	public void setEpsilon(float val) {
		this.epsilon=val;
	}
	
	/**
	 * AgentDataPiont
	 * Holds data points in the tuple(state,action,reward,nextstate,nextaction)
	 * Later, on the python side the nextstate,nextaction is resolved to a sum of discounted future rewards(Q-value)
	 * This is then fed as data to train the neural network.
	 * 
	 * bellman equation(we will use this to train NNs)
	 * Q(s,a) = r(s,a) + gamma*Q(s',a')
	 */

	public class AgentDataPoint {
		
		private List<Integer> state;//one state as input to NN
		private List<Integer> action;//one output of NN
		private float reward;//reward as float
		private List<Integer> nextState;//next state as input to NN
		private List<Integer> nextAction;//next action as input to NN
		
		
		
		public AgentDataPoint(List<Integer> state,List<Integer> action,float reward,List<Integer> nextState,List<Integer> nextAction){
			this.state=state;
			this.action=action;
			this.reward=reward;
			this.nextState=nextState;
			this.nextAction=nextAction;
		}
		//outputs the data point as a string
		//data is split by semicolons and then commas
		@Override
		public String toString() {
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
		
		//this is for similarity testing
		public List<Integer> getState(){
			return this.state;
		}
		//this is for similarity testing
		public List<Integer> getNextState(){
			return this.nextState;
		}
	}
	
	
}
