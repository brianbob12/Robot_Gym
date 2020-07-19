package levelEditor;
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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import gameDynamics.GameObject;
import gameDynamics.Level;
import gameDynamics.LevelFileManager;
import tools.Keyboard;
import userInterface.GameArea;

import java.util.*;

/**
 * LevelEditor 
 * 
 * UI for creating and editing levels.
 *
 */
public class LevelEditor {
	
	static Level level;//the level being edited
	
	public static void main(String[] args) {
		
		//setup frame
		final JFrame frame= new JFrame("Robot Gym(0.1) Level Editor");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		Keyboard keyboard=new Keyboard();
		
		frame.addKeyListener(keyboard);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		level=newLevel();
		
		//add level view
		GameArea levelView=new GameArea(level);
		levelView.setBounds(0,0,600,600);
		levelView.setLayout(null);
		levelView.setVisible(true);
		levelView.setBackgroundImage("assets/images/levelEditorBackground.png",10);
		frame.add(levelView);
		levelView.addKeyListener(keyboard);
		
		//add selector
		Selector selector=new Selector();
		selector.setBounds(0,600,600,400);
		selector.setLayout(null);
		selector.setVisible(true);
		selector.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.add(selector);
		
		//add inspector
		Inspector inspector=new Inspector(frame);
		inspector.setBounds(600,0,400,600);
		inspector.setLayout(null);
		inspector.setVisible(true);
		inspector.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.add(inspector);
		
		//level interface
		LevelInterface li=new LevelInterface(selector,levelView,inspector);
		frame.addMouseListener(li);
		frame.addMouseMotionListener(li);
		frame.requestFocus();
		
		//load button
		JButton loadButton= new JButton();
		loadButton.setText("Import Level");
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser chooser = new JFileChooser("gameData");
				chooser.setDialogTitle("Chose File To Load");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Level Files Only","lvl");
				chooser.setFileFilter(filter);
	            int status = chooser.showOpenDialog(null);
	            if (status == JFileChooser.APPROVE_OPTION) {
	            	String path= chooser.getSelectedFile().getAbsolutePath();
	            	try {
						level=LevelFileManager.loadLevel(path);
						levelView.setLevel(level);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
			}
			
		});
		loadButton.setVisible(true);
		loadButton.setBounds(860,870,120,30);
		loadButton.setFocusable(false);//very important stops element from taking the focus away from keyboard
		frame.add(loadButton);
		//save button
		JButton saveButton= new JButton();
		saveButton.setText("Save Level");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser chooser = new JFileChooser("gameData");
				chooser.setDialogTitle("Specify a file to save");   
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Level Files Only","lvl");
				chooser.setFileFilter(filter);
				int userSelection = chooser.showSaveDialog(frame);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					try {
						LevelFileManager.saveLevel(chooser.getSelectedFile().getAbsolutePath()+".lvl",level);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			
		});
		saveButton.setVisible(true);
		saveButton.setBounds(860,910,120,30);
		saveButton.setFocusable(false);//very important stops element from taking the focus away from keyboard
		frame.add(saveButton);
		
		
		//looping
		int frameUpdate=20;//number of milliseconds between frame updates
		long lastFrameUpdate=20;
		
		while(true) {
			if(System.currentTimeMillis()%frameUpdate==0&&System.currentTimeMillis()!=lastFrameUpdate) {
				lastFrameUpdate=System.currentTimeMillis();
				mainLoop(levelView,frame,keyboard);
			}
		}
	}
	
	//creates a new level and returns it
	public static Level newLevel() {
		Level out=new Level();
		out.setEndGoal(300);
		out.setStartPosition(0);
		
		//add floor by default
		GameObject floor=new GameObject(0,0,300,10,false,false,out);
		floor.color=Color.black;
		out.addObject(floor);
		
		return out;
	}
	
	
	public static void mainLoop(GameArea levelView,JFrame frame,Keyboard keyboard) {
		//camera scroll speed thorugh the level
		float scrollSpeed=0.1F;
		
		if(keyboard.space) {
			scrollSpeed=0.5F;
		}
		if(keyboard.w) {
			levelView.viewCenterY+=scrollSpeed;
		}
		if(keyboard.s) {
			levelView.viewCenterY-=scrollSpeed;
		}
		if(keyboard.a) {
			levelView.viewCenterX-=scrollSpeed;
		}
		if(keyboard.d) {
			levelView.viewCenterX+=scrollSpeed;
		}
		if(levelView.viewCenterX<0) {
			levelView.viewCenterX=0;
		}
		if(levelView.viewCenterY<0) {
			levelView.viewCenterY=0;
		}
		//paint
		levelView.repaint();
		frame.repaint();
		
	}
	

}
