package fw.gui.game_canvas;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import fw.game.GameEngine;

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
public class GameCanvas extends Canvas implements MouseListener,MouseMoveListener {
	private static final Logger _log = Logger.getLogger(GameCanvas.class
			.getName());

	Image offScreenImg = null, offScreenImgOld = null;;
	boolean isDisposed = false;
	GameCanvasDrawInterface drawInterface = null;
	boolean blockPaint = false;
	boolean delayedRedraw = false;
	AtomicBoolean blockDraw = new AtomicBoolean();
	boolean enableDraw = false;

	public GameCanvas(Composite parent, int style) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.addMouseMoveListener(this);
		this.addMouseListener(this);
		this.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent evt) {
				rootControlResized(evt);
			}
		});

		this.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent evt) {
				rootPaintControl(evt);
			}
		});

		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent evt) {
				rootWidgetDisposed(evt);
			}
		});

		new FrameControl(this).start();
	}

	private boolean drawFrame() {
		if (offScreenImgOld != null) {
			offScreenImgOld.dispose();
			offScreenImgOld = null;
		}

		if (offScreenImg == null || drawInterface == null || blockDraw.get())
			return false;
		GC gc = new GC(offScreenImg);

		blockPaint = true;
		boolean ret = drawInterface.drawFrame(gc);
		blockPaint = false;

		gc.dispose();
		return ret || delayedRedraw;
	}

	private void rootControlResized(ControlEvent evt) {
		blockDraw.set(true);
		if (offScreenImg != null) {
			offScreenImgOld = offScreenImg;
		}
		try {
			Rectangle bounds = getBounds();
			offScreenImg = new Image(getDisplay(), bounds);

			if (drawInterface != null)
				drawInterface.setFrameBounds(bounds);
		} catch (Exception e) {
			offScreenImg = null;
			_log.warning("Error set resize canvas: " + e.getMessage());
		}
		blockDraw.set(false);
	}

	private void rootPaintControl(PaintEvent evt) {
		if (!enableDraw)
			return;

		if (blockPaint) {
			delayedRedraw = true;
			return;
		}

		delayedRedraw = false;

		GC gc = evt.gc;
		try {
			if (offScreenImg != null)
				gc.drawImage(offScreenImg, 0, 0);
		} catch (Exception e) {
			_log.warning("Error draw buff image: " + e.getMessage());
		}
	}

	private void rootWidgetDisposed(DisposeEvent evt) {
		isDisposed = true;
		if (drawInterface != null)
			drawInterface.dispose();
		if (offScreenImg != null)
			offScreenImg.dispose();
		if (offScreenImgOld != null)
			offScreenImgOld.dispose();
	}

	public void setDrawInterface(GameCanvasDrawInterface drawInterface) {
		enableDraw = true;
		this.drawInterface = drawInterface;
		drawInterface.setFrameBounds(getBounds());
	}

	class FrameControl extends Thread {
		GameCanvas gameCanvas;

		public FrameControl(GameCanvas gameCanvas) {
			this.gameCanvas = gameCanvas;
		}

		@Override
		public void run() {
			long frameTime = 1000 / 50;

			while (!gameCanvas.isDisposed) {
				try {
					long iniTime = System.currentTimeMillis();

					if (gameCanvas.isEnableDraw() && gameCanvas.drawFrame()) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								try {
									gameCanvas.redraw();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}

					long endTime = System.currentTimeMillis();
					long sleepTime = frameTime - (endTime - iniTime);

					if (sleepTime < 0) {
						// System.out.println("frame draw time is too long " +
						// (endTime - iniTime));
					} else {
						sleep(sleepTime);
					}
				} catch (InterruptedException e) {
				}
			}
		}

	}

	public boolean isEnableDraw() {
		return enableDraw;
	}

	public void setEnableDraw(boolean enableDraw) {
		this.enableDraw = enableDraw;
	}

	public void mouseDoubleClick(MouseEvent evt) {
		this.drawInterface.onMouseDoubleClick(evt);
	}

	public void mouseDown(MouseEvent evt) {
		this.drawInterface.onMouseDown(evt);
	}

	public void mouseUp(MouseEvent evt) {
		this.drawInterface.onMouseUp(evt);
	}

	public void mouseMove(MouseEvent evt) {		
		this.drawInterface.onMouseMove(evt);
	}

}
