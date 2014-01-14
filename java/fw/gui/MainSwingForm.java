package fw.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;

import fw.gui.bot.awtBotFrame;
import fw.test.AWTGLRender;
import xmlex.config.ConfigSystem;

import java.awt.Window.Type;

public class MainSwingForm extends Frame {

	private static final long serialVersionUID = 1879948070981652392L;	
	private static MainSwingForm _instance;
	
	private JTabbedPane _tabPane = new JTabbedPane();
		
	public MainSwingForm() throws LWJGLException {
		setAlwaysOnTop(true);
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		setTitle("L2Walker");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		initGUI();		
	}
	
	private void initGUI()
	{
		
		MenuBar menuBar = new MenuBar();		
		Menu menuFile = new Menu();
		menuBar.add(menuFile);
		
		
		JComponent panel1 = makeTextPanel("Panel #1");
		_tabPane.addTab("Tab 1", null, panel1,"Does nothing");
		//_tabPane.setMnemonicAt(1, KeyEvent.VK_1);
		
		JComponent panel2 = makeTextPanel("Panel #2");
		_tabPane.addTab("Tab 2", null, panel2,"Does nothing");
		//_tabPane.setMnemonicAt(1, KeyEvent.VK_1);
		
		this.setMenuBar(menuBar);
		this.add(_tabPane);
		
	}
	
	 protected JComponent makeTextPanel(String text) {
	        /*JPanel panel = new JPanel(false);	        
	        panel.setLayout(new GridLayout(1, 1));
	        panel.add();	*/		
	        return new awtBotFrame();
	    }
	
	public static void main(String[] args) throws Exception {
		
		try{
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookAndFeel);
		}catch(Exception e){
			System.out.println("Not init system theme theme");
		}
		
		ConfigSystem.load();	
		
		_instance = new MainSwingForm();
		_instance.setSize(850, 600);
		_instance.setVisible(true);		
		
		System.out.println("LWGL Version: "+Sys.getVersion());
	}

	
	
}
