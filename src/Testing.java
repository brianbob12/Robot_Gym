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
		show();
	}
	private static void show() {
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(500,500);
		frame.setLayout(null);
		frame.setVisible(true);
		
		Level testingLevel=new Level();
		GameArea testingLevelView=new GameArea(testingLevel);
		testingLevelView.setBounds(0,0,100,100);
		testingLevelView.setLayout(null);
		testingLevelView.setVisible(true);
	}

}
