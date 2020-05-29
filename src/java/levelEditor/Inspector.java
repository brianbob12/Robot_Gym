package levelEditor;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.awt.Color;

/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.event.*;
import gameDynamics.GameObject;
import gameDynamics.GameObject.objectType;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Inspector
 * 
 * This is used in the level editor for editing GameObjects
 *
 */


public class Inspector extends JPanel {
	
	private GameObject selected;
	private JFrame frame;
	public JTextArea widthInp;
	private JLabel widthLabel;
	public JTextArea heightInp;
	private JLabel heightLabel;
	public JCheckBox gravityBox;
	public JCheckBox moveableBox;
	public JCheckBox collidableBox;
	
	public Inspector(JFrame frame) {
		this.frame=frame;
		
		//width input
		this.widthInp= new JTextArea();
		this.widthInp.setLayout(null);
		this.widthInp.setVisible(false);
		this.widthInp.setBounds(50,50,200,20);
		this.add(this.widthInp);
		this.widthInp.addKeyListener(new Handler(this));
		
		this.widthLabel= new JLabel("Width");
		this.widthLabel.setVisible(false);
		this.widthLabel.setBounds(50,30,200,20);
		this.add(this.widthLabel);
		
		//height input
		this.heightInp= new JTextArea();
		this.heightInp.setLayout(null);
		this.heightInp.setVisible(false);
		this.heightInp.setBounds(50,90,200,20);
		this.add(this.heightInp);
		this.heightInp.addKeyListener(new Handler(this));
		
		this.heightLabel= new JLabel("Height");
		this.heightLabel.setVisible(false);
		this.heightLabel.setBounds(50,70,200,20);
		this.add(this.heightLabel);
		//boxes
		this.gravityBox=new JCheckBox("Gravity");
		this.gravityBox.setLayout(null);
		this.gravityBox.setVisible(false);
		this.gravityBox.setBounds(10,110,100,20);
		this.add(gravityBox);
		this.gravityBox.addItemListener(new ItemHandler(this));
		this.moveableBox=new JCheckBox("Moveable");
		this.moveableBox.setLayout(null);
		this.moveableBox.setVisible(false);
		this.moveableBox.setBounds(110,110,100,20);
		this.add(moveableBox);
		this.moveableBox.addItemListener(new ItemHandler(this));
		this.collidableBox=new JCheckBox("Collidable");
		this.collidableBox.setLayout(null);
		this.collidableBox.setVisible(false);
		this.collidableBox.setBounds(210,110,100,20);
		this.add(collidableBox);
		this.collidableBox.addItemListener(new ItemHandler(this));
	}

	public GameObject getSelected(){
		return this.selected;
	}
	public void setSelected(GameObject x) {
		this.selected=x;
		this.widthInp.setText(Float.toString(this.selected.width));
		this.heightInp.setText(Float.toString(this.selected.height));
		this.showAll();
		if(this.selected.type==objectType.ENEMY) {
			this.gravityBox.setVisible(false);
			this.moveableBox.setVisible(false);
			this.collidableBox.setVisible(false);
		}
		else {
			//set boolean values
			this.gravityBox.setSelected(this.selected.gravity);
			this.moveableBox.setSelected(this.selected.moveable);
			this.collidableBox.setSelected(this.selected.collidable);
		}
	}
	//clears the panel by making components inviable
	public void clearAll() {
		this.widthInp.setVisible(false);
		this.widthLabel.setVisible(false);
		this.heightInp.setVisible(false);
		this.heightLabel.setVisible(false);
		this.gravityBox.setVisible(false);
		this.moveableBox.setVisible(false);
		this.collidableBox.setVisible(false);
	}
	//shows all cleared components
	public void showAll() {
		this.widthInp.setVisible(true);
		this.widthLabel.setVisible(true);
		this.heightInp.setVisible(true);
		this.heightLabel.setVisible(true);
		this.gravityBox.setVisible(true);
		this.moveableBox.setVisible(true);
		this.collidableBox.setVisible(true);
	}

	
	class Handler implements KeyListener{
		
		Inspector parent;
		public Handler(Inspector parent) {
			this.parent=parent;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			JTextArea source= (JTextArea) e.getSource();
			if(source==this.parent.widthInp) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					this.parent.frame.requestFocus();
					//may fail
					try {
						this.parent.getSelected().width=Float.parseFloat(source.getText());
						source.setForeground(Color.BLACK);
						
					}
					catch(NumberFormatException ex) {
						source.setForeground(Color.RED);
					}
					catch(Exception ex) {
						System.out.println(ex);
					}
					//this refreshes values
					this.parent.setSelected(this.parent.getSelected());
				}
			}
			else if(source==this.parent.heightInp) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					this.parent.frame.requestFocus();
					//may fail
					try {
						this.parent.getSelected().height=Float.parseFloat(source.getText());
						source.setForeground(Color.BLACK);
						
					}
					catch(NumberFormatException ex) {
						source.setForeground(Color.RED);
					}
					catch(Exception ex) {
						System.out.println(ex);
					}
					//this refreshes values
					this.parent.setSelected(this.parent.getSelected());
				}
			}
		}


		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	class ItemHandler implements ItemListener{
		
		Inspector parent;
		public ItemHandler(Inspector parent) {
			this.parent=parent;
		}
		@Override
		public void itemStateChanged(ItemEvent e) {
			JCheckBox source= (JCheckBox) e.getSource();
			if(source==this.parent.gravityBox) {
				this.parent.selected.gravity=source.isSelected();
			}
			else if(source==this.parent.moveableBox) {
				this.parent.selected.moveable=source.isSelected();
			}
			else if(source==this.parent.collidableBox) {
				this.parent.selected.collidable=source.isSelected();
			}
			//this refreshes values
			this.parent.setSelected(this.parent.getSelected());
		}
	}
}
