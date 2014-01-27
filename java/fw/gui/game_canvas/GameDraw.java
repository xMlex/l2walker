package fw.gui.game_canvas;

import java.util.Map.Entry;
import java.awt.Polygon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import fw.com.swtdesigner.SWTResourceManager;
import fw.connection.game.clientpackets.MoveBackwardToLocation;
import fw.extensions.util.Location;
import fw.game.GameEngine;
import fw.game.model.L2Character;
import fw.game.model.L2Object;
import fw.game.model.instances.L2NpcInstance;

public class GameDraw implements GameCanvasDrawInterface {
	public Color bgColor = SWTResourceManager.getColor(0, 0, 0);
	public Color defColor = SWTResourceManager.getColor(0, 225, 0);
	public Color fontColor = SWTResourceManager.getColor(200, 255, 50);

	public Color colorNpc = SWTResourceManager.getColor(255, 255, 0);
	public Color colorMob = SWTResourceManager.getColor(0, 255, 0);
	public Color colorChar = SWTResourceManager.getColor(100, 200, 0);
	public Color colorPlayer = SWTResourceManager.getColor(0, 0, 225);
	public Font baseFont = SWTResourceManager.getFont("Tahoma", 8, 0, false,
			false);
	GameCanvas gameCanvas;
	GameEngine gameEngine;

	private Image _map = null;

	// MAP
	private L2MapCalc _mapCalc;
	private int _cur_map_x = 0, _cur_map_y = 0;
	private int _x = -86089, _y = 241148, _z = -3537;
	private int char_map_pos_x = 0, char_map_pos_y = 0;
	private int map_center_x = 0, map_center_y = 0;
	private int _scale = 1;
	private long _lastLocUpdate=0;

	private boolean _r_chars = true, _r_drop = true, _r_npc = true,
			_r_move = true;

	Rectangle bounds = null;
	int pos = 0;
	boolean isDisposed = false;
	
	// new map
	private Location MapCachePos = new Location(),
			mapImagePos = new Location(),MeLoc,
					mapStartLocation = new Location();
	
	private Image mapImage = null,bmpBuf = null;
	private int mapWidth=900, mapHeight=900,fViewStretch=1,
			fViewRectLeft=0,fViewRectTop=0;
	private int Scale = 1;
	private final double cWorldParameter = 0.0625;
	
	private Polygon _pol = new Polygon();

	public GameDraw(GameCanvas gameCanvas, GameEngine ge) {
		this.gameCanvas = gameCanvas;
		this.gameEngine = ge;
		
		bmpBuf = new Image(Display.getCurrent(), mapWidth, mapHeight);
		
		_mapCalc = new L2MapCalc();
		
		_pol.addPoint(10, 10);
		_pol.addPoint(50, 40);
		_pol.addPoint(100, 90);
		_pol.addPoint(15, 15);
		//_pol.addPoint(15, 15);
	}
		
	public void dispose() {
		isDisposed = true;
	}

	public void setFrameBounds(Rectangle bounds) {
		this.bounds = bounds;
		MapCachePos.x = bounds.width / 2;
		MapCachePos.y = bounds.height / 2;
	}

	public boolean drawFrame(GC gc) {
		if (gameEngine.getSelfChar() == null)
			return false;

		calculateMap(gc);
		//synchronized (gameEngine.getWorld().getObjects()) {
			
		
		/*for (Entry<Integer, L2Object> obj : gameEngine.getWorld().getObjectList()) {			
			L2Object _o = obj.getValue();
			updateMove(_o);
			if(_o == null) continue;
			gc.setForeground(defColor);
			if (_o.isNpc()){
				gc.setForeground(colorNpc);
				if( ((L2NpcInstance)_o).isAttackable() )
					gc.setForeground(colorMob);
			}
				
			if (_o.isPlayer())
				gc.setForeground(colorPlayer);
			if (_o.isChar())
				gc.setForeground(colorChar);
			gc.drawRectangle(toX(_o.getX()),toY(_o.getY()), 5, 5);
			gc.setForeground(defColor);
			if(_o.getToX() != 0)
			gc.drawLine(toX(_o.getX()),toY(_o.getY()), toX(_o.getToX()),toY(_o.getToY()));
		}*/
		//}

		gc.setForeground(fontColor);
		//gc.drawRectangle(map_center_x, map_center_y, 5, 5);		
		_lastLocUpdate = System.currentTimeMillis();
		gc.drawText("x: " + _x + " y: " + _y+" Time: "+_lastLocUpdate, 0, 0, true);
		return true;
	}

	private int toX(int x){
		//Round(cx + ((x - zx)/MAPBLOCKSIZEDIV900)/scale);
		return (int) (map_center_x + ((x-_x)/L2MapCalc.MAPBLOCKSIZEDIV900/_scale));
	}
	private int toY(int y){
		//Round(cx + ((x - zx)/MAPBLOCKSIZEDIV900)/scale);
		return (int) (map_center_y + ((y-_y)/L2MapCalc.MAPBLOCKSIZEDIV900/_scale));
	}
	
