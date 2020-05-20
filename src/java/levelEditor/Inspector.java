package levelEditor;

/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gameDynamics.GameObject;
import javax.swing.*;

/**
 * Inspector
 * 
 * This is used in the level editor for editing GameObjects
 *
 */


public class Inspector extends JPanel {
	
	private GameObject selected;
	private JFrame frame;
	
	public Inspector(JFrame frame) {
		this.frame=frame;
		JTextArea widthInp= new JTextArea();
		widthInp.setLayout(null);
		widthInp.setVisible(true);
		widthInp.setBounds(50,50,200,40);
		//widthInp.setFocusable(false);
		this.add(widthInp);
	}
	public GameObject getSelected(){
		return this.selected;
	}
	public void setSelected(GameObject x) {
		this.selected=x;
	}
}
