package fw.gui.mainArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ProgressBar extends Canvas
{
	public enum TextType
	{
		NONE, VALUE, PERCENT
	}

	int maxValue = 100;
	int value = 100;
	TextType textType = TextType.NONE;
	int text_width = 0;
	int text_height = 0;
	Color textColor;

	public ProgressBar(Composite parent, int style)
	{
		super(parent, SWT.DOUBLE_BUFFERED | SWT.NO_FOCUS);
		textColor = getForeground();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			this.setLayout(null);
			this.setSize(113, 64);
			this.addPaintListener(new PaintListener()
			{
				public void paintControl(PaintEvent evt)
				{
					rootPaintControl(evt);
				}
			});
			this.addDisposeListener(new DisposeListener()
			{
				public void widgetDisposed(DisposeEvent evt)
				{
					rootWidgetDisposed(evt);
				}
			});
			this.addControlListener(new ControlAdapter()
			{
				@Override
				public void controlResized(ControlEvent evt)
				{
					rootControlResized(evt);
				}
			});
			this.layout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void rootControlResized(ControlEvent evt)
	{
		// NADA
	}

	private void rootWidgetDisposed(DisposeEvent evt)
	{
		// NADA
	}

	private void rootPaintControl(PaintEvent evt)
	{
		GC gc = evt.gc;
		Rectangle imgBounds = getBounds();
		int relation = 0;

		if (maxValue >= 1)
			relation = (value * imgBounds.width) / maxValue;

		gc.setBackground(getBackground());
		gc.fillRectangle(0, 0, imgBounds.width, imgBounds.height);

		gc.setBackground(getForeground());
		gc.fillRectangle(0, 0, relation, imgBounds.height);
		if (textType == TextType.NONE)
			return;

		gc.setForeground(textColor);
		gc.setFont(this.getFont());

		String text = "";

		if (textType == TextType.VALUE)
			text = value + "/" + maxValue;
		else
		{
			if (maxValue > 0)
				text = String.valueOf((value * 100) / maxValue) + "%";
			else
				text = "0%";
		}

		int text_width = 0;
		int text_height = 0;
		if (text == null || text.length() == 0)
			return;
		for (int i = 0; i < text.length(); i++)
			text_width += gc.getAdvanceWidth(text.charAt(i));
		text_height = gc.getFontMetrics().getHeight();

		gc.drawString(text, (imgBounds.width / 2) - (text_width / 2), (imgBounds.height / 2) - (text_height / 2), true);
	}

	public int getMaxValue()
	{
		return maxValue;
	}

	public void setMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
		redraw();
	}

	public void setValues(int maxValue, int value)
	{
		this.maxValue = maxValue;
		this.value = value;
		redraw();
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
		redraw();
	}

	public TextType getTextType()
	{
		return textType;
	}

	public void setTextType(TextType textType)
	{
		this.textType = textType;
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public void setTextColor(Color textColor)
	{
		this.textColor = textColor;
	}
}