	private void calculateMap(GC gc) {			
		
		_mapCalc.setVpSize(bounds.width, bounds.height);
		_mapCalc.setScale(_scale);
		_mapCalc.setMapSize(900, 900);
		
		_x = gameEngine.getSelfChar().getX();
		_y = gameEngine.getSelfChar().getY();
		_z = gameEngine.getSelfChar().getZ();
		_mapCalc.setMyLoc(_x, _y);
		

		map_center_x = bounds.width / 2;
		map_center_y = bounds.height / 2;

		
		gc.setBackground(bgColor);
		gc.setForeground(defColor);
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		chekMap();
		if (_map != null) {
			try{
			gc.drawImage(_map, _mapCalc.getMapVpPosX(), _mapCalc.getMapVpPosY());
			}catch(Exception e){e.printStackTrace();}
		}
		gc.setAntialias(SWT.ON);
		gc.setTextAntialias(SWT.ON);
		gc.setFont(baseFont);
		
		drawPolugon(gc, _pol);
		
		gc.drawText("map pos x: " + char_map_pos_x + " y: " + char_map_pos_y, 0, 10, true);
		gc.drawText("pos char x: " + _x + " y: " + _y+" z: "+_z, 0, 20, true);
		//gc.drawText("pos in small block x: " + L2MapCalc.getXInSmallBlock(_x) + " y: " + L2MapCalc.getYInSmallBlock(_y), 0, 30, true);
	}

	private void updateMove(L2Object obj) {
		if(!(obj instanceof L2Character)) 
			return;
		if(true) return;
		L2Character _o = (L2Character)obj;
		
		double dx = _o.getToX()-_o.getX();
		double dy = _o.getToY()-_o.getY();
		double dz = _o.getToZ()-_o.getZ();
		double axy = Math.atan2(dy,dx+0.001); // 0.001 - anti div 0
		double h = Math.round(axy * 32768 / Math.PI);
		double rxy = Math.sqrt(dy*dy+dx*dx);
		double speed = _o.getRunSpeed();
		double d = (System.currentTimeMillis()-_lastLocUpdate)*1; 
		speed = speed*d*_o.getMovementSpeedMultiplier();
		double az = Math.atan2(dz,rxy);
		
		if( rxy<speed){
			_o.setXYZ(_o.getToX(), _o.getToY(), _o.getToZ());
			_o.setToLoc(_o.getLoc());
		}else{
			_o.setXYZ(
					(int)(_o.getX()+speed*Math.cos(axy)),
					(int)(_o.getY()+speed*Math.sin(axy)), 
					(int)(_o.getZ()+speed*Math.tan(az))
					);
		}
		
	}

	private void chekMap() {

		if (_cur_map_x != _mapCalc.xBlock
				|| _cur_map_y !=_mapCalc.yBlock) {

			_cur_map_x = _mapCalc.xBlock;
			_cur_map_y = _mapCalc.yBlock;
			if (_cur_map_x <= 0 || _cur_map_y <= 0) {
				System.out.println("Load map error: block x: " + _cur_map_x
						+ " y:" + _cur_map_y + " Pos: " + _x + " y: " + _y);
				return;
			}

			if (_map != null)
				_map.dispose();
			System.out.println("Load map file: data/maps/" + _cur_map_x + "_"
					+ _cur_map_y + ".jpg");
			_map = SWTResourceManager.getImage("data/maps/" + _cur_map_x + "_"
					+ _cur_map_y + ".jpg");
		}
	}

	public void onMouseUp(MouseEvent evt) {
		// TODO Auto-generated method stub

	}

	public void onMouseDown(MouseEvent evt) {
		if (gameEngine.getSelfChar() == null)
			return;
		int x = _mapCalc.MapXtoReal(evt.x);
		int y = _mapCalc.MapYtoReal(evt.y);
		
		_pol.addPoint(x, y);
		//gameEngine.getGameConnection().sendPacket(
		//		new MoveBackwardToLocation(_mapCalc.getMapXToReal(evt.x),
		//				_mapCalc.getMapYToReal(evt.y), _z));		
		
		System.out.println("MoveTo: x "+x+" y: "+y+" Dist: "+gameEngine.getSelfChar().getLoc().distance(x, y));
	}

	public void onMouseDoubleClick(MouseEvent evt) {
		// TODO Auto-generated method stub

	}

	public void onMouseMove(MouseEvent evt) {

	}
	
	public void drawPolugonx(GC gc, Polygon pol){
	
		Polygon _p = new Polygon();
		
		for (int i = 0; i < pol.npoints; i++) 
			_p.addPoint(toX(pol.xpoints[i]), toY(pol.ypoints[i]));
				
		gc.drawPolygon(_p.xpoints);
		gc.drawPolygon(_p.ypoints);
	}
	public void drawPolugon(GC gc, Polygon _p){				
		gc.drawPolygon(_p.xpoints);
		gc.drawPolygon(_p.ypoints);
	}

}
