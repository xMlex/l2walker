package fw.gui;

import fw.gui.mainArea.UserMainArea;

import java.io.BufferedReader;
import java.io.FileReader;

import javolution.util.FastMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import xmlex.config.ConfigSystem;
import fw.com.swtdesigner.SWTResourceManager;
import fw.common.ThreadPoolManager;
import fw.dbClasses.DbObjects;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MainAppForm extends org.eclipse.swt.widgets.Composite
{
	private static MainAppForm instance = null;
	private Menu menu1;
	private CTabFolder tabMain;
	private MenuItem exitMenuItem;
	private Composite cpMain;
	private MenuItem menuNewTab;
	private Menu menu3;
	private MenuItem menuPacketSniffer;
	private MenuItem toolsMenuItem;
	private Menu menu2;
	private MenuItem menuActions;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	DbObjects dbObjects;
	FastMap<String, ServerConfig> mapServersConfig = new FastMap<String, ServerConfig>();
	Tray tray = getDisplay().getSystemTray();
	Font tabFont = SWTResourceManager.getFont("Tahoma", 8, 1, false, false);

	public MainAppForm(Composite parent, int style)
	{
		super(parent, style);
		ThreadPoolManager.getInstance();
		initGUI();
		SetImage();
		dbObjects = new DbObjects();
		loadServerList();

		// --//
		newTab();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI()
	{
		try
		{
			this.setSize(850, 600);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FillLayout thisLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			{
				cpMain = new Composite(this, SWT.NONE);
				GridLayout cpMainLayout = new GridLayout();
				cpMainLayout.marginHeight = 0;
				cpMainLayout.marginWidth = 0;
				cpMainLayout.verticalSpacing = 0;
				cpMainLayout.horizontalSpacing = 0;
				cpMainLayout.makeColumnsEqualWidth = true;
				cpMain.setLayout(cpMainLayout);
				{
					tabMain = new CTabFolder(cpMain, SWT.NONE);
					tabMain.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
					GridData tabMainLData = new GridData();
					tabMainLData.horizontalSpan = 0;
					tabMainLData.horizontalAlignment = GridData.FILL;
					tabMainLData.verticalAlignment = GridData.FILL;
					tabMainLData.verticalSpan = 0;
					tabMainLData.grabExcessVerticalSpace = true;
					tabMainLData.grabExcessHorizontalSpace = true;
					tabMain.setLayoutData(tabMainLData);
					tabMain.setSelection(0);
				}
			}
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
							exitMenuItem.addSelectionListener(new SelectionAdapter()
							{
								@Override
								public void widgetSelected(SelectionEvent evt)
								{
									exitMenuItemWidgetSelected(evt);
								}
							});
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					menuActions = new MenuItem(menu1, SWT.CASCADE);
					menuActions.setText("Actions");
					{
						menu2 = new Menu(menuActions);
						menuActions.setMenu(menu2);
						{
							menuNewTab = new MenuItem(menu2, SWT.PUSH);
							menuNewTab.setText("New Tab");
							menuNewTab.setImage(SWTResourceManager.getImage("res_images/new_wiz.png"));
							menuNewTab.addSelectionListener(new SelectionAdapter()
							{
								@Override
								public void widgetSelected(SelectionEvent evt)
								{
									menuNewTabWidgetSelected(evt);
								}
							});
						}
					}
				}
				{
					toolsMenuItem = new MenuItem(menu1, SWT.CASCADE);
					toolsMenuItem.setText("Tools");
					{
						menu3 = new Menu(toolsMenuItem);
						toolsMenuItem.setMenu(menu3);
						menu3.setEnabled(true);
						{
							menuPacketSniffer = new MenuItem(menu3, SWT.PUSH);
							menuPacketSniffer.setText("Packet Sniffer");
						}
					}
				}
			}
			this.layout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Composite inside a new
	 * Shell.
	 */
	public static void main(String[] args)
	{
		ConfigSystem.load();
		Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setText("FoxWalker");
		shell.setMaximized(false);
		shell.setImage(SWTResourceManager.getImage("res_images/fox.jpg"));

		shell.addShellListener(new ShellListener()
		{

			public void shellActivated(ShellEvent arg0)
			{
				// TODO Auto-generated method stub
			}

			public void shellClosed(ShellEvent arg0)
			{
				arg0.doit = instance.exitWalker();
			}

			public void shellDeactivated(ShellEvent arg0)
			{
				// TODO Auto-generated method stub
			}

			public void shellDeiconified(ShellEvent arg0)
			{
				// TODO Auto-generated method stub
			}

			public void shellIconified(ShellEvent arg0)
			{
//				shell.setVisible(false);
//				//instance.setVisible(false);
//				arg0.doit = true;
			}

		});

		instance = new MainAppForm(shell, SWT.NULL);
		Point size = instance.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if (size.x == 0 && size.y == 0)
		{
			instance.pack();
			shell.pack();
		} else
		{
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private void menuNewTabWidgetSelected(SelectionEvent evt)
	{
		newTab();
	}

	private void newTab()
	{
		CTabItem tbi = new CTabItem(tabMain, SWT.CLOSE);
		tbi.setFont(tabFont);
		tbi.setText("[no user]");

		UserMainArea mainArea = new UserMainArea(tabMain, SWT.NONE);
		mainArea.setDbObjects(dbObjects);
		mainArea.setMapServersConfig(mapServersConfig);
		mainArea.setParentTabItem(tbi);
		tbi.setControl(mainArea);
		tabMain.setSelection(tbi);
	}

	private boolean exitWalker()
	{
		MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
		messageBox.setText("Exit");
		messageBox.setMessage("Are you sure?");
			if (messageBox.open() == SWT.OK)
			{
				tray.dispose();
				dbObjects.close();
				System.exit(0);
				return true;
			}
		return false;
	}

	private void exitMenuItemWidgetSelected(SelectionEvent evt)
	{
		exitWalker();
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

	public void SetImage(){


		if (tray != null) {
			TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setImage(SWTResourceManager.getImage("res_images/fox.jpg"));
			item.setToolTipText("FoxWalker");
			item.setVisible(true);
			final Menu menu = new Menu(getShell(), SWT.POP_UP);

			MenuItem MiniTray = new MenuItem(menu, SWT.PUSH);
			MiniTray.setText("Minimize to tray");
			MiniTray.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					getShell().setVisible(false);
					//getShell().setMaximized(false);
					//instance.setVisible(false);

				}
			});

			MenuItem Restore = new MenuItem(menu, SWT.PUSH);
			Restore.setText("Restore");
			Restore.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					getShell().setVisible(true);
					//getShell().setMaximized(true);
					//instance.setVisible(false);

				}
			});

			MenuItem Quit = new MenuItem(menu, SWT.PUSH);
			Quit.setText("Quit");
			Quit.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					exitWalker();
				}
			});


			item.addListener (SWT.MenuDetect, new Listener () {
				public void handleEvent (Event event) {
					menu.setVisible (true);
				}
			});
		}
	}
}
