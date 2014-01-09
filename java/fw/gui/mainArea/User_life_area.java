package fw.gui.mainArea;

import java.util.Iterator;

import javolution.util.FastMap;
import fw.game.L2ClassList;
import fw.game.model.L2Player;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Label;

import fw.com.swtdesigner.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class User_life_area extends Canvas {
	private FastMap<MouseListener, MouseListener> mouseListernes = new FastMap<MouseListener, MouseListener>();
	private ProgressBar cpBar;
	private ProgressBar xpBar;
	private ProgressBar mpBar;
	private ProgressBar hpBar;
	private Label lvlBar;
	private L2Player userChar = null;
	boolean isFocus = false;

	public User_life_area(Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			thisLayout.marginHeight = 1;
			thisLayout.marginWidth = 1;
			thisLayout.verticalSpacing = 0;
			thisLayout.marginBottom = 1;
			thisLayout.marginTop = 1;
			thisLayout.marginRight = 1;
			thisLayout.marginLeft = 1;
			thisLayout.horizontalSpacing = 0;
			this.setLayout(thisLayout);
			this.setSize(190, 100);
			this.setForeground(SWTResourceManager.getColor(255, 128, 0));
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
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent evt) {
					rootMouseDown(evt);
				}
			});
			this.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent evt) {
					rootPaintControl(evt);
				}
			});
			{
				lvlBar = new Label(this, SWT.NONE);
				GridData lvlBarLData = new GridData();
				lvlBarLData.verticalAlignment = GridData.BEGINNING;
				lvlBarLData.horizontalAlignment = GridData.FILL;
				lvlBar.setLayoutData(lvlBarLData);
				lvlBar.setText("Lvl:");
				lvlBar.setFont(SWTResourceManager.getFont("Arial", 9, 1, false,
						false));
				lvlBar.setAlignment(SWT.CENTER);
			}
			{
				cpBar = new ProgressBar(this, SWT.NONE);
				cpBar.setTextType(ProgressBar.TextType.VALUE);
				cpBar.setBackground(SWTResourceManager.getColor(128, 128, 128));
				cpBar.setForeground(SWTResourceManager.getColor(251, 245, 26));
				cpBar.setTextColor(SWTResourceManager.getColor(0, 0, 0));
				GridData bar1LData = new GridData();
				bar1LData.horizontalAlignment = GridData.FILL;
				bar1LData.grabExcessHorizontalSpace = true;
				bar1LData.verticalAlignment = GridData.FILL;
				bar1LData.grabExcessVerticalSpace = true;
				cpBar.setLayoutData(bar1LData);
				cpBar.setFont(SWTResourceManager.getFont("Courier New", 8, 0,
						false, false));
				cpBar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}

					@Override
					public void mouseDown(MouseEvent evt) {
						focusOnMouseDown(evt);
					}
				});
			}
			{
				hpBar = new ProgressBar(this, SWT.NONE);
				hpBar.setTextType(ProgressBar.TextType.VALUE);
				GridData progressBar1LData = new GridData();
				progressBar1LData.verticalAlignment = GridData.FILL;
				progressBar1LData.horizontalAlignment = GridData.FILL;
				progressBar1LData.grabExcessHorizontalSpace = true;
				progressBar1LData.grabExcessVerticalSpace = true;
				hpBar.setLayoutData(progressBar1LData);
				hpBar.setBackground(SWTResourceManager.getColor(128, 128, 128));
				hpBar.setForeground(SWTResourceManager.getColor(255, 0, 0));
				hpBar.setFont(SWTResourceManager.getFont("Courier New", 8, 0,
						false, false));
				hpBar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}

					@Override
					public void mouseDown(MouseEvent evt) {
						focusOnMouseDown(evt);
					}
				});
			}
			{
				mpBar = new ProgressBar(this, SWT.NONE);
				mpBar.setTextType(ProgressBar.TextType.VALUE);
				GridData progressBar2LData = new GridData();
				progressBar2LData.verticalAlignment = GridData.FILL;
				progressBar2LData.horizontalAlignment = GridData.FILL;
				progressBar2LData.grabExcessHorizontalSpace = true;
				progressBar2LData.grabExcessVerticalSpace = true;
				mpBar.setLayoutData(progressBar2LData);
				mpBar.setBackground(SWTResourceManager.getColor(128, 128, 128));
				mpBar.setForeground(SWTResourceManager.getColor(60, 157, 255));
				mpBar.setFont(SWTResourceManager.getFont("Courier New", 8, 0,
						false, false));
				mpBar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}

					@Override
					public void mouseDown(MouseEvent evt) {
						focusOnMouseDown(evt);
					}
				});
			}
			{
				xpBar = new ProgressBar(this, SWT.SMOOTH);
				GridData progressBar3LData = new GridData();
				progressBar3LData.verticalAlignment = GridData.FILL;
				progressBar3LData.horizontalAlignment = GridData.FILL;
				progressBar3LData.grabExcessHorizontalSpace = true;
				progressBar3LData.grabExcessVerticalSpace = true;
				xpBar.setTextType(ProgressBar.TextType.VALUE);
				xpBar.setLayoutData(progressBar3LData);
				xpBar.setBackground(SWTResourceManager.getColor(128, 128, 128));
				xpBar.setForeground(SWTResourceManager.getColor(221, 221, 221));
				xpBar.setFont(SWTResourceManager.getFont("Courier New", 8, 0,
						false, false));
				xpBar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent evt) {
						rootMouseDoubleClick(evt);
					}

					@Override
					public void mouseDown(MouseEvent evt) {
						focusOnMouseDown(evt);
					}
				});
			}

			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUserChartChar(L2Player userChar) {
		this.userChar = userChar;

	}

	public void removeUserChar() {
		this.userChar = null;
	}

	private void rootPaintControl(PaintEvent evt) {
		if (isFocus) {
			Point size = getSize();
			evt.gc.setLineWidth(2);
			evt.gc.setForeground(getForeground());
			evt.gc.setBackground(getForeground());
			evt.gc.setLineStyle(SWT.LINE_SOLID);
			evt.gc.drawRectangle(1, 1, size.x - 6, size.y - 6);
		}
	}

	private void rootMouseDown(MouseEvent evt) {
		setFocus();
	}

	private void rootFocusGained(FocusEvent evt) {
		isFocus = true;
		redraw();
	}

	private void rootFocusLost(FocusEvent evt) {
		isFocus = false;
		redraw();
	}

	private void focusOnMouseDown(MouseEvent evt) {
		setFocus();
	}

	public L2Player getUserChar() {
		return userChar;
	}

	public void setUserChar(L2Player userChar) {
		this.userChar = userChar;
		updateStatus();
	}

	public void updateStatus() {
		if (userChar == null)
			return;
		cpBar.setValues(userChar.getMaxCp(), (int)userChar.getCurrentCp());
		hpBar.setValues(userChar.getMaxHp(), (int)userChar.getCurrentHp());
		mpBar.setValues(userChar.getMaxMp(),  (int)userChar.getCurrentMp());
		xpBar.setValues(
				L2ClassList.getL2ClassExp(userChar.getLevel()+1),
				(int)userChar.getExp()								
				);
		lvlBar.setText("Lvl:" + userChar.getLevel());
	}

	@Override
	public void addMouseListener(MouseListener mouseListerne) {
		mouseListernes.put(mouseListerne, mouseListerne);
	}

	@Override
	public void removeMouseListener(MouseListener mouseListerne) {
		mouseListernes.remove(mouseListerne);
	}

	private void rootMouseDoubleClick(MouseEvent evt) {
		for (Iterator<MouseListener> iter = mouseListernes.values().iterator(); iter
				.hasNext();) {
			MouseListener element = (MouseListener) iter.next();
			element.mouseDoubleClick(evt);
		}
	}

}
