package fw.test;

import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JComponent;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fw.common.ThreadPoolManager;
import fw.connection.game.clientpackets.MoveBackwardToLocation;
import fw.game.GameEngine;
import fw.game.L2World;
import fw.game.model.L2Object;
import fw.game.model.L2Player;
import fw.game.model.instances.L2NpcInstance;
import fw.gui.game_canvas.L2MapCalc;
import static org.lwjgl.opengl.GL11.*;

public class AWTGLRender extends AWTGLCanvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final Logger _log = Logger.getLogger(AWTGLRender.class
			.getName());

	/** A base vars */
	private int current_width;
	private int current_height;
	private boolean _enabled = false;
	
	private AWTGLPainter _canvas = new AWTGLPainter();
	
	private GameEngine _game;
	
	private Color cPlayer = Color.red, 
			cDef = Color.white, cNpc = Color.yellow, cChar = Color.blue,
			cMob = Color.green, cDrop=Color.orange,
			cCombatLine=Color.pink,
			cCombatPoint=Color.magenta;
	private Font baseFont;
	private TrueTypeFont _font;
	
	private L2MapCalc _mapCalc;
	private Image _curMap = null;
	private int map_center_x = 0, map_center_y = 0,scale=1;
	private int _x = -86089, _y = 241148, _z = -3537;
	
	private boolean _renderMap = true;
	
	private Polygon _combatPoly = new Polygon();
	private ArrayList<L2Point> _points = new ArrayList<L2Point>();
	private Texture texture = null;

	public class L2Point{
		public int x,y;
		public L2Point(int x,int y){
			this.x = x; this.y = y;
		}
	}
	/*
	 * public AWTGLRender() throws LWJGLException { super();
	 * //setIgnoreRepaint(true); setBackground(Color.BLACK);
	 * ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 100,
	 * 330); }
	 */

	public AWTGLRender(JComponent parent) throws LWJGLException {
		super();
		this.addComponentListener(new ComponentListener() {			
			
			public void componentShown(ComponentEvent arg0) {
				setEnabled(true);				
			}			
			
			public void componentResized(ComponentEvent evt) {}			
			
			public void componentMoved(ComponentEvent arg0) {}			
			
			public void componentHidden(ComponentEvent arg0) {
				setEnabled(false);					
			}
		});
		this.addMouseListener(new MouseListener() {		
			
			public void mouseReleased(MouseEvent evt) {}			
			
			public void mousePressed(MouseEvent evt) {}			
			
			public void mouseExited(MouseEvent evt) {}			
			
			public void mouseEntered(MouseEvent evt) {}			
			
			public void mouseClicked(MouseEvent evt) {
				//_log.info("Click: "+evt.getX()+" y: "+evt.getY());
				if(_game.getSelfChar() == null) return;
				
				//_combatPoly.addPoint(_mapCalc.MapXtoReal(evt.getX()), _mapCalc.MapYtoReal(evt.getY()));
				
				//_game.getSelfChar().sendPacket(new MoveBackwardToLocation(
				//		_game.getSelfChar().getX()-(map_center_x - evt.getX())*10, 
				//		_game.getSelfChar().getY()-(map_center_y - evt.getY())*10, 
				//		_game.getSelfChar().getZ()));
				_game.getSelfChar().sendPacket(new MoveBackwardToLocation(
						_mapCalc.MapXtoReal(evt.getX()),
						_mapCalc.MapYtoReal(evt.getY()),
						_game.getSelfChar().getZ()
						));
			}
		});
		//this.parent.add
		_mapCalc = new L2MapCalc();		
		_mapCalc.setMapSize(900, 900);
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 100,200); 
	} 
	
	public void paintGL() {
		try {
			if(!_enabled)
				return;			
			
			if (getWidth() != current_width || getHeight() != current_height) {
				
				current_width = getWidth();
				current_height = getHeight();				
				map_center_x = current_width/2;
				map_center_y = current_height/2;
				_mapCalc.setVpSize(current_width, current_height);
						
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
				//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				
			}
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);
			
			glColor3f(1f, 1f, 1f);
			_canvas.MoveTo(0, 0);
			try{
				renderL2();
			}catch(Exception e){e.printStackTrace();}
					
			_canvas.stopPrimitive();
			swapBuffers();
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void renderL2() {
		
		if(_game.getSelfChar() == null) return;
		calculateMap();
		cDef.bind();
		
		ArrayList<L2Object> _list = getWorld().getObjectList();
		boolean rend = false;
		for (L2Object obj:_list) {
			_canvas.MoveTo(_mapCalc.toMapX(obj.getX()), _mapCalc.toMapY(obj.getY()));		
				
			if(obj.isNpc()){
				rend = true;
				if(((L2NpcInstance)obj).isAttackable())
					cMob.bind();
				else
					cNpc.bind();
			}
			if(obj.isChar()){
				cChar.bind();
				rend = true;
			}
			if(obj.isDrop()){
				rend = true;
				cDrop.bind();
			}
			
			if(rend)
				_canvas.RectRel(2);//EllipseRel(5);
			//if(obj.getToX() != 0 && obj.getToX() != obj.getX()){	
			//	_canvas.MoveTo(_mapCalc.toMapX(obj.getX()), _mapCalc.toMapY(obj.getY()));
			//	_canvas.LineToRel(_mapCalc.toMapX(obj.getToX()), _mapCalc.toMapY(obj.getToY()));
			//}
		}
		cPlayer.bind();
		_canvas.Ellipse(map_center_x, map_center_y, 4);
		
		//cCombatLine.bind();
		//rendPolygon(_combatPoly);
		//cCombatPoint.bind();
		/*for (L2Point _p:_points) {
			_canvas.MoveTo(_mapCalc.toMapX(_p.x), _mapCalc.toMapY(_p.y));
			_canvas.FillRectRel(4);
		}*/
		
	}
	private void rendPolygon(Polygon p){
		if(p.getPointCount()<2) return;
		float[] _p = p.getPoint(0);
		_canvas.MoveTo(_mapCalc.toMapX((int)_p[0]), _mapCalc.toMapY((int)_p[1]));	
		
		for (int i = 1; i < p.getPointCount(); i++){
			 _p = p.getPoint(i);
			_canvas.LineToRel(_mapCalc.toMapX((int)_p[0]), _mapCalc.toMapY((int)_p[1]));
		}
		
	}

	private void calculateMap() {		

		_mapCalc.setScale(scale);			
		
		_x = _game.getSelfChar().getX();
		_y = _game.getSelfChar().getY();
		_z = _game.getSelfChar().getZ();

		_mapCalc.mapPosCalc(_x, _y);

		renderMap();
	}
	
	private void renderMap(){
		if (!_renderMap) return;
		glEnable(GL_TEXTURE_2D);		
		if (_curMap == null) {
			try{				
				_curMap = new Image("./data/maps/"+_mapCalc.xBlock+"_"+_mapCalc.yBlock+".jpg");			
			}catch(Exception e){e.printStackTrace();}
		}
		if(_curMap != null)
			_curMap.draw(_mapCalc.getMapVpPosX(), _mapCalc.getMapVpPosY(),900*scale,900*scale);
		glDisable(GL_TEXTURE_2D);
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
		this._game = game;
		_enabled = true;
	}

	public void setScale(int scale) {
		this.scale = scale;		
	}

}
