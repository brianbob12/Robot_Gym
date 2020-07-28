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
		testingLevel.epsilon=0f;//this is very important this changes the frequency of random actions in the agents
		
		GameObject floor=new GameObject(100,30,200,10,false,false,testingLevel);
		floor.color=Color.black;
		floor.type=objectType.WALKABLE;
		testingLevel.addObject(floor);
		testingLevel.setEndGoal(150);
		
		
		
		//adding agents
		int numberOfAgents=1;
		List<Agent> agents= new ArrayList<Agent>();
		for(int i=0;i<numberOfAgents;i++) {
			Agent a = new Agent(120,45,testingLevel);
			a.name="A"+numberOfAgents;
			try {
				a.importNetwork("playData/agents/a0/net1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			a.color=Color.green;
			agents.add(a);
			testingLevel.addAgent(a);
		}
		
		
		testingLevel.setUpAgents();
		
		GameObject player=new Player(110,45,testingLevel,myKeyboard);
		try {
			player.setDisplayImage(ImageIO.read(new File("assets/images/PlayerPlaceholder.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testingLevel.addObject(player);//player should always be added after agents
		
		GameArea testingLevelView=new GameArea(testingLevel);
		testingLevelView.setBounds(0,0,800,800);
		testingLevelView.setLayout(null);
		testingLevelView.setVisible(true);
		testingLevelView.fixedViewCenter=player;
		testingLevelView.dynamicViewCenter=true;
		testingLevelView.viewCenterOffsetY=-30;
		testingLevelView.viewCenterOffsetX=-50;
		//testingLevelView.setBackgroundImage("assets/images/BackgroundBlue.png", 50);
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
		
		//export data
		//int i=0;
		//for(Agent agent: agents) {
		//	try {
		//		agent.exportData("playData/data/g"+i+".txt");
		//	} catch (IOException e) {
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		//	i+=1;
		//}
		//System.out.println("done");
	}

}
