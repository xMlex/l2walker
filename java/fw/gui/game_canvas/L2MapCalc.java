package fw.gui.game_canvas;

import java.util.logging.Logger;

import fw.extensions.util.Location;
import xmlex.config.ConfigSystem;

public class L2MapCalc {

	private static final Logger _log = Logger.getLogger(L2MapCalc.class
			.getName());

	/** Размер одного блока */
	public final static int MAPBLOCKSIZE = 32768;
	/** Размер одного блока разделенный на 3 */
	public final static int MAPBLOCKSIZEDIV3 = MAPBLOCKSIZE / 3;
	public final static int MAPBLOCKSIZEDIV900 = MAPBLOCKSIZE / 900;
	/** Корректировка координат по X */
	public final static int MAP_CORRECT_X = 131035;
	/** Корректировка координат по Y */
	public final static int MAP_CORRECT_Y = 262053;

	public int xBlock, yBlock;
	/** Основные позиции */
	private int myX, myY, blockNumber,locX,locY;
	/** размер изображения карты */
	private int map_size_x, map_size_y;
	/** Размер окна через которое смотрим(canvas) */
	private int vp_x, vp_y, map_center_x, map_center_y,
	map_vp_pos_x,map_vp_pos_y;
	private double scale = 1;

	public void setMyLoc(int x, int y) {
		myX = x;
		myY = y;
	}

	public void setMapSize(int w, int h) {
		map_size_x = w;
		map_size_y = h;
	}

	public void setVpSize(int w, int h) {
		vp_x = w;
		vp_y = h;
		map_center_x = GoodDiv(w, 2);
		map_center_y = GoodDiv(h, 2);
	}

	public int toMapXf(float x) {
		return (int)(map_center_x + ((x - myX) / MAPBLOCKSIZEDIV900) * scale);
	}

	public int toMapYf(float y) {		 
		return (int)(map_center_y + ((y - myY) / MAPBLOCKSIZEDIV900) * scale);
	}
	public int toMapY(int y){
		return toMapYf(y);
	}
	public int toMapX(int x){
		return toMapXf(x);
	}
	
	public int MapXtoReal(int x)
	{
		return (int)( myX + ((x - map_center_x )* MAPBLOCKSIZEDIV900) / scale );
	}
	public int MapYtoReal(int y)
	{
		return (int)( myY + ((y - map_center_y )* MAPBLOCKSIZEDIV900) /  scale);
	}


	private void mapBlockCalc(int x,int y){
		
		// Вычисляем номер блока
		xBlock = GoodDiv(x, MAPBLOCKSIZE);
		yBlock = GoodDiv(y, MAPBLOCKSIZE);
		
		// Вычисляем смешение внутри маленького блока
		locX = (x - (xBlock*3)*MAPBLOCKSIZEDIV3) / MAPBLOCKSIZEDIV900 ;
		locY = (y - (yBlock*3)*MAPBLOCKSIZEDIV3) / MAPBLOCKSIZEDIV900 ;
		
		// Корректируем смещения в номерах блоков, Корейцы почему то считаеют не от нуля
		xBlock +=16;
		yBlock +=10;
		//_log.info("xBlock: "+xBlock+" yBlock: "+yBlock);		
	}
	/** Рассчитываем позицию карты на экране с учетом увеличения */
	public void mapPosCalc(int x,int y){
		
		int _x = x + MAP_CORRECT_X;
		int _y = y + MAP_CORRECT_Y;	
		
		myX = x; myY = y;
		
		mapBlockCalc(_x,_y);
		
		setMapVpPosX((int) ( GoodDiv(vp_x, 2) - locX*scale));
		setMapVpPosY((int) ( GoodDiv(vp_y, 2) - locY*scale));
		
		//_log.info("MapVpPosX: "+map_vp_pos_x+" MapVpPosY: "+map_vp_pos_y);
		
	}	
	
	private static int GoodDiv(int a, int b) {
		return (a + (a >> 31)) / b - (a >> 31);
		// return a/b;
	}

	public void setScale(int i) {
		scale = i;		
	}
	public void setScale(double i) {
		scale = i;		
	}

	public int getMapVpPosX() {
		return map_vp_pos_x;
	}

	private void setMapVpPosY(int y) {
		this.map_vp_pos_y = y;
	}
	private void setMapVpPosX(int x) {
		this.map_vp_pos_x = x;
	}

	public int getMapVpPosY() {
		return map_vp_pos_y;
	}
	
	public static void main(String[] args){
		ConfigSystem.load();
		L2MapCalc _mapCalc = new L2MapCalc();
		_mapCalc.setVpSize(300, 300);
		_mapCalc.setScale(10);
		_mapCalc.setMapSize(900, 900);
		_mapCalc.mapPosCalc(-86089, 241148);
		int x = _mapCalc.toMapX(-86089 + 200);
		int y = _mapCalc.toMapY(241148 + 200);
		
		_log.info("map x: "+x+" y: "+y);
		int xr = _mapCalc.MapXtoReal(x);
		int yr = _mapCalc.MapYtoReal(y);
		
		Location _my = new Location(-86089, 241148, 1000);
		Location _res = new Location(xr, yr, 1000);
		
		_log.info("xr: "+xr+" yr: "+yr+" dist: "+_my.distance(_res));
	}
}
