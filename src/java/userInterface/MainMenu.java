package userInterface;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import gameDynamics.Level;


/**
 * Main Menu
 * 
 * This class is responsible for displaying a main menu and managing user navigation.
 *
 */
public class MainMenu {
	public static void main(String[] args) {
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton playButton= new JButton("Play");
		playButton.setBounds(300,300,400,50);
		playButton.setLayout(null);
		frame.add(playButton);
		
		
		
	}
}

