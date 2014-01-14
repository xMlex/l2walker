package fw.gui.bot;

import javax.swing.JPanel;

import java.awt.*;


import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class awtBotFrame extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public awtBotFrame() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panelAuth = new JPanel();
		panelAuth.setBackground(Color.ORANGE);
		panelAuth.setToolTipText("Text");
		panelAuth.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		panelAuth.add(new JButton("start 2")); 
		panelAuth.add(new JButton("start 2")); 
		panelAuth.add(new JButton("start 3")); 
		panelAuth.add(new JButton("start 4")); 
		panelAuth.add(new JButton("start 5")); 
		panelAuth.add(new JButton("start 6")); 
		panelAuth.add(new JButton("Okay"));
		add(panelAuth);
		
		JPanel panel = new JPanel();
		add(panel);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(50);
		flowLayout.setHgap(50);
		panel.setBackground(Color.BLUE);
	}
}
