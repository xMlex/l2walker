package fw.gui.bot;

import java.awt.event.*;

import javax.swing.*;

import java.awt.GridLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;

public class botWindowConfig extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final botWindowConfig _instance;
	private JTabbedPane _tabbedPane;
	
	public botWindowConfig(){
		_instance = this;
		setAlwaysOnTop(true);
		setSize(800, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		_tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		getContentPane().add(_tabbedPane);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				_instance.setVisible(false);
			}
		});
		initGUI();
	}
	
	private void initGUI(){
		
		JComponent panel1 =new JPanel();
		_tabbedPane.addTab("Basic Option", null, panel1,"Используется для основных настроек");
		panel1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//tabbedPane.addTab("Other setting", null, null ,null);
		panel1.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Other", null, panel, null);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(505, Short.MAX_VALUE))
		);
		
		JLabel lblFishing = new JLabel("Fishing:");
		panel_1.add(lblFishing);
		
		JCheckBox chckbxActive = new JCheckBox("Active");
		panel_1.add(chckbxActive);
		panel.setLayout(gl_panel);
		 
		//_tabPane.setIconAt(tabCount, awtBotFrame.getIcon("user"))
		
	}
}
