package fw.test;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class AWTGLPainter {

	private static final int cNoPrimitive = Integer.MAX_VALUE;

	private int _lastPrimitive;
	private Point _curPos = new Point();

	public AWTGLPainter(){
		_lastPrimitive = cNoPrimitive;
	}
	
	private void startPrimitive(int primitiv) {
		if (primitiv != _lastPrimitive) {
			if (_lastPrimitive != cNoPrimitive)
				GL11.glEnd();
			if (primitiv != cNoPrimitive)
				GL11.glBegin(primitiv);
			_lastPrimitive = primitiv;
		}
	}

	public void stopPrimitive() {
		startPrimitive(cNoPrimitive);
	}

	public void MoveTo(int x, int y) {
		_curPos.setLocation(x, y);
	}

	public void MoveToRel(int x, int y) {
		_curPos.setLocation(_curPos.getX() + x, _curPos.getY() + y);
	}

	public void LineTo(int x, int y) {
		startPrimitive(GL11.GL_LINES);
		GL11.glVertex2i(_curPos.getX(), _curPos.getY());
		MoveTo(x, y);
		GL11.glVertex2i(_curPos.getX(), _curPos.getY());
	}
	
	public void LineToRel(int x, int y)
	{
	  LineTo(_curPos.getX() + x, _curPos.getY() + y);
	}
	
	public void FrameRect(int x1,int x2, int y1, int y2){	
	  startPrimitive(GL11.GL_LINE_LOOP);
	  GL11.glVertex2i(x1, y1);
	  GL11.glVertex2i(x2, y1);
	  GL11.glVertex2i(x2, y2);
	  GL11.glVertex2i(x1, y2);
	  stopPrimitive();
	}
}
