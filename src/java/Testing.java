/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

import gameDynamics.Agent;
import gameDynamics.GameObject;
import gameDynamics.Level;
import gameDynamics.Player;
import tools.Keyboard;
import userInterface.GameArea;

import java.util.*;

/**
 * Testing 
 * 
 * This class runs the game. This includes running levels both visibly and invisibly. 
 * Just for testing and messing around.
 *
 */

public class Testing {
	
	
	public static void main(String[] args) {
		
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		Keyboard myKeyboard=new Keyboard();
		frame.addKeyListener(myKeyboard);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//manual setup level
		/*
		Level testingLevel=new Level();
		
		GameObject bloc2=new GameObject(150,100,10,10,true,true,testingLevel);
		bloc2.color=Color.blue;
		bloc2.vx=(float)-0.5;
		GameObject bloc3=new GameObject(132,100,10,10,true,true,testingLevel);
		bloc3.color=Color.magenta;
		bloc3.vy=(float)1.0;
		GameObject bloc4=new GameObject(200,50,10,30,true,true,testingLevel);
		bloc4.color=Color.cyan;
		GameObject floor=new GameObject(100,30,200,10,false,false,testingLevel);
		floor.color=Color.black;
		testingLevel.addObject(bloc2);
		testingLevel.addObject(bloc3);
		testingLevel.addObject(bloc4);
		testingLevel.addObject(floor);
		testingLevel.endGoal=300;
		testingLevel.startPosition=0;
		*/
		
		
		
		//import level area
		
		Level testingLevel = null;
		try {
			FileInputStream fis = new FileInputStream("playData/testtinglevel.lvl");
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    testingLevel=(Level)ois.readObject();
		    ois.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		testingLevel.postImportSetup();
		
		
		
		//writing level
		/*
		try {
			FileOutputStream fos= new FileOutputStream("playData/testtinglevel.lvl");
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(testingLevel);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		//adding agents
		Agent agent=new Agent(120,45,testingLevel);
		agent.name="A1";
		agent.importNetwork("playData/net1");
		agent.color=Color.green;
		
		
		testingLevel.addAgent(agent);
		
		
		testingLevel.setUpAgents();
		
		GameObject player=new Player(110,45,testingLevel,myKeyboard);
		player.color=Color.red;
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
		
		
		
		//running the game
		int physicsUpdate=20;//number of milliseconds between physics Updates
		long lastPhysicsUpdate=0;
		int frameUpdate=20;//number of milliseconds between frame updates
		long lastFrameUpdate=20;
		while(true) {
			if(System.currentTimeMillis()%physicsUpdate==0&&System.currentTimeMillis()!=lastPhysicsUpdate) {
				lastPhysicsUpdate=System.currentTimeMillis();
				testingLevel.step();
			}
			if(System.currentTimeMillis()%frameUpdate==0&&System.currentTimeMillis()!=lastFrameUpdate) {
				lastFrameUpdate=System.currentTimeMillis();
				testingLevelView.repaint();
				frame.repaint();
			}
		}
		
	}
	
}
