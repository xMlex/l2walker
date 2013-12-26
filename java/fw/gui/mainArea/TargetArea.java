package fw.gui.mainArea;

import java.util.Iterator;

import javolution.util.FastMap;
import fw.game.L2Char;
import fw.game.NpcChar;
import fw.game.PlayerChar;
import fw.game.UserChar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import fw.com.swtdesigner.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class TargetArea extends Canvas
{
	private FastMap<MouseListener, MouseListener> mouseListernes = new FastMap<MouseListener, MouseListener>();
	private CLabel lbName;
	private CLabel lbClass;
	private CLabel lbClan;
	private ProgressBar bar1;
	private L2Char targetChar = null;
	boolean isFocus = false;

	public TargetArea(Composite parent, int style)
	{
		super(parent, style);
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			thisLayout.marginHeight = 2;
			thisLayout.marginWidth = 2;
			thisLayout.verticalSpacing = 0;
			thisLayout.marginBottom = 2;
			thisLayout.marginTop = 2;
			thisLayout.marginRight = 2;
			thisLayout.marginLeft = 2;
			this.setLayout(thisLayout);
			this.setSize(159, 84);
			this.setForeground(SWTResourceManager.getColor(255, 128, 0));
			this.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(FocusEvent evt)
				{
					rootFocusLost(evt);
				}

				@Override
				public void focusGained(FocusEvent evt)
				{
					rootFocusGained(evt);
				}
			});
			super.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseDown(MouseEvent evt)
				{
					rootMouseDown(evt);
				}
			});
			this.addPaintListener(new PaintListener()
			{
				public void paintControl(PaintEvent evt)
				{
					rootPaintControl(evt);
				}
			});
			{
				lbName = new CLabel(this, SWT.NONE);
				lbName.setText("---");
				lbName.setAlignment(SWT.CENTER);
				GridData lbNameLData = new GridData();
				lbNameLData.horizontalAlignment = GridData.FILL;
				lbNameLData.grabExcessHorizontalSpace = true;
				lbNameLData.verticalAlignment = GridData.BEGINNING;
				lbNameLData.heightHint = 16;
				lbName.setLayoutData(lbNameLData);
				lbName.setFont(SWTResourceManager.getFont("Tahoma", 7, 1, false, false));
				lbName.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseDoubleClick(MouseEvent evt)
					{
						rootMouseDoubleClick(evt);
					}

					@Override
					public void mouseDown(MouseEvent evt)
					{
						lbClanMouseDown(evt);
					}
				});
			}
			{
				lbClass = new CLabel(this, SWT.NONE);
				lbClass.setAlignment(SWT.CENTER);
				lbClass.setText("---");
				GridData cLabel1LData = new GridData();
				cLabel1LData.verticalAlignment = GridData.BEGINNING;
				cLabel1LData.horizontalAlignment = GridData.FILL;
				cLabel1LData.heightHint = 13;
				cLabel1LData.grabExcessHorizontalSpace = true;
				lbClass.setLayoutData(cLabel1LData);
				lbClass.setFont(SWTResourceManager.getFont("Tahoma", 7, 0, false, false));
				lbClass.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}
					@Override
					public void mouseDown(MouseEvent evt) {
						lbClanMouseDown(evt);
					}
				});
			}
			{
				lbClan = new CLabel(this, SWT.NONE);
				lbClan.setAlignment(SWT.CENTER);
				lbClan.setText("---");
				GridData cLabel1LData1 = new GridData();
				cLabel1LData1.verticalAlignment = GridData.BEGINNING;
				cLabel1LData1.horizontalAlignment = GridData.FILL;
				cLabel1LData1.heightHint = 13;
				cLabel1LData1.grabExcessHorizontalSpace = true;
				lbClan.setLayoutData(cLabel1LData1);
				lbClan.setFont(SWTResourceManager.getFont("Tahoma", 7, 0, false, false));
				lbClan.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}
					@Override
					public void mouseDown(MouseEvent evt) {
						lbClanMouseDown(evt);
					}
				});
			}
			{
				bar1 = new ProgressBar(this, SWT.BORDER);
				bar1.setBackground(SWTResourceManager.getColor(128, 128, 128));
				bar1.setForeground(SWTResourceManager.getColor(255, 0, 0));
				bar1.setTextType(ProgressBar.TextType.VALUE);
				bar1.setTextColor(SWTResourceManager.getColor(253, 223, 248));
				GridData bar1LData = new GridData();
				bar1LData.horizontalAlignment = GridData.FILL;
				bar1LData.grabExcessHorizontalSpace = true;
				bar1LData.verticalAlignment = GridData.FILL;
				bar1LData.grabExcessVerticalSpace = true;
				bar1.setLayoutData(bar1LData);
				bar1.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
				bar1.setVisible(false);
				bar1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}
					@Override
					public void mouseDown(MouseEvent evt) {
						bar1MouseDown(evt);
					}
				});
			}
			this.layout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateHP()
	{
		if (targetChar != null)
			bar1.setValues(targetChar.hpMax, targetChar.hp);
	}

	public L2Char getTargetChar()
	{
		return targetChar;
	}

	public void setTargetChar(L2Char targetChar)
	{
		this.targetChar = targetChar;
		lbName.setText(targetChar.realName);

		if (targetChar instanceof NpcChar)
		{
			bar1.setValues(targetChar.hpMax, targetChar.hp);
			lbClass.setText(((NpcChar) targetChar).dbl2npc.getType());
			lbClan.setText("");
			bar1.setVisible(true);
		} else if (targetChar instanceof PlayerChar)
		{
			lbClass.setText(((PlayerChar) targetChar).className);
			lbClan.setText(((PlayerChar) targetChar).clanInfo.clanName);
			bar1.setVisible(false);
		} else if (targetChar instanceof UserChar)
		{
			bar1.setValues(targetChar.hpMax, targetChar.hp);
			lbClass.setText("");
			lbClan.setText("");
			bar1.setVisible(true);
		}

	}

	public void removeTargetChar()
	{
		this.targetChar = null;
		lbName.setText("---");
		lbClass.setText("---");
		lbClan.setText("---");
		bar1.setVisible(false);
	}

	private void rootPaintControl(PaintEvent evt)
	{
		if (isFocus)
		{
			Point size = getSize();
			evt.gc.setLineWidth(2);
			evt.gc.setForeground(getForeground());
			evt.gc.setBackground(getForeground());
			evt.gc.setLineStyle(SWT.LINE_SOLID);
			evt.gc.drawRectangle(1, 1, size.x - 6, size.y - 6);
		}
	}

	private void rootMouseDown(MouseEvent evt)
	{
		setFocus();
	}

	private void rootFocusGained(FocusEvent evt)
	{
		isFocus = true;
		redraw();
	}

	private void rootFocusLost(FocusEvent evt)
	{
		isFocus = false;
		redraw();
	}

	private void lbClanMouseDown(MouseEvent evt)
	{
		setFocus();
	}

	private void bar1MouseDown(MouseEvent evt)
	{
		setFocus();
	}

	@Override
	public void addMouseListener(MouseListener mouseListerne)
	{
		mouseListernes.put(mouseListerne, mouseListerne);
	}

	@Override
	public void removeMouseListener(MouseListener mouseListerne)
	{
		mouseListernes.remove(mouseListerne);
	}

	private void rootMouseDoubleClick(MouseEvent evt)
	{
		for (Iterator<MouseListener> iter = mouseListernes.values().iterator(); iter.hasNext();)
		{
			MouseListener element = (MouseListener) iter.next();
			element.mouseDoubleClick(evt);
		}
	}
	
	 

}
