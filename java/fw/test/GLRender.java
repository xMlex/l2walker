package fw.test;

import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

import fw.com.swtdesigner.SWTResourceManager;
import fw.extensions.util.Rnd;

public class GLRender implements Runnable {

	private Canvas _canvas;

	private GLData data = new GLData();
	private GLCanvas canvas;
	private GLRender _instance;
	private ArrayList<IGLRenderListener> _listeners = new ArrayList<IGLRenderListener>();

	public GLRender(Composite comp, Object ldata) {
		this._canvas = new Canvas(comp, SWT.NONE);
		_canvas.setLayoutData(ldata);
		//_canvas.setBackground(SWTResourceManager.getColor(50, 50, 50));

		this._canvas.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				resizeScene();
			}
			public void controlMoved(ControlEvent e) {}
		});
		_instance = this;
		data.doubleBuffer = true;
		canvas = new GLCanvas(comp, SWT.NONE, data);
		
		canvas.setParent(_canvas);
		canvas.setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		canvas.addListener(SWT.Resize, new Listener() {

			public void handleEvent(Event event) {
				Rectangle bounds = canvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				canvas.setCurrent();
				try {
					GLContext.useContext(canvas);
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				//GL11.glViewport(0, 0, bounds.width, bounds.height);
				/*GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();*/

				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, bounds.width, bounds.height, 0, 1, -1);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				
				for (IGLRenderListener _l : _listeners)
					_l.onRsize(bounds.width, bounds.height);
				System.out.println("Resize: " + bounds.width + " "
						+ bounds.height);
			}
		});

		/*GL11.glClearColor(.0f, .0f, .0f, 1.0f);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glClearDepth(1.0);
		//GL11.glLineWidth(8f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_F);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		//GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_FASTEST);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
*/
		canvas.addListener(SWT.Paint, new Listener() {

			public void handleEvent(Event event) {
				_instance.run();
			}
		});
		this._canvas.addPaintListener(new PaintListener() {
			
			public void paintControl(PaintEvent e) {
				//e.gc.setForeground(SWTResourceManager.getColor(225, 0, 0));
				//e.gc.fillRectangle(0, 0, e.width, e.height);
				_instance.run();
			}
		});
		Display.getCurrent().asyncExec(this);
	}

	public void run() {
		if (!canvas.isDisposed()) {
			canvas.setCurrent();
			try {
				GLContext.useContext(canvas);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
				GL11.glClearColor(.0f, .0f, .0f, 0.5f);
			
			//GL11.glClearColor(.0f, .0f, .0f, 1.0f);
			GL11.glColor3f(0.5f,0.5f,1.0f);
			//Color.green.bind();
		
			
			GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(100,100);
		    GL11.glVertex2f(100+200,100);
		    GL11.glVertex2f(100+200,100+200);
		    GL11.glVertex2f(100,100+200);
		    GL11.glEnd();
			
			//onRenred();
			canvas.swapBuffers();
			Display.getCurrent().asyncExec(this);
			//canvas.getDisplay().timerExec(50, this);
		}
	}

	public void onRenred() {
		try {
			for (IGLRenderListener _l : _listeners)
				_l.onRender(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final GLCanvas getCanvas() {
		return canvas;
	}

	public void addListener(IGLRenderListener e) {
		_listeners.add(e);
	}
	
	protected void resizeScene() {
		Rectangle rect = this._canvas.getClientArea();
		this.canvas.setSize(rect.width, rect.height);
	}

}
