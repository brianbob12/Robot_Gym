/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
import java.lang.Math;
/**
 * Agent
 * The class for the clone agents that play the game off a neural network.
 * IMPORTANT: Whilst the Agent handled by the python stuff has two neural networks. Any agent in the game will only
 * be acting with one.
 */

public class Agent extends Competitor {
	
	//Neural network parameters
	//Matrices are used here for easer computation
	private List<Matrix> weights;
	private List<Matrix> biases;
	private FileManager myFileManager;
	private int stateSpace=864;//better to import this from a config file
	private int actionSpace=8;
	private List<Integer> nHidden;
	private List<String> activation;//stores activation functions for each layer
	
	//observation stuff
	private Grid myGrid;
	private int nGh=12;//horizontal view range(grid space)
	private int nGv=8;//vertical view range(grid space)
	private int sG=7;//grid cell size in level space
	private int SGS;//number of grid observations in the observed state
	private List<List<Integer>> frames;//stores the grid outputs before evaluation max length of SGS
	
	//current action variables
	private int selectedActionA=0;
	private int selectedActionB=0;
	private int selectedActionC=0;
	
	private float epsilon=0F;//the proportion of completely random action taken 
	
	private List<Double> lastState;//stores the last state(made from observations) of the agent
	//used in training
	
	public Agent(float x, float y, Level level) {
		super(x, y, 10F, 20F, level, 100F);
		
		this.myFileManager=new FileManager();
	}
	
	//read network from csv
	public void importNetwork(String path) {
		//read hyper.txt for general data
		List<String> hyperData=myFileManager.readByLine(path+"/hyper.txt");
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
		this.weights.add(new Matrix(this.nHidden.get(0),this.stateSpace));//note: stateSpace is equivilent to input layer size
		this.weights.get(0).setData(myFileManager.readRectangleCSV(path+"/w0.csv"));
		this.biases.add(new Matrix(this.nHidden.get(0),1));
		this.biases.get(0).setData(myFileManager.readRectangleCSV(path+"/b0.csv"));
		//for each layer excluding the first hidden layer and the output layer
		for(int i=1;i<this.nHidden.size();i++) {
			//import weights
			this.weights.add(new Matrix(this.nHidden.get(i),this.nHidden.get(i-1)));
			this.weights.get(i).setData(myFileManager.readRectangleCSV(path+"/w"+Integer.toString(i)+".csv"));
			this.biases.add(new Matrix(this.nHidden.get(i),1));
			this.biases.get(i).setData(myFileManager.readRectangleCSV(path+"/b"+Integer.toString(i)+".csv"));
		}
		//output layer
		this.weights.add(new Matrix(this.actionSpace,this.nHidden.get(this.nHidden.size()-1)));//note: stateSpace is equivilent to input layer size
		this.weights.get(this.nHidden.size()).setData(myFileManager.readRectangleCSV(path+"/w"+Integer.toString(this.nHidden.size())+".csv"));
		this.biases.add(new Matrix(this.actionSpace,1));
		this.biases.get(this.nHidden.size()).setData(myFileManager.readRectangleCSV(path+"/b"+Integer.toString(this.nHidden.size())+".csv"));
	}
	
	//Evaluates the neural network for a single input
	//keeps everything as float
	//returns output layer
	
	private List<Double> evaluateNetwork(List<Double> inp) {
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
			layerVals.add(layerVals.get(i-1).multiply(this.weights.get(i-1)).add(this.biases.get(i-1)));
			//apply activation function
			List<List<Double>> nums=layerVals.get(i).getData();
			for(int j=0;j<nums.size();j++) {
				for(int k=0;k<nums.get(j).size();k++) {
					if(this.activation.get(i-1).equals("sigmoid")) {
						nums.get(j).set(k, this.sigmoid(nums.get(j).get(k)));
					}
					else if(this.activation.get(i-1).equals("liniar")) {
						continue;
					}
				}
			};
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
		return output;
	}
	
	//the sigmoid function for evaluating neural networks
	private double sigmoid(double x) {
		return (1/(1+Math.pow(Math.E,-1*x)));
	}
	
	//override action functions
	@Override
	public int choseActionA() {
		return this.selectedActionA;
	}
	@Override
	public int choseActionB() {
		return this.selectedActionB;
	}
	@Override
	public int choseActionC() {
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
		
		//possible random action
		//epsilon=0 in levels with the player
		if(Math.random()< this.epsilon) {
			//take random action
			this.selectedActionA=(int) Math.floor(Math.random()*3);
			this.selectedActionB=(int) Math.floor(Math.random()*2);
			this.selectedActionC=(int) Math.floor(Math.random()*3);
		}
		else {
			//evaluate network
			List<Double> rawOutput=this.evaluateNetwork(networkInput);
			//take argmax of each action type
			List<Double> actionsA=rawOutput.subList(0, 3);
			List<Double> actionsB=rawOutput.subList(3,5);
			List<Double> actionsC=rawOutput.subList(5, 8);
			
			this.selectedActionA=this.argMax(actionsA);
			this.selectedActionB=this.argMax(actionsB);
			this.selectedActionC=this.argMax(actionsC);
			
		}
		//clear frames
		this.clearFrames();
		
		//log history
		if(this.lastState!=null) {
			this.exportData(networkInput,oldActionA,oldActionB,oldActionC,this.lastState,this.selectedActionA,this.selectedActionB,this.selectedActionC);
		}
		this.lastState=networkInput;
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
	//exports if it is generally unique
	private void exportData(List<Double> state,int actionA,int actionB,int actionC,List<Double> statePrime,int actionAPrime,int actionBPrime,int ActionCPrime) {
		//TODO
	}
}
