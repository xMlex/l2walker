package fw.gui.l2map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.newdawn.slick.*;

import static org.lwjgl.opengl.GL11.*;
import fw.game.GameEngine;
import fw.test.GLRender;
import fw.test.IGLRenderListener;

public class L2MapOpenGL implements IGLRenderListener{

	private Composite comp;
	private Object ldata;
	private GLRender _render = null;
	private GameEngine gameEngine = null;
	
	public L2MapOpenGL(Composite comp, Object data) {	
		this.comp = comp;
		this.ldata = data;
	}

	public void setGameEngine(GameEngine gameEngine) {		
		this.gameEngine = gameEngine;
		_render = new GLRender(comp,ldata);
		_render.addListener(this);
		System.out.println("Loaded OpenGL engine");
	}

	public void onRender(GLRender glRender) {		
		/*Color.green.bind();
		glEnable(GL_LINE);
		glVertex2f(10, 10);
		glVertex2f(50, 50);
		glEnd();*/
		//System.out.println("Render");
	}

	public void onRsize(int w, int h) {
		
	}
	


}
