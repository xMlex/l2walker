package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.L2Skill;
import fw.gui.mainArea.efects.VisualEfectsTimer;
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
public class SkillArea extends ScrolledComposite
{
	private FastMap<SkillAreaItem, SkillAreaItem> skillAreaItens = null;
	private Composite cpRows;

	public SkillArea(Composite parent, int style)
	{
		super(parent, SWT.V_SCROLL);
		skillAreaItens = new FastMap<SkillAreaItem, SkillAreaItem>();
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

	public SkillAreaItem newItem(GameEngine gameEnginer,VisualEfectsTimer visualEfectsTimer, L2Skill l2Skill)
	{
		cpRows.setRedraw(false);
		SkillAreaItem skillAreaItem = new SkillAreaItem(cpRows, SWT.BORDER);

		GridData skillAreaItemData = new GridData();
		skillAreaItemData.heightHint = 22;
		skillAreaItemData.horizontalAlignment = GridData.FILL;
		skillAreaItemData.verticalAlignment = GridData.BEGINNING;
		skillAreaItemData.grabExcessHorizontalSpace = true;		
		skillAreaItem.setLayoutData(skillAreaItemData);

		skillAreaItem.setL2Skill(gameEnginer,visualEfectsTimer, l2Skill);
		skillAreaItens.put(skillAreaItem, skillAreaItem);

		cpRows.layout();
		cpRows.pack();
		
		Rectangle bounds = getClientArea();
		Rectangle cpRowsBounds = cpRows.getBounds();
		cpRowsBounds.width = bounds.width;
		cpRows.setBounds(cpRowsBounds);
		
		cpRows.setRedraw(true);
		cpRows.redraw();  
		return skillAreaItem;
	}

	public void removeItem(SkillAreaItem skillAreaItem)
	{
		skillAreaItens.remove(skillAreaItem);
		skillAreaItem.dispose();
		
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
		 for (Iterator<SkillAreaItem> iter = skillAreaItens.values().iterator(); iter.hasNext();)
		{
			SkillAreaItem skillAreaItem = (SkillAreaItem) iter.next();			
			skillAreaItem.removeL2Skill();
		}		
	}

}
