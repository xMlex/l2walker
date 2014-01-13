package fw.gui.l2map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.newdawn.slick.*;

import fw.game.GameEngine;

public class L2MapOpenGL{

	
	private CanvasGameContainer _gameContainer = null;
	private L2MapOpenGLRender _render = null;
	
	public L2MapOpenGL(Composite arg0, int arg1) {
		//super(arg0, arg1);
		
		try {
			_render = new L2MapOpenGLRender();
			_gameContainer = new CanvasGameContainer(_render);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		Composite containerComp = new Composite(arg0, SWT.EMBEDDED);
		containerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		java.awt.Frame containerFrame = SWT_AWT.new_Frame(containerComp);
		containerFrame.add(_gameContainer);
		_gameContainer.getContainer().setAlwaysRender(false); 
		_gameContainer.getContainer().setTargetFrameRate(22);
		_gameContainer.getContainer().setShowFPS(false);
		_gameContainer.getContainer().setVSync(false);
		_gameContainer.requestFocus();			
		try {
			_gameContainer.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Loaded OpenGL engine");
	}

	public void setGameEngine(GameEngine gameEngine) {		
		_render.setGameEngine(gameEngine);
	}
	


}
