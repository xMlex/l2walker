package fw.gui.bot;

import java.awt.event.*;

import javax.swing.*;

import java.awt.GridLayout;

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
		_tabbedPane.addTab("[no char]", null, panel1,"");
		//_tabPane.setIconAt(tabCount, awtBotFrame.getIcon("user"))
		
	}
}
