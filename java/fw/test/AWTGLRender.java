package fw.test;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JComponent;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.newdawn.slick.Color;

import fw.common.ThreadPoolManager;
import fw.extensions.util.Rnd;
import fw.game.GameEngine;
import fw.game.L2World;
import fw.game.model.L2Object;
import fw.game.model.L2Player;
import fw.gui.game_canvas.L2MapCalc;
import static org.lwjgl.opengl.GL11.*;

public class AWTGLRender extends AWTGLCanvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final Logger _log = Logger.getLogger(AWTGLRender.class
			.getName());

	/** A base vars */
	private int current_width;
	private int current_height;
	private boolean _enabled = true;
	private JComponent parent;
	private AWTGLPainter _canvas = new AWTGLPainter();
	
	private	volatile float angle;
	
	private GameEngine _game;
	
	private Color cPlayer = Color.red;
	private int map_center_x = 0, map_center_y = 0;

	/*
	 * public AWTGLRender() throws LWJGLException { super();
	 * //setIgnoreRepaint(true); setBackground(Color.BLACK);
	 * ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 100,
	 * 330); }
	 */

	public AWTGLRender(JComponent parent) throws LWJGLException {
		super();
		this.parent = parent;
		this.addComponentListener(new ComponentListener() {			
			@Override
			public void componentShown(ComponentEvent arg0) {
				setEnabled(true);				
			}			
			@Override
			public void componentResized(ComponentEvent evt) {}			
			@Override
			public void componentMoved(ComponentEvent arg0) {}			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				setEnabled(false);					
			}
		});
		//this.parent.add
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 100,50); 
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
				
				map_center_x = current_width/2;
				map_center_y = current_height/2;
				
				_log.info("Resize VP: "+current_width+" "+current_height);
						
				glViewport(0, 0, current_width, current_height);		
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0, current_width, current_height, 0, 1,-1 );
				glDisable(GL_DEPTH_TEST);
				glMatrixMode(GL_MODELVIEW);
				
				//glEnable(GL_LINE_SMOOTH);
				//glHint(GL_LINE_SMOOTH_HINT, GL_FASTEST);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				repaint();
				
			}
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);
			
			glColor3f(1f, 1f, 1f);
			_canvas.MoveTo(0, 0);
			/*_canvas.LineTo(50, 50);
			glColor3f(1f, 0.0f, 1f);
			_canvas.LineToRel(0, 50);
			glColor3f(1f, 1f, .0f);
			//_canvas.FrameRect(current_width/2, current_width/2+5, current_height/2, (current_height/2)+5);
			_canvas.MoveTo(current_width/2, current_height/2);			
			cPlayer.bind();
			_canvas.EllipseRel(40);
			
			Color.green.bind();
			_canvas.MoveTo(current_width/3+Rnd.get(1, 20), current_height/3);
			_canvas.RectRel(Rnd.get(10, 20));*/
			//glColor3f(1f, .0f, .0f);
			//_canvas.FrameRect(current_width-5, current_width, current_height-5, current_height);
			//_canvas.stopPrimitive();
			try{
				renderL2();
			}catch(Exception e){e.printStackTrace();}
			
			/*glPushMatrix();
			glColor3f(1f, 1f, 0f);
			glTranslatef(current_width / 2.0f, current_height / 2.0f, 0.0f);
			glRotatef(angle, 0f, 0f, 1.0f);
			glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
			glPopMatrix();*/
			//glFlush();
			_canvas.stopPrimitive();
			swapBuffers();
			//repaint();
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}

	private int toX(int x){
		//Round(cx + ((x - zx)/MAPBLOCKSIZEDIV900)/scale);
		return (int) (map_center_x + ((x-getMyChar().getX())/L2MapCalc.MAPBLOCKSIZEDIV900/*/_scale*/));
	}
	private int toY(int y){
		//Round(cx + ((x - zx)/MAPBLOCKSIZEDIV900)/scale);
		return (int) (map_center_y + ((y-getMyChar().getY())/L2MapCalc.MAPBLOCKSIZEDIV900/*/_scale*/));
	}
	
	private void renderL2() {
		
		ArrayList<L2Object> _list = getWorld().getObjectList();
		for (L2Object obj:_list) {
			if(obj.isPlayer()){
				cPlayer.bind();
				_canvas.RectRel(5);
			}
		}
		
	}

	public void run() {
		if (!isEnabled()) 
			return;		
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
	}

	public L2World getWorld() {
		return _game.getWorld();
	}
	public L2Player getMyChar() {
		return _game.getSelfChar();
	}

	public void setGame(GameEngine game) {
		this._game = _game;
	}

}
