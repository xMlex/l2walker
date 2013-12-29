package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.L2PartyChar;
import java.util.Iterator;
import javolution.util.FastMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Display;
import fw.com.swtdesigner.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PartyAreaItem extends Canvas
{
	private FastMap<MouseListener, MouseListener> mouseListernes = null;
	private ProgressBar barCp;
	private ProgressBar barMp;
	private ProgressBar barHp;
	private CLabel lbName;
	private L2PartyChar l2PartyChar= null;
	boolean isFocus = false;
	private GameEngine gameEnginer = null; 

	public PartyAreaItem(Composite parent, int style)
	{
		super(parent, style);
		mouseListernes = new FastMap<MouseListener, MouseListener>();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			{
				GridLayout thisLayout = new GridLayout();
				thisLayout.verticalSpacing = 0;
				thisLayout.marginWidth = 1;
				thisLayout.marginHeight = 1;
				thisLayout.horizontalSpacing = 0;
				thisLayout.marginBottom = 1;
				thisLayout.marginLeft = 1;
				thisLayout.marginRight = 1;
				thisLayout.marginTop = 1;
				this.setLayout(thisLayout);
				this.setSize(203, 57);
				this.setForeground(SWTResourceManager.getColor(255, 128, 0));
				this.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent evt) {
						rootKeyPressed(evt);
					}
				});
				this.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent evt) {
						rootFocusLost(evt);
					}

					@Override
					public void focusGained(FocusEvent evt) {
						rootFocusGained(evt);
					}
				});
				this.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent evt) {
						rootPaintControl(evt);
					}
				});
				{
					lbName = new CLabel(this, SWT.NONE);
					lbName.setText("---");
					GridData lbNameLData = new GridData();
					lbNameLData.verticalAlignment = GridData.BEGINNING;
					lbNameLData.horizontalAlignment = GridData.FILL;
					lbNameLData.grabExcessHorizontalSpace = true;
					lbNameLData.heightHint = 17;
					lbName.setLayoutData(lbNameLData);
					lbName.setAlignment(SWT.CENTER);
					lbName.setFont(SWTResourceManager.getFont("Tahoma", 7, 1, false, false));
					lbName.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent evt) {
							rootKeyPressed(evt);
						}
					});
					lbName.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseDoubleClick(MouseEvent evt) {
							rootMouseDoubleClick(evt);
						}

						@Override
						public void mouseDown(MouseEvent evt) {
							lbNameMouseDown(evt);
						}
					});
				}
				{
					barCp = new ProgressBar(this, SWT.NONE);
					GridData bar1LData = new GridData();
					bar1LData.horizontalAlignment = GridData.FILL;
					bar1LData.verticalAlignment = GridData.FILL;
					bar1LData.grabExcessVerticalSpace = true;
					bar1LData.grabExcessHorizontalSpace = true;
					barCp.setLayoutData(bar1LData);
					barCp.setForeground(SWTResourceManager.getColor(210, 200, 0));
					barCp.setTextType(ProgressBar.TextType.VALUE);
					barCp.setBackground(SWTResourceManager.getColor(135,117,155));
					barCp.setFont(SWTResourceManager.getFont("Tahoma", 7, 0, false, false));
					barCp.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent evt) {
							rootKeyPressed(evt);
						}
					});
					barCp.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseDoubleClick(MouseEvent evt) {
							rootMouseDoubleClick(evt);
						}

						@Override
						public void mouseDown(MouseEvent evt) {
							barHpMouseDown(evt);
						}
					});
				}
				{
					barHp = new ProgressBar(this, SWT.NONE);
					barHp.setTextType(ProgressBar.TextType.VALUE);
					GridData progressBar1LData = new GridData();
					progressBar1LData.verticalAlignment = GridData.FILL;
					progressBar1LData.horizontalAlignment = GridData.FILL;
					progressBar1LData.grabExcessHorizontalSpace = true;
					progressBar1LData.grabExcessVerticalSpace = true;
					barHp.setLayoutData(progressBar1LData);
					barHp.setBackground(SWTResourceManager.getColor(135,117,155));
					barHp.setForeground(SWTResourceManager.getColor(255, 0, 0));
					barHp.setFont(SWTResourceManager.getFont("Tahoma", 7, 0, false, false));
					barHp.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent evt) {
							rootKeyPressed(evt);
						}
					});
					barHp.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseDown(MouseEvent evt) {
							barHpMouseDown(evt);
						}
						@Override
						public void mouseDoubleClick(MouseEvent evt) {
							rootMouseDoubleClick(evt);
						}
					});
				}
				{
					barMp = new ProgressBar(this, SWT.NONE);
					barMp.setTextType(ProgressBar.TextType.VALUE);
					GridData progressBar1LData1 = new GridData();
					progressBar1LData1.verticalAlignment = GridData.FILL;
					progressBar1LData1.horizontalAlignment = GridData.FILL;
					progressBar1LData1.grabExcessHorizontalSpace = true;
					progressBar1LData1.grabExcessVerticalSpace = true;
					barMp.setLayoutData(progressBar1LData1);
					barMp.setBackground(SWTResourceManager.getColor(135,117,155));
					barMp.setForeground(SWTResourceManager.getColor(40, 67, 251));
					barMp.setFont(SWTResourceManager.getFont("Tahoma", 7, 0, false, false));

				}
			}
			super.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseDown(MouseEvent evt)
				{
					rootMouseDown(evt);
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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

	private void barHpMouseDown(MouseEvent evt)
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

	 
	public void setProgressBarValues(final int maxValue,final int value)
	{		
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					if(isDisposed()|| barCp.isDisposed())return;
					barCp.setValues(maxValue, value);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});		
	}

	private void rootMouseDoubleClick(MouseEvent evt)
	{
		for (Iterator<MouseListener> iter = mouseListernes.values().iterator(); iter.hasNext();)
		{
			evt.widget = this;
			MouseListener element = (MouseListener) iter.next();
			element.mouseDoubleClick(evt);
		}

		//int control = ((evt.stateMask & SWT.CONTROL) != 0) ? 1 : 0;
		//int shift = ((evt.stateMask & SWT.SHIFT) != 0) ? 1 : 0;
		
		//if(l2PartyChar!=null)
		//gameEnginer.sendAction(l2PartyChar.objIdPartyChar);
 
	}
 

	public void setL2PartyChar(GameEngine gameEnginer,L2PartyChar l2PartyChar)
	{
		this.l2PartyChar = l2PartyChar;
		this.gameEnginer = gameEnginer;
		lbName.setText(l2PartyChar.partyCharName); 
		barCp.setValues(l2PartyChar.cpMax, l2PartyChar.cp);
		barHp.setValues(l2PartyChar.hpMax, l2PartyChar.hp);
		barMp.setValues(l2PartyChar.mpMax, l2PartyChar.mp);
	}

	public void updateL2PartyChar()
	{ 
		lbName.setText(l2PartyChar.partyCharName); 
		barCp.setValues(l2PartyChar.cpMax, l2PartyChar.cp);
		barHp.setValues(l2PartyChar.hpMax, l2PartyChar.hp);
		barMp.setValues(l2PartyChar.mpMax, l2PartyChar.mp);
	}
	
	public void removeL2PartyChar()
	{ 
		PartyArea partyArea = (PartyArea) getParent().getParent();
		l2PartyChar = null;
		gameEnginer = null; 
		partyArea.removeItem(this);
	}
 
	private void lbNameMouseDown(MouseEvent evt)
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
	
	private void rootKeyPressed(KeyEvent evt) { 
				
		if(evt.keyCode==13 || evt.keyCode==SWT.KEYPAD_CR)
		{
			//int control = ((evt.stateMask & SWT.CONTROL) != 0) ? 1 : 0;
			//int shift = ((evt.stateMask & SWT.SHIFT) != 0) ? 1 : 0;
			//if(l2PartyChar!=null)
			//	gameEnginer.sendAction(l2PartyChar.objIdPartyChar);
			 
		}
		
		
		
	}
	
	 

}
