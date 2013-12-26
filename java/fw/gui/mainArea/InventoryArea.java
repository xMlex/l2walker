package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.L2Item;
import java.util.Iterator;
import javolution.util.FastMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class InventoryArea extends ScrolledComposite
{
	private FastMap<InventoryAreaItem, InventoryAreaItem> InventoryAreaItems = null;
	private Composite cpRows;

	public InventoryArea(Composite parent, int style)
	{
		super(parent, SWT.V_SCROLL);
		InventoryAreaItems = new FastMap<InventoryAreaItem, InventoryAreaItem>();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			this.setLayout(null);
			this.setSize(249, 236);
			this.addControlListener(new ControlAdapter()
			{
				@Override
				public void controlResized(ControlEvent evt)
				{
					rootControlResized(evt);
				}
			});
			{
				cpRows = new Composite(this, SWT.DOUBLE_BUFFERED);
				GridLayout cpRowsLayout = new GridLayout();
				cpRowsLayout.makeColumnsEqualWidth = true;
				cpRowsLayout.marginHeight = 0;
				cpRowsLayout.marginWidth = 0;
				cpRowsLayout.horizontalSpacing = 0;
				cpRowsLayout.verticalSpacing = 0;
				cpRows.setLayout(cpRowsLayout);
				Rectangle bounds = getBounds();
				cpRows.setBounds(0, 0, bounds.width, bounds.height);
				this.setContent(cpRows);
			}
			{
			}

			getVerticalBar().setIncrement(5);
			setAlwaysShowScrollBars(true);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
 
	public void addItems(GameEngine gameEngine, L2Item l2Items[])
	{
		cpRows.setRedraw(false);

		for (int i = 0; i < l2Items.length; i++)
		{
			L2Item l2Item = l2Items[i];

			InventoryAreaItem item = new InventoryAreaItem(cpRows, SWT.BORDER);

			GridData skillAreaItemData = new GridData();
			skillAreaItemData.heightHint = 22;
			skillAreaItemData.horizontalAlignment = GridData.FILL;
			skillAreaItemData.verticalAlignment = GridData.BEGINNING;
			skillAreaItemData.grabExcessHorizontalSpace = true;
			item.setLayoutData(skillAreaItemData);

			item.setL2Item(gameEngine, l2Item);
			
			L2InventoryItemVisualObject visualObject=new L2InventoryItemVisualObject();
			visualObject.inventoryAreaItem=item;
			l2Item.visualObject=visualObject;
			InventoryAreaItems.put(item, item);
		}

		cpRows.layout();
		cpRows.pack();

		Rectangle bounds = getClientArea();
		Rectangle cpRowsBounds = cpRows.getBounds();
		cpRowsBounds.width = bounds.width;
		cpRows.setBounds(cpRowsBounds);

		cpRows.setRedraw(true);
		cpRows.redraw();
	}

	public void updateItems(GameEngine gameEngine, L2Item l2Items[])
	{  		
		for (int i = 0; i < l2Items.length; i++)
		{
			L2Item l2Item = l2Items[i];
			if(l2Item.visualObject!=null)
			{
				((L2InventoryItemVisualObject)l2Item.visualObject).inventoryAreaItem.updateL2Item();
			}
		} 
	}
	
	public void deleteItems(GameEngine gameEngine, L2Item l2Items[])
	{
		cpRows.setRedraw(false);

		for (int i = 0; i < l2Items.length; i++)
		{
			L2Item l2Item = l2Items[i];
			if(l2Item.visualObject!=null)
			{
				((L2InventoryItemVisualObject)l2Item.visualObject).inventoryAreaItem.clearItemAndDispose();
			}
		} 

		cpRows.layout();
		cpRows.pack();

		Rectangle bounds = getClientArea();
		Rectangle cpRowsBounds = cpRows.getBounds();
		cpRowsBounds.width = bounds.width;
		cpRows.setBounds(cpRowsBounds);

		cpRows.setRedraw(true);
		cpRows.redraw();
	}
 
	
	public void removeItem(InventoryAreaItem inventoryAreaItem)
	{
		InventoryAreaItems.remove(inventoryAreaItem);
		inventoryAreaItem.dispose();

		resize();
	}

	private void rootControlResized(ControlEvent evt)
	{
		resize();
	}

	private void resize()
	{
		cpRows.setRedraw(false);
		cpRows.layout();
		cpRows.pack();

		Rectangle bounds = getClientArea();
		Rectangle cpRowsBounds = cpRows.getBounds();
		cpRowsBounds.width = bounds.width;
		cpRows.setBounds(cpRowsBounds);

		cpRows.setRedraw(true);
		cpRows.redraw();
	}

	@Override
	public void setBackground(Color arg0)
	{
		super.setBackground(arg0);
		cpRows.setBackground(arg0);
	}

	@Override
	public void setForeground(Color arg0)
	{
		super.setForeground(arg0);
		cpRows.setForeground(arg0);
	}

	public void removeAll()
	{
		for (Iterator<InventoryAreaItem> iter = InventoryAreaItems.values().iterator(); iter.hasNext();)
		{
			InventoryAreaItem item = (InventoryAreaItem) iter.next();
			item.clearItemAndDispose();
		}
	}

}
