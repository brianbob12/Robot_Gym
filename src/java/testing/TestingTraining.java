package testing;

/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import gameDynamics.Agent;
import gameDynamics.GameObject;
import gameDynamics.Level;
import gameDynamics.Player;
import gameDynamics.GameObject.objectType;
import tools.Keyboard;
import tools.PythonManager;
import userInterface.GameArea;

/**
 * Testing training
 * 
 * This class runs the game in real time and then trains the agent
 *
 */



public class TestingTraining {

	public static void main(String[] args) {
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		Keyboard myKeyboard=new Keyboard();
		frame.addKeyListener(myKeyboard);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//manual setup level
		
		Level testingLevel=new Level();
		testingLevel.trainingLevel=true;
		testingLevel.epsilon=0.45f;//this is very important this changes the frequency of random actions in the agents
		
		GameObject floor=new GameObject(100,30,50,10,false,false,testingLevel);
		floor.color=Color.black;
		floor.type=objectType.WALKABLE;
		testingLevel.addObject(floor);
		
		GameObject plat1=new GameObject(170,30,50,10,false,false,testingLevel);
		plat1.color=Color.black;
		plat1.type=objectType.WALKABLE;
		testingLevel.addObject(plat1);
		
		GameObject plat2=new GameObject(240,30,20,10,false,false,testingLevel);
		plat2.color=Color.black;
		plat2.type=objectType.WALKABLE;
		testingLevel.addObject(plat2);
		
		GameObject plat3=new GameObject(280,30,50,10,false,false,testingLevel);
		plat3.color=Color.black;
		plat3.type=objectType.WALKABLE;
		testingLevel.addObject(plat3);
		
		testingLevel.setEndGoal(310);
		
		
		
		//adding agents
		int numberOfAgents=10;
		List<Agent> agents= new ArrayList<Agent>();
		//first agent
		Agent a = new Agent(120,45,testingLevel);

		try {
			a.importNetwork("playData/agents/a0A/net1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a.color=Color.green;
		agents.add(a);
		testingLevel.addAgent(a);
		
		for(int i=1;i<numberOfAgents;i++) {
			//subsequent agents
			Agent b=a.middleCopy();
			agents.add(b);
			testingLevel.addAgent(b);
		}
		
		
		testingLevel.setUpAgents();
		
		GameObject player=new Player(110,45,testingLevel,myKeyboard);
		testingLevel.addObject(player);//player should always be added after agents
		
		GameArea testingLevelView=new GameArea(testingLevel);
		testingLevelView.setBounds(0,0,800,800);
		testingLevelView.setLayout(null);
		testingLevelView.setVisible(true);
		testingLevelView.fixedViewCenter=player;
		testingLevelView.dynamicViewCenter=true;
		testingLevelView.viewCenterOffsetY=-30;
		testingLevelView.viewCenterOffsetX=-50;
		testingLevelView.setBackgroundImage("assets/images/wereBackground (2).png", 10);
		frame.add(testingLevelView);
		
		int trainingFrames=1200;//the ammount of frames for training
		
		//running the game
		int physicsUpdate=20;//number of milliseconds between physics Updates
		long lastPhysicsUpdate=0;
		int frameUpdate=20;//number of milliseconds between frame updates
		long lastFrameUpdate=20;
		
		int frameNum=0;
		
		while(frameNum<trainingFrames) {
			if(System.currentTimeMillis()%physicsUpdate==0&&System.currentTimeMillis()!=lastPhysicsUpdate) {
				lastPhysicsUpdate=System.currentTimeMillis();
				testingLevel.step();
				frameNum+=1;
			}
			if(System.currentTimeMillis()%frameUpdate==0&&System.currentTimeMillis()!=lastFrameUpdate) {
				lastFrameUpdate=System.currentTimeMillis();
				testingLevelView.repaint();
				frame.repaint();
			}
		}
		System.out.println("Started Saving");
		//export data
		int i=0;
		for(Agent agent: agents) {
			try {
				agent.exportData("playData/data/g"+i+".txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i+=1;
		}
		System.out.println("Begining Training");
		
		try {
			Process process=PythonManager.trainAgent("playData/agents/a0B", "playData/agents/a1B","playData/data", 1);
			while(!PythonManager.finishedSuccessfully(process)) {
				//waiting for process to finish
				int x=process.getInputStream().read();
				if(x==-1) {//end of input stream
					continue;
				}
				else {
					System.out.print((char) x);
					if(x==0) {
						System.out.print("\t");
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished Training");
	}

}
