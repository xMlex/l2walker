package fw.gui.game_canvas;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;


public class GameDrawObject {
	private String title = "NONE";
	private int x = 10,y = 10;
	private Color color = null;
	
	public void drawSelf(GC gc){
		if(color != null)
			gc.setForeground(color);
		gc.drawRectangle(x, y, 5, 5);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
