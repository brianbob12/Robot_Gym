package levelEditor;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Selector
 * 
 * This is used in the level editor to add things
 *
 */

public class Selector extends JPanel implements ActionListener{
	
	public enum AddSelected{
		WALKABLE,
		ENEMY,
		DEADLY
	}
	
	AddSelected sel=AddSelected.WALKABLE;
	
	public Selector(){
		String[] addOptions= {"walkable object","enemy","deadly object"};
		JComboBox addBox= new JComboBox(addOptions);
		addBox.setSelectedIndex(0);
		addBox.addActionListener(this);
		addBox.setLayout(null);
		addBox.setVisible(true);
		addBox.setBounds(50,50,200,40);
		this.add(addBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox addBox=(JComboBox)e.getSource();
		int s=addBox.getSelectedIndex();
		if(s==0) {
			this.sel=AddSelected.WALKABLE;
		}
		else if (s==1) {
			this.sel=AddSelected.ENEMY;
		}
		else if(s==2) {
			this.sel=AddSelected.DEADLY;
		}
	}

}
