/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.*;

/**
 * Testing 
 * 
 * This class runs the game. This includes running levels both visibly and invisibly. 
 *
 */

public class Testing {
	
	
	public static void main(String[] args) {
		
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(500,500);
		frame.setLayout(null);
		frame.setVisible(true);
		
		Level testingLevel=new Level();
		GameObject bloc1=new GameObject(10,100,10,10,true,true);
		bloc1.color=Color.red;
		GameObject bloc2=new GameObject(50,100,10,10,true,true);
		bloc2.color=Color.blue;
		bloc2.vx=(float)-0.5;
		GameObject bloc3=new GameObject(70,100,10,10,true,true);
		bloc3.color=Color.magenta;
		bloc3.vx=(float)0.25;
		GameObject floor=new GameObject(0,5,90,10,false,false);
		floor.color=Color.black;
		testingLevel.addObject(bloc1);
		testingLevel.addObject(bloc2);
		testingLevel.addObject(bloc3);
		testingLevel.addObject(floor);
		
		GameArea testingLevelView=new GameArea(testingLevel);
		testingLevelView.setBounds(0,0,400,400);
		testingLevelView.setLayout(null);
		testingLevelView.setVisible(true);
		frame.add(testingLevelView);
		
		int updateCounter=0;
		
		while(true) {
			testingLevel.step();
			testingLevelView.repaint();
			frame.repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateCounter+=1;
		}
		
	}
	
}
