package fw.test;

import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.JComponent;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import fw.common.ThreadPoolManager;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class AWTGLRender extends AWTGLCanvas implements Runnable {

	

	private static final long serialVersionUID = 1L;

	private static final Logger _log = Logger.getLogger(AWTGLRender.class
			.getName());

	/** A base vars */
	private int current_width;
	private int current_height;
	private boolean _enabled = true;
	private JComponent parent;
	
	private	volatile float angle;

	/*
	 * public AWTGLRender() throws LWJGLException { super();
	 * //setIgnoreRepaint(true); setBackground(Color.BLACK);
	 * ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 100,
	 * 330); }
	 */

	public AWTGLRender(JComponent parent) throws LWJGLException {
		super();
		this.parent = parent;
		//this.parent.add
	}
	
	public void paintGL() {
		try {
			if(!_enabled){ 
				//_log.info("not render");
				return;			
			}
			angle += 1.0f;
			if (getWidth() != current_width || getHeight() != current_height) {
				current_width = getWidth();
				current_height = getHeight();
				glViewport(0, 0, current_width, current_height);
				setSwapInterval(5);
				glViewport(0, 0, getWidth(), getHeight());				
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				gluOrtho2D(0.0f, (float) current_width, 0.0f, (float) current_height);
				glMatrixMode(GL_MODELVIEW);
				
			}
			glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);
			glPushMatrix();
			glColor3f(1f, 1f, 0f);
			glTranslatef(current_width / 2.0f, current_height / 2.0f, 0.0f);
			glRotatef(angle, 0f, 0f, 1.0f);
			glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
			glPopMatrix();
			swapBuffers();
			repaint();
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}

	public void run() {
		try {
			synchronized (this) {
				repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean _enabled) {
		this._enabled = _enabled;
		repaint();
	}

}
