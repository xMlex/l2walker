package fw.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import xmlex.config.ConfigSystem;

public class SlickSWTTest extends BasicGame
{
	private TiledMap map;
	
	private String mapName;
	
	private int clkMapX = 0;
	
	private int clkMapY = 0;
	
	private int clkTileX = 0;
	
	private int clkTileY = 0;
	
	private int hovMapX = 0;

    private int hovMapY = 0;
	
    private int hovTileX = 0;

    private int hovTileY = 0;
	
	private int clickedTileId = 0;
	
	private int hoverTileId = 0;
	
	private double mapXForTile = 0;
	
	private double mapYForTile = 0;
	
	public SlickSWTTest() {
		super("Test painting on a SWT canvas");
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		//map = new TiledMap("data/maps/testTileset.tmx");
				
		// read some properties from map and layer
		mapName = "mapName";// map.getMapProperty("title", "Unknown map name");
	}

	@Override
	public void update(GameContainer container, int delta) 
		throws SlickException {}

	@Override
	public void render(GameContainer container, Graphics g) 
		throws SlickException 
	{
		//map.render(0, 0);
		
		//g.scale(0.35f,0.35f);
		//map.render(1400, 0);
		//g.resetTransform();
		g.setAntiAlias(true);
		g.drawLine(0, 0, 20, 20);
		g.drawString("Map name: " + mapName, 130, 12);
		
		//g.drawString("Map height: " + map.getHeight(), 510, 50);
		//g.drawString("Tile height: " + map.getTileHeight(), 510, 50);
		//g.drawString("Tile width: " + map.getTileWidth(), 510, 70);
		
		g.drawLine(510, 95, 775, 95);
		
		g.drawString("Click map coords: " + clkMapX + ", " + clkMapY, 510, 100);
		g.drawString("Click tile coords: " + clkTileX + ", " + clkTileY, 510, 120);
		
		g.drawString("Hover map coords: " + hovMapX + ", " + hovMapY , 510, 140);
		g.drawString("Hover tile coords: " + hovTileX + ", " + hovTileY, 510, 160);
		
		g.drawString("Clicked tile id " + clickedTileId, 510, 180);
		g.drawString("Hover tile id " + hoverTileId, 510, 200);
		
		g.drawString("MapX edge of current tile: " + mapXForTile, 510, 220);
		g.drawString("MapY edge of current tile: " + mapYForTile, 510, 240);
		
	}
	
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
	}
	
	public void mouseReleased(int button, int x, int y) 
	{
		clkMapX = x;
		clkMapY = y;
		
		clkTileX = 1;//TileMapUtils.getTileXfromMapX(x, map);
		clkTileY = 2;//TileMapUtils.getTileYfromMapY(y, map);
		
		clickedTileId = 0;//map.getTileId(clkTileX, clkTileY, 0);
	}

	
	 public void mouseMoved(int oldx, int oldy, int newx, int newy)
	 {
		 hovMapX = newx;
	     hovMapY = newy;
	     
	     hovTileX = oldx;//TileMapUtils.getTileXfromMapX(newx, map);
	     hovTileY = oldy;//TileMapUtils.getTileYfromMapY(newy, map);
	     
	     hoverTileId = 0;//map.getTileId(hovTileX, hovTileY, 0);
	     
	     mapXForTile = 0.0;//TileMapUtils.getTileXBorderFromMapX(newx, map);
	     mapYForTile = 0.0;//TileMapUtils.getTileYBorderFromMapY(newy, map);
	 }

	
	public static void main(String[] argv) {
		ConfigSystem.load();
		try {
			CanvasGameContainer container = new CanvasGameContainer(new SlickSWTTest());
			     
			Shell shell = new Shell();
			shell.setSize(800,600);
			Display display = shell.getDisplay();
			shell.setLayout(new GridLayout());
			shell.setText("CompositeContainerTest");
			/*
			final Menu mBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(mBar);
			final MenuItem fileItem = new MenuItem(mBar, SWT.CASCADE);
			fileItem.setText("File");
			final Menu fileMenu = new Menu(fileItem);
			fileItem.setMenu(fileMenu);
			for (int i = 0; i < 5; i++) 
			{
				final MenuItem importItem = new MenuItem(fileMenu, SWT.NONE);
				importItem.setText("Item " + i);
			}
			
			
			MenuItem quitItem = new MenuItem(fileMenu,SWT.NONE);
			quitItem.setText("Quit");
			quitItem.addListener(SWT.SELECTED, new Listener() {
				public void handleEvent(Event e)
				{
					System.exit(0);
				}
			});
			*/
			     
			Composite containerComp = new Composite(shell, SWT.EMBEDDED);
			containerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			java.awt.Frame containerFrame = SWT_AWT.new_Frame(containerComp);
			containerFrame.add(container);
			
			container.getContainer().setAlwaysRender(true); 
			container.getContainer().setTargetFrameRate(23);
			container.requestFocus();			
			container.start();
			     
			shell.open();
			while (!shell.isDisposed()) 
			{
				if (!display.readAndDispatch()) 
				{
					display.sleep();
				}
			}
			shell.dispose();   
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}