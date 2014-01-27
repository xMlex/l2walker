package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.model.L2PartyChar;

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
public class PartyArea extends ScrolledComposite
{
	private FastMap<PartyAreaItem, PartyAreaItem> partyAreaItens = null;
	private Composite cpRows;

	public PartyArea(Composite parent, int style)
	{
		super(parent, SWT.V_SCROLL);
		partyAreaItens = new FastMap<PartyAreaItem, PartyAreaItem>();
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
				cpRowsLayout.numColumns = 2;
				cpRows.setLayout(cpRowsLayout);				
				Rectangle bounds = getBounds();
				cpRows.setBounds(bounds);
				this.setContent(cpRows);
			}
			{
			}
			
			getVerticalBar().setIncrement(10);
			setAlwaysShowScrollBars(true);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addPartyChars(GameEngine gameEngine,final L2PartyChar[] l2PartyChar)
	{
		cpRows.setRedraw(false);
				
		for (int i = 0; i < l2PartyChar.length; i++)
		{
			L2PartyChar partyChar=l2PartyChar[i];
			if(partyChar.visualObject!=null)
			{
				((L2PartyCharVisualObject)partyChar.visualObject).partyAreaItem.updateL2PartyChar();
				continue;
			}
			
			L2PartyCharVisualObject visualObject = new L2PartyCharVisualObject();
			visualObject.partyAreaItem = newItem(gameEngine, partyChar);
			partyChar.visualObject = visualObject;			
		}
		
		resize();
	}

	private PartyAreaItem newItem(GameEngine gameEngine, L2PartyChar l2PartyChar)
	{	
		PartyAreaItem partyAreaItem = new PartyAreaItem(cpRows, SWT.BORDER);

		GridData itemData = new GridData();
		itemData.heightHint = 60;
		itemData.horizontalAlignment = GridData.FILL;
		itemData.verticalAlignment = GridData.BEGINNING;
		itemData.grabExcessHorizontalSpace = true;		
		partyAreaItem.setLayoutData(itemData);

		partyAreaItem.setL2PartyChar(gameEngine,l2PartyChar);
		partyAreaItens.put(partyAreaItem, partyAreaItem);	
		return partyAreaItem;
	}

	public void removeItem(PartyAreaItem partyAreaItem)
	{
		partyAreaItens.remove(partyAreaItem);
		partyAreaItem.dispose(); 
		resize();
	}

	private void rootControlResized(ControlEvent evt)
	{ 
		cpRows.setRedraw(false);
		resize();
	}
	
	private void resize()
	{  		
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
		 for (Iterator<PartyAreaItem> iter = partyAreaItens.values().iterator(); iter.hasNext();)
		{
			 PartyAreaItem partyAreaItem = (PartyAreaItem) iter.next();			
			 partyAreaItem.removeL2PartyChar();
		}	
	}

}
