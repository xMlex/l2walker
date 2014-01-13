package fw.gui.dialogs;

import fw.game.L2ClassList;
import fw.game.model.CharSelectInfoPackage;
import fw.gui.mainArea.UserMainArea;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;



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
public class RequestHeroesListDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Table heroesTable;
	private TableColumn heroCount;
	private TableColumn heroAlly;
	private TableColumn heroClan;
	private TableColumn heroClass;
	private TableColumn heroName;
	UserMainArea mainArea;
	CharSelectInfoPackage[] myHeroes;
	int heroesSize;
	TableItem[] tableHeroChar;
	boolean isDisposed = false;

	/**
	* Auto-generated main method to display this
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(final UserMainArea mainArea, final CharSelectInfoPackage[] heroesList,final int size) {

		new Thread()
		{
			@Override
			public void run()
			{

				Display.getDefault().syncExec(new Runnable()
				{
					public void run()
					{
						try {
							Display display = Display.getDefault();
							Shell shell = new Shell(display, SWT.ON_TOP | SWT.TITLE);

							shell.setBounds(171, 100, 834, 608);
							shell.addShellListener(new ShellAdapter()
							{
								@Override
								public void shellClosed(ShellEvent evt)
								{
									evt.doit = false;
								}
							});

							RequestHeroesListDialog inst = new RequestHeroesListDialog(shell, SWT.NULL);
							inst.mainArea = mainArea;
							inst.myHeroes = heroesList;
							inst.heroesSize = size;
							inst.open();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}.start();


	}

	public RequestHeroesListDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText("Heroes List - "+"["+mainArea.gameEngine.getSelfChar().getName()+"]");
			dialogShell.addDisposeListener(new DisposeListener()
			{
				public void widgetDisposed(DisposeEvent evt)
				{
					rootWidgetDisposed(evt);
				}
			});

			dialogShell.setLayout(null);
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(600, 400);
			{
				{
					heroesTable = new Table(dialogShell, SWT.NONE);
					heroesTable.setHeaderVisible(true);
					heroesTable.setLinesVisible(true);
					heroesTable.setSize(730,  410);
					{
						heroName = new TableColumn(heroesTable, SWT.NONE);
						heroName.setText("Name");
						heroName.setWidth(180);
					}
					{
						heroClass = new TableColumn(heroesTable, SWT.NONE);
						heroClass.setText("Class");
						heroClass.setWidth(160);
					}
					{
						heroClan = new TableColumn(heroesTable, SWT.NONE);
						heroClan.setText("Clan");
						heroClan.setWidth(160);
					}
					{
						heroAlly = new TableColumn(heroesTable, SWT.NONE);
						heroAlly.setText("Ally");
						heroAlly.setWidth(150);
					}
					{
						heroCount = new TableColumn(heroesTable, SWT.NONE);
						heroCount.setText("Count");
						heroCount.setWidth(60);
					}
				}

			}


			tableHeroChar = new TableItem[heroesSize];

			for(int i=0;i<heroesSize;i++)
			{
				tableHeroChar[i] = new TableItem(heroesTable, SWT.NONE);

				String[] realClassName = L2ClassList.getL2ClassName(myHeroes[i].getClassId()).split("_");

				tableHeroChar[i].setText(new String[]
				{
						myHeroes[i].getName(), realClassName[1], myHeroes[i].getName(), myHeroes[i].getName(),String.valueOf(myHeroes.length)
				});
			}

			dialogShell.open();
			Display display = dialogShell.getDisplay();


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
								if (isDisposed)
									return;

								if (!dialogShell.isDisposed())
								{
									//dialogShell.dispose();
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});

				}
			}.start();


			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rootWidgetDisposed(DisposeEvent evt)
	{
		isDisposed = true;
	}
}
