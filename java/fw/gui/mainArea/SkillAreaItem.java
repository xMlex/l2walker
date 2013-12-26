package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.L2Skill;
import fw.game.L2SkillUse;
import fw.gui.mainArea.efects.VisualEfectsTimer;

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
public class SkillAreaItem extends Canvas
{
	private FastMap<MouseListener, MouseListener> mouseListernes = null;
	private ProgressBar bar1;
	private CLabel lbName;
	private L2Skill l2Skill = null;
	boolean isFocus = false;
	private GameEngine gameEnginer = null;
	private VisualEfectsTimer visualEfectsTimer=null;

	public SkillAreaItem(Composite parent, int style)
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
				thisLayout.numColumns = 3;
				thisLayout.verticalSpacing = 1;
				thisLayout.marginWidth = 1;
				thisLayout.marginTop = 1;
				thisLayout.marginRight = 1;
				thisLayout.marginLeft = 1;
				thisLayout.marginHeight = 1;
				thisLayout.marginBottom = 1;
				thisLayout.horizontalSpacing = 1;
				this.setLayout(thisLayout);
				this.setSize(188, 25);
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
					lbNameLData.verticalAlignment = GridData.FILL;
					lbNameLData.horizontalAlignment = GridData.FILL;
					lbNameLData.grabExcessVerticalSpace = true;
					lbNameLData.grabExcessHorizontalSpace = true;
					lbName.setLayoutData(lbNameLData);
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
					bar1 = new ProgressBar(this, SWT.NONE);
					GridData bar1LData = new GridData();
					bar1LData.horizontalAlignment = GridData.END;
					bar1LData.verticalAlignment = GridData.FILL;
					bar1LData.grabExcessVerticalSpace = true;
					bar1LData.widthHint = 79;
					bar1.setLayoutData(bar1LData);
					bar1.setForeground(SWTResourceManager.getColor(157, 150, 250));
					bar1.setTextType(ProgressBar.TextType.PERCENT);
					bar1.setBackground(SWTResourceManager.getColor(135, 117, 155));
					bar1.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent evt) {
							rootKeyPressed(evt);
						}
					});
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

	public void skillUse(final L2SkillUse l2SkillUse)
	{
		if(l2Skill!=null)
		 visualEfectsTimer.addSkillInEfect(l2Skill, l2SkillUse);
	}
	
	public void setProgressBarValues(final int maxValue,final int value)
	{		
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					if(isDisposed()|| bar1.isDisposed())return;
					bar1.setValues(maxValue, value);
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

		int control = ((evt.stateMask & SWT.CONTROL) != 0) ? 1 : 0;
		int shift = ((evt.stateMask & SWT.SHIFT) != 0) ? 1 : 0;

		if (l2Skill != null)
			gameEnginer.sendRequestUseSkill(l2Skill.skill_id, control, shift);
	}

	public L2Skill getL2Skill()
	{
		return l2Skill;
	}

	public void setL2Skill(GameEngine gameEnginer,VisualEfectsTimer visualEfectsTimer, L2Skill l2Skill)
	{
		this.l2Skill = l2Skill;
		this.gameEnginer = gameEnginer;
		this.visualEfectsTimer=visualEfectsTimer;

		lbName.setText(l2Skill.dbL2Skill.getName()+" ["+l2Skill.level+"]");
		lbName.setToolTipText(l2Skill.dbL2Skill.getDesc());
		bar1.setToolTipText(l2Skill.dbL2Skill.getDesc());
	}

	public void removeL2Skill()
	{
		SkillArea skillArea = (SkillArea) getParent().getParent();
		l2Skill = null;
		gameEnginer = null;
		skillArea.removeItem(this); 
	}

	public void setGameEnginer(GameEngine gameEnginer)
	{
		this.gameEnginer = gameEnginer;
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
			int control = ((evt.stateMask & SWT.CONTROL) != 0) ? 1 : 0;
			int shift = ((evt.stateMask & SWT.SHIFT) != 0) ? 1 : 0;
			
			if (l2Skill != null)
				gameEnginer.sendRequestUseSkill(l2Skill.skill_id, control, shift);
		}
		
	}
	
	 

}
