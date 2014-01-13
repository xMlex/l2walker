package fw.gui.dialogs;

import fw.game.L2Item;
import fw.gui.mainArea.L2ItemVisualObject;
import fw.gui.mainArea.UserMainArea;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class RequestTradeDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Composite gridArea;
	private Button btnCancel;
	private Button btnOk;
	private TableColumn myTradeItemsCount;
	private TableColumn hisItemCount;
	private TableColumn hisItemName;
	private TableColumn myItemCount;
	private Table hisItems;
	private Table myTradeItems;
	private Table myItems;
	private TableColumn myItemName;
	private TableColumn myTradeItemsName;
	private L2Item items[];
	private int count=1;
	UserMainArea mainArea;
	Shell shell;


	public RequestTradeDialog(Shell parent, int style) {
		super(parent, style);
	}

	public RequestTradeDialog(Shell parent) {
		    super(parent);
	}

	public static void createDialog(final UserMainArea mainArea, final L2Item items[])
	{
		new Thread()
		{
			@Override
			public void run()
			{

				Display.getDefault().syncExec(new Runnable()
				{
					public void run()
					{
						try
						{

							Display display = Display.getDefault();
							Shell shell = new Shell(display, SWT.ON_TOP | SWT.TITLE);

							shell.setText("["+mainArea.gameEngine.getSelfChar().getName()+"] - Trade");

							shell.setBounds(171, 100, 834, 608);
							shell.addShellListener(new ShellAdapter()
							{
								@Override
								public void shellClosed(ShellEvent evt)
								{
									evt.doit = false;
								}
							});

							RequestTradeDialog inst = new RequestTradeDialog(shell, SWT.NULL);
							inst.items = items;
							inst.shell = shell;
							inst.mainArea = mainArea;
							mainArea.myTradeDialog = inst;
							inst.open();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		}.start();

	}

	public void open() {

		try {
			dialogShell = shell;
//			Display display = Display.getDefault();
//			dialogShell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setLayout(new FormLayout());
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(305, 450);
			{
				gridArea = new Composite(dialogShell, SWT.NONE);
				GridLayout gridAreaLayout = new GridLayout();
				gridAreaLayout.makeColumnsEqualWidth = false;
				gridAreaLayout.numColumns = 2;
				FormData gridAreaLData = new FormData();
				gridAreaLData.width = 304;
				gridAreaLData.height = 445;
				gridAreaLData.top =  new FormAttachment(0, 1000, 0);
				gridAreaLData.left =  new FormAttachment(0, 1000, 0);
				gridAreaLData.bottom =  new FormAttachment(1000, 1000, 0);
				gridAreaLData.right =  new FormAttachment(1000, 1000, 0);
				gridArea.setLayoutData(gridAreaLData);
				gridArea.setLayout(gridAreaLayout);
				{
					myItems = new Table(gridArea, SWT.NONE|SWT.FULL_SELECTION);
					myItems.setHeaderVisible(true);
					GridData myItemsLData = new GridData();
					myItemsLData.verticalAlignment = GridData.BEGINNING;
					myItemsLData.grabExcessHorizontalSpace = true;
					myItemsLData.horizontalAlignment = GridData.FILL;
					myItemsLData.heightHint = 150;
					myItemsLData.horizontalSpan = 2;
					myItems.setLayoutData(myItemsLData);
					myItems.setLinesVisible(true);
					{
						myItemName = new TableColumn(myItems, SWT.NONE);
						myItemName.setText("My Items");
						myItemName.setWidth(220);
					}
					{
						myItemCount = new TableColumn(myItems, SWT.NONE);
						myItemCount.setText("Count");
						myItemCount.setWidth(60);
					}
					myItems.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseDoubleClick(
							MouseEvent evt) {
							AddItemToTrade();
						}
					});

				}
				{
					myTradeItems = new Table(gridArea, SWT.NONE);
					myTradeItems.setHeaderVisible(true);
					GridData hisItemsLData = new GridData();
					hisItemsLData.verticalAlignment = GridData.BEGINNING;
					hisItemsLData.grabExcessHorizontalSpace = true;
					hisItemsLData.horizontalAlignment = GridData.FILL;
					hisItemsLData.heightHint = 90;
					hisItemsLData.horizontalSpan = 2;
					myTradeItems.setLayoutData(hisItemsLData);
					myTradeItems.setLinesVisible(true);
					{
						myTradeItemsName = new TableColumn(myTradeItems, SWT.NONE);
						myTradeItemsName.setText("My trade items");
						myTradeItemsName.setWidth(220);
					}
					{
						myTradeItemsCount = new TableColumn(myTradeItems, SWT.NONE);
						myTradeItemsCount.setText("Count");
						myTradeItemsCount.setWidth(60);
					}
				}
				{
					hisItems = new Table(gridArea, SWT.NONE);
					hisItems.setHeaderVisible(true);
					GridData hisItemsLData = new GridData();
					hisItemsLData.verticalAlignment = GridData.BEGINNING;
					hisItemsLData.grabExcessHorizontalSpace = true;
					hisItemsLData.horizontalAlignment = GridData.FILL;
					hisItemsLData.heightHint = 90;
					hisItemsLData.horizontalSpan = 2;
					hisItems.setLayoutData(hisItemsLData);
					hisItems.setLinesVisible(true);
					{
						hisItemName = new TableColumn(hisItems, SWT.NONE);
						hisItemName.setText("His trade items");
						hisItemName.setWidth(220);
					}
					{
						hisItemCount = new TableColumn(hisItems, SWT.NONE);
						hisItemCount.setText("Count");
						hisItemCount.setWidth(60);
					}
				}
				{
					btnOk = new Button(gridArea, SWT.PUSH | SWT.CENTER);
					GridData btnOkLData = new GridData();
					btnOkLData.horizontalAlignment = GridData.END;
					btnOkLData.verticalAlignment = GridData.END;
					btnOkLData.grabExcessHorizontalSpace = true;
					btnOkLData.widthHint = 50;
					btnOkLData.heightHint = 25;
					btnOk.setLayoutData(btnOkLData);
					btnOk.setText("Ok");
				    btnOk.addListener(SWT.Selection, new Listener() {
				        public void handleEvent(Event event) {
				        	//mainArea.gameEngine.sendTradeDone(1);
				        }
				    });
				}
				{
					btnCancel = new Button(gridArea, SWT.PUSH | SWT.CENTER);
					GridData btnCancelLData = new GridData();
					btnCancelLData.verticalAlignment = GridData.END;
					btnCancelLData.horizontalAlignment = GridData.END;
					btnCancelLData.widthHint = 50;
					btnCancelLData.heightHint = 25;
					btnCancel.setLayoutData(btnCancelLData);
					btnCancel.setText("Cancel");
					btnCancel.addListener(SWT.Selection, new Listener() {
					        public void handleEvent(Event event) {
					        	//mainArea.gameEngine.sendTradeDone(0);
					        }
					});
				}
			}


			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();

			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {

				if (!display.readAndDispatch()){
					display.sleep();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addMyItems()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					gridArea.setRedraw(false);

					Table grid = myItems;

					for(int i=0;i<items.length;i++){
						L2Item l2Item = items[i];
						L2ItemVisualObject visualObject = new L2ItemVisualObject();
						visualObject.tableItemChar = new TableItem(grid, SWT.NONE);
						visualObject.tableItemChar.setData(l2Item);
						visualObject.tableItemChar.setText(new String[]
						{
								l2Item.dbL2Item.getName(), String.valueOf(l2Item.itemCount)
						});

						l2Item.visualObject = visualObject;
					}

					gridArea.layout();
					gridArea.pack();
					gridArea.setRedraw(true);
					gridArea.redraw();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void addOwnItem(final L2Item item)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					gridArea.setRedraw(false);
					Table grid = myTradeItems;

						L2ItemVisualObject visualObject = new L2ItemVisualObject();
						visualObject.tableItemChar = new TableItem(grid, SWT.NONE);
						visualObject.tableItemChar.setData(item);
						visualObject.tableItemChar.setText(new String[]
						{
								item.dbL2Item.getName(), String.valueOf(item.itemCount)
						});

						item.visualObject = visualObject;


					gridArea.layout();
					gridArea.pack();
					gridArea.setRedraw(true);
					gridArea.redraw();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}


	public void addOtherItem(final L2Item item)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					gridArea.setRedraw(false);
					Table grid = hisItems;

						L2ItemVisualObject visualObject = new L2ItemVisualObject();
						visualObject.tableItemChar = new TableItem(grid, SWT.NONE);
						visualObject.tableItemChar.setData(item);
						visualObject.tableItemChar.setText(new String[]
						{
								item.dbL2Item.getName(), String.valueOf(item.itemCount)
						});

						item.visualObject = visualObject;

					gridArea.layout();
					gridArea.pack();
					gridArea.setRedraw(true);
					gridArea.redraw();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void AddItemToTrade()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					if (myItems.getSelectionCount() == 0)
						return;
					TableItem tableItem = myItems.getSelection()[0];
					L2Item tradeItem = (L2Item) tableItem.getData();

					InputNumberDialog dialog  = new	InputNumberDialog(shell);
					count=1;
					count = dialog.open();

					if(tradeItem.itemCount > 1)
					{
						if(count <= tradeItem.itemCount){
							//mainArea.gameEngine.sendAddTradeItem(tradeItem,count);
						}
					}else{
						//mainArea.gameEngine.sendAddTradeItem(tradeItem,1);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
	}

	public void confirmedTrade()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					hisItems.setEnabled(false);
					myTradeItems.setEnabled(false);
					myItems.setEnabled(false);
					shell.setText(shell.getText()+ " - Done");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
	}

	public void closeTradeDialog(){
		mainArea.myTradeDialog = null;
		shell.dispose();
	}

}
