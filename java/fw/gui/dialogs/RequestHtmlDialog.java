package fw.gui.dialogs;


import fw.gui.mainArea.UserMainArea;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


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
public class RequestHtmlDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private static String myMsg;
	static private Browser htmlArea;
	UserMainArea mainArea;
	boolean isDisposed = false;

	/**
	* Auto-generated main method to display this
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(final UserMainArea mainArea, final String msg) {

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
							myMsg = msg;
							RequestHtmlDialog inst = new RequestHtmlDialog(shell, SWT.NULL);
							inst.mainArea = mainArea;
							inst.open();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}.start();


	}

	public RequestHtmlDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText("Npc Dialog - "+"["+mainArea.gameEngine.getUserChar().realName+"]");
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
			dialogShell.setSize(350, 450);

			Rectangle shellBounds = parent.getBounds();
	        Point dialogSize = dialogShell.getSize();

	        dialogShell.setLocation(
	          shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
	          shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
			{
				htmlArea = new Browser(dialogShell, SWT.NONE);
				htmlArea.setSize(330, 410);
				htmlArea.setText(myMsg);

				htmlArea.addLocationListener(new LocationAdapter() {
					@Override
					public void changing(LocationEvent event) {
				        linkSelected(event);

					}
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

	private void linkSelected(LocationEvent event)
	{
		String url = event.location.substring(6);
        event.doit = false;
		mainArea.gameEngine.sendHtmlRequest(url);
		dialogShell.dispose();
	}

	private void rootWidgetDisposed(DisposeEvent evt)
	{
		isDisposed = true;
	}
}
