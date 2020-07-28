package userInterface;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import gameDynamics.Level;
import gameDynamics.LevelFileManager;
import testing.TestingTraining;


/**
 * Main Menu
 * 
 * This class is responsible for displaying a main menu and managing user navigation. This will also manage the level import
 *
 */
public class MainMenu {
	
	private static boolean playing=false;
	private int levelNum=1;
	
	public static void main(String[] args) {
		final JFrame frame= new JFrame("Robot Gym(0.1)");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton playButton= new JButton("Play");
		playButton.setBounds(300,300,400,50);
		playButton.setLayout(null);
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				playing=true;
			}
		});
		frame.add(playButton);
		
		
		
	}
}

