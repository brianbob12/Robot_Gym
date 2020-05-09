/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Testing 
 * 
 * This class runs the game. This includes running levels both visibly and invisibly. 
 *
 */

public class Testing {
	
	
	public static void main(String[] args) {
		
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		
		Level testingLevel=new Level();
		GameObject bloc1=new GameObject(10,100,10,10,true,true,testingLevel);
		bloc1.color=Color.red;
		GameObject bloc2=new GameObject(50,100,10,10,true,true,testingLevel);
		bloc2.color=Color.blue;
		bloc2.vx=(float)-0.5;
		GameObject bloc3=new GameObject(32,100,10,10,true,true,testingLevel);
		bloc3.color=Color.magenta;
		bloc3.vy=(float)1.0;
		GameObject floor=new GameObject(0,30,90,10,false,false,testingLevel);
		floor.color=Color.black;
		testingLevel.addObject(bloc1);
		testingLevel.addObject(bloc2);
		testingLevel.addObject(bloc3);
		testingLevel.addObject(floor);
		
		GameArea testingLevelView=new GameArea(testingLevel);
		testingLevelView.setBounds(0,0,800,800);
		testingLevelView.setLayout(null);
		testingLevelView.setVisible(true);
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
