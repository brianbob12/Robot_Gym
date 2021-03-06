package levelEditor;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.awt.Color;
import java.awt.Font;

/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.event.*;
import gameDynamics.GameObject;
import gameDynamics.GameObject.objectType;
import gameDynamics.Destructible;

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
	public JCheckBox harmableBox;
	public JTextArea healthInp;
	private JLabel healthLabel;
	private JLabel typeLabel;
	
	
	public Inspector(JFrame frame) {
		this.frame=frame;
		
		//type input
		this.typeLabel=new JLabel();
		this.typeLabel.setLayout(null);
		this.typeLabel.setVisible(false);
		this.typeLabel.setBounds(50,15,200,25);
		this.typeLabel.setFont(new Font("Tahoma",Font.BOLD,18));
		this.add(this.typeLabel);
		
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
		
		//health input
		this.healthInp= new JTextArea();
		this.healthInp.setLayout(null);
		this.healthInp.setVisible(false);
		this.healthInp.setBounds(50,160,200,20);
		this.add(this.healthInp);
		this.healthInp.addKeyListener(new Handler(this));
		
		this.healthLabel= new JLabel("MaxHealth");
		this.healthLabel.setVisible(false);
		this.healthLabel.setBounds(50,130,200,20);
		this.add(this.healthLabel);
		
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
		this.harmableBox=new JCheckBox("Harmable");
		this.harmableBox.setLayout(null);
		this.harmableBox.setVisible(false);
		this.harmableBox.setBounds(10,190,100,20);
		this.add(harmableBox);
		this.harmableBox.addItemListener(new ItemHandler(this));
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
			this.typeLabel.setText("Enemy");
			this.gravityBox.setVisible(false);
			this.moveableBox.setVisible(false);
			this.collidableBox.setVisible(false);
			this.harmableBox.setVisible(true);
			this.harmableBox.setSelected(((Destructible)this.selected).harmable);
			this.healthInp.setText(Float.toString(((Destructible)this.selected).getMaxHealth()));
			this.healthInp.setVisible(true);
			this.healthLabel.setVisible(true);
		}
		else if(this.selected.type==objectType.DEADLY) {
			this.typeLabel.setText("Deadly");
			this.gravityBox.setVisible(true);
			this.moveableBox.setVisible(true);
			this.collidableBox.setVisible(true);
			this.harmableBox.setVisible(true);
			this.harmableBox.setSelected(((Destructible)this.selected).harmable);
			this.gravityBox.setSelected(this.selected.gravity);
			this.moveableBox.setSelected(this.selected.moveable);
			this.collidableBox.setSelected(this.selected.collidable);
			this.healthInp.setText(Float.toString(((Destructible)this.selected).getMaxHealth()));
			this.healthInp.setVisible(true);
			this.healthLabel.setVisible(true);
		}
		else {//for walkable
			this.typeLabel.setText("Walkable");
			//set boolean values
			this.gravityBox.setVisible(true);
			this.moveableBox.setVisible(true);
			this.collidableBox.setVisible(true);
			this.gravityBox.setSelected(this.selected.gravity);
			this.moveableBox.setSelected(this.selected.moveable);
			this.collidableBox.setSelected(this.selected.collidable);
			this.harmableBox.setVisible(false);
			this.healthInp.setVisible(false);
			this.healthLabel.setVisible(false);
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
		this.harmableBox.setVisible(false);
		this.healthInp.setVisible(false);
		this.healthLabel.setVisible(false);
		this.typeLabel.setVisible(false);
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
		this.harmableBox.setVisible(true);
		this.healthInp.setVisible(true);
		this.healthLabel.setVisible(true);
		this.typeLabel.setVisible(true);
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
			else if(source==this.parent.healthInp) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					this.parent.frame.requestFocus();
					//may fail
					try {
						((Destructible)this.parent.getSelected()).currentHealth=Float.parseFloat(source.getText());
						((Destructible)this.parent.getSelected()).setMaxHealth(Float.parseFloat(source.getText()));
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
			else if(source==this.parent.harmableBox) {
				((Destructible) this.parent.selected).harmable=source.isSelected();
			}
			//this refreshes values
			this.parent.setSelected(this.parent.getSelected());
		}
	}
}
