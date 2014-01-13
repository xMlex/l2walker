package fw.gui.l2map;

import java.awt.Font;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import fw.game.GameEngine;
import fw.gui.game_canvas.L2MapCalc;

public class L2MapOpenGLRender extends BasicGame {

	private TrueTypeFont baseFont;
	
	private GameEngine gameEngine = null;
	
	private int _MouseX,_MouseY;
	private int _ViewCenterX,_ViewCenterY;
	
	private L2MapCalc _mapCalc;
	
	public L2MapOpenGLRender() {
		super("L2MapOpenGLRender");			
	}

	@Override
	public void render(GameContainer parent, Graphics g) throws SlickException 
	{
		if(gameEngine == null) return;
		g.setFont(baseFont);
		g.drawString("mouse: x"+_MouseX+" Y:"+_MouseY, 10, 10);
		g.setColor(Color.green);
	}
	
	public void mouseReleased(int button, int x, int y) 
	{
		_MouseX = x;
		_MouseY = y;
	}
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	 {
		_MouseX = newx;
		_MouseY = newy;		
	 }

	@Override
	public void init(GameContainer canvas) throws SlickException {
		Font awtFont = new Font("Tahoma", Font.PLAIN, 12);
		baseFont = new TrueTypeFont(awtFont, true);	
		System.out.println("Update: "+_ViewCenterX);		
	}

	@Override
	public void update(GameContainer canvas, int delta) throws SlickException 
	{
		_ViewCenterX = canvas.getWidth()/2;
		_ViewCenterY = canvas.getHeight()/2;
		//System.out.println("Update: "+delta);
	}
	
	
	
	public void setGameEngine(GameEngine gameEngine) {		
		this.gameEngine = gameEngine;
	}

}
