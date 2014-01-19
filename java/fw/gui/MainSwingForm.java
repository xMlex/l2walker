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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MainSwingForm extends Frame {

	private static final long serialVersionUID = 1879948070981652392L;	
	private static MainSwingForm _instance;
	
	private JTabbedPane _tabPane = new JTabbedPane();
	private int tabCount = 0;
	
	HashMap<String, ServerConfig> mapServersConfig = new HashMap<String, ServerConfig>();
		
	public MainSwingForm() throws LWJGLException {
		
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		setTitle("L2Walker");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		loadServerList();
		initGUI();		
	}
	
	private void initGUI()
	{
		
		MenuBar menuBar = new MenuBar();		
		Menu menuFile = new Menu("File");
		menuFile.add(new MenuItem("New connect"));
		menuBar.add(menuFile);			
		
		this.setMenuBar(menuBar);
		this.add(_tabPane);
		
		for (int i = 0; i < 2; i++) 
			makeTextPanel();
		
	}
	
	 protected void makeTextPanel() {
		 try{
		 	JComponent panel1 =new awtBotFrame(mapServersConfig,_tabPane,tabCount);
			_tabPane.addTab("[no char]", null, panel1,"");
			_tabPane.setIconAt(tabCount, awtBotFrame.getIcon("user"));			
			tabCount++;
		 }catch(Exception e){}
		 
	    }
	
	public static void main(String[] args) throws Exception {
		
		/*try{
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookAndFeel);
		}catch(Exception e){
			System.out.println("Not init system theme theme");
		}*/
		
		ConfigSystem.load();	
		
		_instance = new MainSwingForm();
		_instance.setSize(1024, 768);
		_instance.setVisible(true);		
		_instance.setLocationRelativeTo(null);
		
		System.out.println("LWGL Version: "+Sys.getVersion());
	}

	
	public void loadServerList()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("data/server_list.cfg"));

			String line = null;
			String arr[] = null;

			while ((line = in.readLine()) != null)
			{// while
				line = line.trim();
				if (line.equalsIgnoreCase("#BEGIN"))
				{// if
					ServerConfig serverConfig = new ServerConfig();
					while ((line = in.readLine()) != null)
					{// while
						line = line.trim();
						if (line.equalsIgnoreCase("#END"))
						{
							mapServersConfig.put(serverConfig.name, serverConfig);
							break;
						}

						arr = line.split("=");

						if (arr[0].trim().equalsIgnoreCase("NAME"))
						{
							serverConfig.name = arr[1].trim();
						} else if (arr[0].trim().equalsIgnoreCase("ADDR"))
						{
							String addr[] = arr[1].split(":");
							serverConfig.hostLogin = addr[0].trim();
							serverConfig.port = Integer.parseInt(addr[1].trim());
						} else if (arr[0].trim().equalsIgnoreCase("HOSTS"))
						{
							String servers[] = arr[1].replaceAll(" ", "").split(",");
							serverConfig.gameServers = servers;
						} else if (arr[0].trim().equalsIgnoreCase("PROTOCOL"))
						{
							serverConfig.protocol = Integer.parseInt(arr[1].trim());
						} else if (arr[0].trim().equalsIgnoreCase("TOKEN"))
						{
							String tokenHex = arr[1].trim();
							StringBuilder token = new StringBuilder();
							for (int i = 0; i < tokenHex.length(); i = i + 2)
							{
								String hex = "" + tokenHex.charAt(i) + tokenHex.charAt(i + 1);
								token.append((char) Integer.parseInt(hex, 16));
							}
							serverConfig.token = token.toString();
						}

					}// while
				}// if
			}// while

			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
