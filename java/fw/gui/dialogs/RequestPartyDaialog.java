package fw.gui.dialogs;

import fw.gui.mainArea.ProgressBar;
import fw.gui.mainArea.UserMainArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fw.com.swtdesigner.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class RequestPartyDaialog extends org.eclipse.swt.widgets.Dialog
{

	private static Font msgFont = SWTResourceManager.getFont("Tahoma", 8, 1, false, false);
	private static Color bgBar = SWTResourceManager.getColor(253, 223, 248);
	private static Color fgBar = SWTResourceManager.getColor(100, 100, 100);
	private Shell dialogShell;
	private Button btnYes;
	private ProgressBar progress;
	private CLabel lbMsg;
	private Button btnNo;
	UserMainArea mainArea;
	Shell shell;
	String msg;
	boolean isDisposed = false;

	public RequestPartyDaialog(Shell parent, int style)
	{
		super(parent, style);
	}

	public static void createDialog(final UserMainArea mainArea, final String msg)
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

							shell.setText("["+mainArea.gameEngine.getUserChar().realName+"]");

							shell.setBounds(171, 100, 834, 608);
							shell.addShellListener(new ShellAdapter()
							{
								@Override
								public void shellClosed(ShellEvent evt)
								{
									evt.doit = false;
								}
							});

							RequestPartyDaialog inst = new RequestPartyDaialog(shell, SWT.NULL);
							inst.shell = shell;
							inst.mainArea = mainArea;
							inst.msg = msg;

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

	public void open()
	{
		try
		{
			dialogShell = shell;
			shell.addDisposeListener(new DisposeListener()
			{
				public void widgetDisposed(DisposeEvent evt)
				{
					rootWidgetDisposed(evt);
				}
			});
			dialogShell.setLayout(null);
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(289, 109);

			{
				btnNo = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				btnNo.setText("No");
				btnNo.setBounds(213, 52, 64, 23);
				btnNo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent evt)
					{
						btnNoWidgetSelected(evt);
					}
				});
			}

			{
				btnYes = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				btnYes.setText("Yes");
				btnYes.setBounds(5, 52, 64, 23);
				btnYes.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent evt)
					{
						btnYesWidgetSelected(evt);
					}
				});
			}

			{
				lbMsg = new CLabel(dialogShell, SWT.NONE);
				lbMsg.setText(msg);
				lbMsg.setBounds(3, 2, 275, 24);
				lbMsg.setFont(msgFont);
			}
			{
				progress = new ProgressBar(dialogShell, SWT.SMOOTH);
				progress.setBounds(4, 35, 274, 14);
				progress.setValues(250, 250);
				progress.setBackground(bgBar);
				progress.setForeground(fgBar);

			}
			dialogShell.open();
			Display display = dialogShell.getDisplay();

			new Thread()
			{
				@Override
				public void run()
				{
					for (int i = 250; i > 0; i--)
					{
						try
						{
							if (isDisposed)
								return;
							Display.getDefault().syncExec(new Runnable()
							{
								public void run()
								{
									try
									{
										if (!progress.isDisposed())
											progress.setValue(progress.getValue() - 1);
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							});

							sleep(80);
						}
						catch (InterruptedException e)
						{
						}
					}

					Display.getDefault().syncExec(new Runnable()
					{
						public void run()
						{
							try
							{
								if (!dialogShell.isDisposed())
								{
									mainArea.gameEngine.sendAnswerJoinParty(0);
									shell.dispose();
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

			while (!dialogShell.isDisposed())
			{
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void btnYesWidgetSelected(SelectionEvent evt)
	{
		mainArea.gameEngine.sendAnswerJoinParty(1);
		shell.dispose();
	}

	private void btnNoWidgetSelected(SelectionEvent evt)
	{
		mainArea.gameEngine.sendAnswerJoinParty(0);
		shell.dispose();
	}

	private void rootWidgetDisposed(DisposeEvent evt)
	{
		isDisposed = true;
	}

}
