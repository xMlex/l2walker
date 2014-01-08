package fw.gui.l2map;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import fw.com.swtdesigner.SWTResourceManager;
import fw.common.ThreadPoolManager;

public class L2MapCanvas extends Canvas implements ControlListener,DisposeListener,PaintListener,Runnable {

	private static final Logger _log = Logger.getLogger(L2MapCanvas.class.getName());
	
	private Image _buf = null;
	private boolean isDisposed = false;
	private GC _bgc = null;
	
	public L2MapCanvas(Composite parent, int style) {
		super(parent, SWT.DOUBLE_BUFFERED);		
		this.addControlListener(this);
		this.addDisposeListener(this);
		this.addPaintListener(this);
		this.setBackground(SWTResourceManager.getColor(0, 0, 0));	
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 1000, 100);
	}

	public void paintControl(PaintEvent evt) {
		if(_buf == null)
			return;
		evt.gc.drawImage(_buf, 0, 0);
	}

	public void widgetDisposed(DisposeEvent evt) {
		isDisposed = true;			
	}

	public void controlMoved(ControlEvent evt) {}

	public void controlResized(ControlEvent evt) {
		Rectangle bounds = getBounds();
		if(getDisplay() == null ) return;
		_buf = new Image(getDisplay(), bounds);	
		if(_bgc != null)
			_bgc.dispose();
		_bgc = new GC(_buf);
		//_bgc.setBackground(SWTResourceManager.getColor(0, 0, 0));	
	}

	public void run() 
	{
		if(this.isDisposed){
			_log.info("Remove repain event");
			ThreadPoolManager.getInstance().getGeneralScheduledThreadPool().remove(this);
			return;
		}
		if(_buf == null){
			return;
		}
		
		drawFrame();
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					redraw();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	private void drawFrame(){
		_bgc.setBackground(SWTResourceManager.getColor(0, 0, 0));
		_bgc.setForeground(SWTResourceManager.getColor(255, 255, 255));
		_bgc.drawString("String", 10, 10);
		_bgc.drawLine(0, 0, 100, 100);
	}

}
