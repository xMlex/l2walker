package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.L2Item;
import fw.gui.dialogs.InputNumberDialog;

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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import fw.com.swtdesigner.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class InventoryAreaItem extends Canvas
{
	private FastMap<MouseListener, MouseListener> mouseListernes = null;
	private CLabel lbName;
	private L2Item l2Item = null;
	private CLabel lbQtd;
	boolean isFocus = false;
	private GameEngine gameEnginer = null;
	private static Color equipedItemTextColor=SWTResourceManager.getColor(255, 0, 0);
	private static Color unequipedItemTextColor=SWTResourceManager.getColor(0, 0, 0);

	public InventoryAreaItem(Composite parent, int style)
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
				Menu popUpMenu = new Menu(getShell(),SWT.POP_UP);
				MenuItem dropItem = new MenuItem(popUpMenu,SWT.PUSH);
				dropItem.setText("Drop");
				dropItem.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {
						dropItem();
					}
				});

				MenuItem deleteItem = new MenuItem(popUpMenu,SWT.PUSH);
				deleteItem.setText("Delete");
				deleteItem.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {
						deleteItem();
					}
				});
				MenuItem crystalizeItem = new MenuItem(popUpMenu,SWT.PUSH);
				crystalizeItem.setText("Crystalize");
				crystalizeItem.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {

					}
				});

				GridLayout thisLayout = new GridLayout();
				thisLayout.numColumns = 2;
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
				this.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyPressed(KeyEvent evt)
					{
						lbQtdKeyPressed(evt);
					}
				});
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
					GridData lbNameLData = new GridData();
					lbNameLData.verticalAlignment = GridData.FILL;
					lbNameLData.horizontalAlignment = GridData.FILL;
					lbNameLData.grabExcessVerticalSpace = true;
					lbNameLData.grabExcessHorizontalSpace = true;
					lbName.setLayoutData(lbNameLData);
					lbName.setMenu(popUpMenu);
					lbName.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyPressed(KeyEvent evt)
						{
							lbQtdKeyPressed(evt);
						}
					});
					lbName.addMouseListener(new MouseAdapter()
					{
						@Override
						public void mouseDoubleClick(MouseEvent evt)
						{
							lbQtdMouseDoubleClick(evt);
						}

						@Override
						public void mouseDown(MouseEvent evt)
						{
							lbQtdMouseDown(evt);
						}
					});
				}
				{
					lbQtd = new CLabel(this, SWT.NONE);
					lbQtd.setText("0");
					GridData cLabel1LData = new GridData();
					cLabel1LData.verticalAlignment = GridData.FILL;
					cLabel1LData.horizontalAlignment = GridData.END;
					cLabel1LData.grabExcessVerticalSpace = true;
					cLabel1LData.widthHint = 80;
					lbQtd.setLayoutData(cLabel1LData);
					lbQtd.setAlignment(SWT.RIGHT);
					lbQtd.setForeground(SWTResourceManager.getColor(0, 0, 255));
					lbQtd.setFont(SWTResourceManager.getFont("Tahoma", 7, 1, false, false));
					lbQtd.setMenu(popUpMenu);
					lbQtd.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent evt) {
							lbQtdKeyPressed(evt);
						}
					});
					lbQtd.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseDown(MouseEvent evt) {
							lbQtdMouseDown(evt);
						}
						@Override
						public void mouseDoubleClick(MouseEvent evt) {
							lbQtdMouseDoubleClick(evt);
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

	public void itemUse()
	{

	}

	private void lbQtdMouseDoubleClick(MouseEvent evt)
	{
		for (Iterator<MouseListener> iter = mouseListernes.values().iterator(); iter.hasNext();)
		{
			evt.widget = this;
			MouseListener element = (MouseListener) iter.next();
			element.mouseDoubleClick(evt);
		}

		//int control = ((evt.stateMask & SWT.CONTROL) != 0) ? 1 : 0;
		//int shift = ((evt.stateMask & SWT.SHIFT) != 0) ? 1 : 0;

		if (l2Item != null)
		{
			 //gameEnginer.sendUseItem(l2Item.objectId);
		}

	}

	public L2Item getL2Item()
	{
		return l2Item;
	}

	public void setL2Item(GameEngine gameEnginer, L2Item l2Item)
	{
		this.l2Item = l2Item;
		this.gameEnginer = gameEnginer;

		if(l2Item.isEquiped==1)
			lbName.setForeground(equipedItemTextColor);
		else
			lbName.setForeground(unequipedItemTextColor);

		lbName.setText(l2Item.dbL2Item.getName());
		lbQtd.setText(String.valueOf( l2Item.itemCount));
	}

	public void updateL2Item()
	{
		assert(l2Item!=null);

		if(l2Item.isEquiped==1)
			lbName.setForeground(equipedItemTextColor);
		else
			lbName.setForeground(unequipedItemTextColor);

		lbName.setText(l2Item.dbL2Item.getName());
		lbQtd.setText(String.valueOf( l2Item.itemCount));
	}

	public void removeItem()
	{
		InventoryArea inventoryArea = (InventoryArea) getParent().getParent();
		l2Item = null;
		gameEnginer = null;
		inventoryArea.removeItem(this);
	}

	public void clearItemAndDispose()
	{
		l2Item = null;
		gameEnginer = null;
		dispose();
	}

	private void lbQtdMouseDown(MouseEvent evt)
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

	private void lbQtdKeyPressed(KeyEvent evt)
	{
		if (evt.keyCode == 13 || evt.keyCode == SWT.KEYPAD_CR)
		{
			//int control = ((evt.stateMask & SWT.CONTROL) != 0) ? 1 : 0;
			//int shift = ((evt.stateMask & SWT.SHIFT) != 0) ? 1 : 0;

			if (l2Item != null)
			{
				 //gameEnginer.sendUseItem(l2Item.objectId);
			}

		}

	}

	private void dropItem()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					int cnt=0;
					if(l2Item.itemCount > 1)
					{
						InputNumberDialog dialog  = new	InputNumberDialog(getShell());
						cnt = dialog.open();

						//if(cnt == 1)
						//	gameEnginer.sendDropItem(l2Item.objectId, cnt,gameEnginer.getSelfChar().getX(),gameEnginer.getSelfChar().getY(),gameEnginer.getSelfChar().getZ() );
					}else{
						//gameEnginer.sendDropItem(l2Item.objectId, 1,gameEnginer.getSelfChar().getX(),gameEnginer.getSelfChar().getY(),gameEnginer.getSelfChar().getZ());
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
	}

	private void deleteItem()
	{
		final MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
		messageBox.setText("Exit");
		messageBox.setMessage("Are you sure?");

		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					Integer cnt=0;

					if(l2Item.itemCount > 1 && messageBox.open() == SWT.OK )
					{
						InputNumberDialog dialog  = new	InputNumberDialog(getShell());
						cnt = dialog.open();

						//if(cnt != null)
						//	gameEnginer.sendDeleteItem(l2Item.objectId, cnt);
					}

					if(l2Item.itemCount == 1 && messageBox.open() == SWT.OK)
					{
						//gameEnginer.sendDeleteItem(l2Item.objectId, 1);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
