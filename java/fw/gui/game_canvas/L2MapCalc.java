package fw.gui.game_canvas;

import java.util.logging.Logger;

import fw.game.model.L2Object;

public class L2MapCalc {

	private static final Logger _log = Logger.getLogger(L2MapCalc.class.getName());
	
	/** Размер одного блока */
	public final static int MAPBLOCKSIZE= 32768;
	/** Размер одного блока разделенный на 3 */
	public final static int MAPBLOCKSIZEDIV3 = MAPBLOCKSIZE / 3;
	public final static int MAPBLOCKSIZEDIV900 = MAPBLOCKSIZE / 900;
	/** Корректировка координат по X */
	public final static int MAP_CORRECT_X= 131035;
	/** Корректировка координат по Y */
	public final static int MAP_CORRECT_Y= 262053;
	
	private int map_img_width = 900;
	private int map_img_height = 900,
			mapVpheight = 0,mapVpwidth = 0,mapScale = 1;
		
	private int vMapBlockSizeX,vMapBlockSizeY,m;
	
	private final L2Object _char;
	
	public L2MapCalc(L2Object mychar) {
		this._char = mychar;
	}
	public void setMapSize(int w, int h){
		map_img_width = w;
		map_img_height = h;
	}
	public void setVpSize(int w, int h){
		mapVpwidth = w;
		mapVpheight = h;
	}
	public void setMapScale(int scale) {
		mapScale = scale;
		m = mapScale*mapScale;
		vMapBlockSizeX = (mapVpwidth*m) / 200;
		vMapBlockSizeY = (mapVpheight*m) / 200;
	}
	
	private static int GoodDiv(int a, int b){
		return (a + (a >> 31)) / b - (a >> 31);
		//return a/b;		
	}
	
	public static int getBlockX(int x){
		//return (x + MAPBLOCKSIZE * 18) / MAPBLOCKSIZE;
		return (x+MAP_CORRECT_X) / MAPBLOCKSIZE;
	}
	public static int getBlockY(int y){
		//return (y + L2MapCalc.MAPBLOCKSIZE * 20) / L2MapCalc.MAPBLOCKSIZE;
		return (y+MAP_CORRECT_Y) / MAPBLOCKSIZE;
	}
	
	
	
	// BASE CALC 
	
	public int getMapXToReal(int pos){
		//Result:= self.myRealX + Round((x - Self.mapWidth / 2)* Self.mapScale * MAPBLOCKSIZEDIV900);
		return _char.getX()+((pos-map_img_width/2)*MAPBLOCKSIZEDIV900);
	}
	public int getMapYToReal(int pos){
		//Result:= self.myRealY + Round((y - Self.mapHeight / 2)* Self.mapScale * MAPBLOCKSIZEDIV900);
		return _char.getY()+((pos-map_img_height/2)*MAPBLOCKSIZEDIV900);
	}
	
	/** Вычисляем номер блока */
	public static int getXBlock(int xPos)
	{
		return (xPos+MAP_CORRECT_X) / MAPBLOCKSIZE;
	}
	/** Вычисляем номер блока с учетом корректировки(для номера файла)*/
	public static int getXBlockCorrect(int xPos){
		return getXBlock(xPos)+16;
	}
	/** Вычисляем координату внутри блока */
	public static int getXInBlock(int xPos){
		int xBlock = getXBlock(xPos);
		int xBlockPos = ((xPos+MAP_CORRECT_X) - xBlock*MAPBLOCKSIZE) / MAPBLOCKSIZEDIV3;	
		//_log.info("Map x in block: "+xBlockPos);
		return xBlockPos;
	}
	/** Вычисляем смешение внутри маленького блока */
	public static int getXInSmallBlock(int xPos){
		int xBlock = getXBlock(xPos);
		int xBlockPos = getXInBlock(xPos);
		return  ((xPos+MAP_CORRECT_X) - (xBlock+xBlockPos)*MAPBLOCKSIZEDIV3) / MAPBLOCKSIZEDIV900 - 300;
	}
	
	/** Вычисляем смешение внутри маленького блока */
	public  int getXInSmallBlockMap(int xPos){
		int xBlock = getXBlock(xPos);
		int xBlockPos = getXInBlock(xPos);
		return  ((xPos+MAP_CORRECT_X) - (xBlock+xBlockPos)*MAPBLOCKSIZEDIV3) / MAPBLOCKSIZEDIV900 - 300;
	}
	
	
	
	/** Вычисляем номер блока */
	public static int getYBlock(int yPos){
		return (yPos+MAP_CORRECT_Y) / MAPBLOCKSIZE;
	}
	/** Вычисляем номер блока с учетом корректировки(для номера файла)*/
	public static int getYBlockCorrect(int yPos){
		return getYBlock(yPos)+10;
	}
	/** Вычисляем координату внутри блока */
	public static int getYInBlock(int yPos){
		int yBlock = getYBlock(yPos);
		int yBlockPos = ((yPos+MAP_CORRECT_Y) - yBlock*MAPBLOCKSIZE) / MAPBLOCKSIZEDIV3;
		//_log.info("Map y in block: "+yBlockPos);
		return yBlockPos;
	}
	/** Вычисляем смешение внутри маленького блока */
	public static int getYInSmallBlock(int yPos){
		int yBlock = getYBlock(yPos);
		int yBlockPos = getXInBlock(yPos);
		return  ((yPos+MAP_CORRECT_Y) - (yBlock*3+yBlockPos)*MAPBLOCKSIZEDIV3) / MAPBLOCKSIZEDIV900 + 300;
	}
	
	public static void main(String[] args)
	{
		int x = -86089,y=241148,z=-3555;
		_log.info("Map x: "+getXBlockCorrect(x));
		_log.info("Map x small block: "+getXInSmallBlock(x));
		_log.info("Map y: "+getYBlockCorrect(y));
		_log.info("Map y small block: "+getYInSmallBlock(y));
		_log.info("Good div 10/3: "+GoodDiv(10, 3));
	}	
	
	class L2Block{
		public int x=0,y=0;
		public L2Block(int _x, int _y){
			x=_x; y=_y;
		}
		public L2Block(){}		
	}	
	
}
