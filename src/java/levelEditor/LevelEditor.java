package levelEditor;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.*;
import javax.swing.*;

import gameDynamics.GameObject;
import gameDynamics.Level;
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

	public static void main(String[] args) {
		
		//setup frame
		final JFrame frame= new JFrame("Robot Gym(0.1) Level Editor");
		frame.setSize(1000,1000);
		frame.setLayout(null);
		frame.setVisible(true);
		Keyboard keyboard=new Keyboard();
		
		frame.addKeyListener(keyboard);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Level level=newLevel();
		
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
		out.endGoal=300;
		out.startPosition=0;
		
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
