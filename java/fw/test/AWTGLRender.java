package fw.test;

import java.awt.Color;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import fw.common.ThreadPoolManager;
import static org.lwjgl.opengl.GL11.*;

public class AWTGLRender extends AWTGLCanvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final Logger _log = Logger.getLogger(AWTGLRender.class
			.getName());

	/** A base vars */
	private int current_width;
	private int current_height;

	public AWTGLRender() throws LWJGLException {
		super();
		setBackground(Color.BLACK);
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 100,
				100);
	}

	public void paintGL() {

		if (getWidth() != current_width || getHeight() != current_height) {
			current_width = getWidth();
			current_height = getHeight();
			setup();
		}
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(.0f, .0f, .0f, 1.0f);

		
		glColor3f(225, 225, 225);
		glLineWidth(2.5f); 
		glColor3f(1.0f, 0.0f, 0.0f);
		glBegin(GL_LINES);
		glVertex2i(0, 0);
		glVertex2i(50, 50);
		glEnd();
		try {
			
			swapBuffers();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setup() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, current_width, current_height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
