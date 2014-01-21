package fw.test;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class AWTGLPainter {

	private static final int cNoPrimitive = Integer.MAX_VALUE;

	private int _lastPrimitive;
	public static float DEG2RAD = (float)Math.PI/180.0f;
	private Point _curPos = new Point();

	public AWTGLPainter() {
		_lastPrimitive = cNoPrimitive;
	}

	public void startPrimitive(int primitiveType) {
		if (primitiveType != _lastPrimitive) {
			if (_lastPrimitive != cNoPrimitive)
				GL11.glEnd();
			if (primitiveType != cNoPrimitive)
				GL11.glBegin(primitiveType);
			_lastPrimitive = primitiveType;
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
		startPrimitive(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(_curPos.getX(), _curPos.getY());
		MoveTo(x, y);
		GL11.glVertex2i(_curPos.getX(), _curPos.getY());
	}

	public void LineToRel(int x, int y) {
		//_curPos.setX(_curPos.getX() + x);
		//_curPos.setY(_curPos.getY() + y);
		LineTo(x, y);
	}

	public void Rect(int x1, int x2, int y1, int y2) {
		startPrimitive(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1-1, y1-1);
		GL11.glVertex2i(x2, y1);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x1, y2);
		stopPrimitive();
	}
	public void RectRel(int r){
		//int pols = r/2;
		startPrimitive(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(_curPos.getX()+r, _curPos.getY()+r);
		GL11.glVertex2i(_curPos.getX()+r, _curPos.getY()-r);
		GL11.glVertex2i(_curPos.getX()-r, _curPos.getY()-r);
		GL11.glVertex2i(_curPos.getX()-r, _curPos.getY()+r);
		stopPrimitive();
		//Rect(_curPos.getX()-pols, _curPos.getX()+pols, _curPos.getY()-pols, _curPos.getY()+pols);
	}

	public void FillRect(int x1, int x2, int y1, int y2) {
		startPrimitive(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x2, y1);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x1, y2);
		stopPrimitive();
	}
	public void FillRectRel(int r){
		int pols = r/2;
		FillRect(_curPos.getX()-pols, _curPos.getX()+pols, _curPos.getY()-pols, _curPos.getY()+pols);
	}

	private void EllipseVertices(int x, int y, int r1, int r2) {
		// GL11.glVertex2f(x, y); // вершина в центре круга
		float a;
		/*for (int i = 0; i <= 50; i++) {
			a = (float) (i / 50.0f * Math.PI * 2.0f);
			GL11.glVertex2d(x + Math.cos(a) * r1, y + Math.sin(a) * r2);
		}*/
		for(int i=0; i < 360; i++)
		{
			 //convert degrees into radians
			float degInRad = i*DEG2RAD;
			GL11.glVertex2d(x+Math.cos(degInRad)*r1,y+Math.sin(degInRad)*r2);
		} 
	}

	public void Ellipse(int x, int y, int r) {
		startPrimitive(GL11.GL_LINE_STRIP);
		EllipseVertices(x, y, r, r);
		stopPrimitive();
	}

	public void FillEllipse(int x, int y, int r) {
		startPrimitive(GL11.GL_TRIANGLE_FAN);
		EllipseVertices(x, y, r, r);
		stopPrimitive();
	}

	public void EllipseRel(int r) {
		Ellipse(_curPos.getX(), _curPos.getY(), r);
	}

	public void FillEllipseRel(int r) {
		FillEllipse(_curPos.getX(), _curPos.getY(), r);
	}
	
	public void Polygon(org.newdawn.slick.geom.Polygon p){	
		startPrimitive(GL11.GL_POLYGON);
		for (int i = 0; i < p.getPointCount(); i++) {
			float[] _p = p.getPoint(i);
			GL11.glVertex2f(_p[0],_p[1]);
		}
		stopPrimitive();
	}

}
