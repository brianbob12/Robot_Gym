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
	List<Matrix> weights;
	List<Matrix> biases;
	FileManager myFileManager;
	int stateSpace=1;//better to import this from a config file
	int actionSpace=1;
	List<Integer> nHidden;
	List<String> activation;//stores activation functions for each layer
	
	//observation stuff
	Grid myGrid;
	
	//current action variables
	int selectedActionA=0;
	int selectedActionB=0;
	int selectedActionC=0;
	
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
			this.weights.add(new Matrix(this.nHidden.get(i-1),this.nHidden.get(i)));
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
	
	public List<Double> evaluateNetwork(List<Double> inp) {
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
			}
		}
		
		//return last layer
		return layerVals.get(this.nHidden.size()+1).getRow(0);
	}
	
	//grid stuff
	
	public void observe() {
		
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

}
