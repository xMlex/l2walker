package fw.test.move;

import java.awt.*;
import java.awt.event.*;

import fw.common.ThreadPoolManager;

public class Start extends Frame {

	private static Start _instance;

	// private MyCanvas _canvas = new MyCanvas();

	public Start() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		this.add(new MyCanvas());
		// _canvas.setBackground(Color.BLACK);

	}

	public static void main(String[] args) {

		_instance = new Start();
		_instance.setSize(800, 600);
		_instance.setVisible(true);
		_instance.setLocationRelativeTo(null);
	}

	class MyCanvas extends Canvas implements Runnable {

		private long _lastTime = 0, _curTime;
		private int _start_x=0,_start_y=0,_end_x = 250,_end_y = 250;
		private int _cur_x=0,_cur_y=0;
		private float speed = 1.1f;

		public MyCanvas() {
			setBackground(Color.GRAY);
			// setSize(300, 300);
			ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this,
					1000, 500);
		}

		public void paint(Graphics g) {
			_curTime = System.currentTimeMillis();
			if (_lastTime == 0)
				_lastTime = _curTime;
			int delta = (int) (_curTime - _lastTime)/100;
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.drawString("It is a custom canvas area", 70, 70);

			//drawBresenhamLine(0, 0, 200, 200, g);
			move(g,delta);
			_lastTime = _curTime;
		}

		@Override
		public void run() {
			repaint();

		}

		public void move(Graphics g,int d) {
			double _nx = _end_x - _cur_x;
            double _ny = _end_y - _cur_y;
            double _distance = Math.sqrt((_nx*_nx) + (_ny*_ny));
            
            if (_distance>d) {
                // Normalize vector (make it length of 1.0)
                double _vx = _nx / _distance;
                double _vy = _ny / _distance;

                // Move object based on vector and speed
                _cur_x += (float)_vx * d;
                _cur_y += (float)_vy * d;
            } else {
                // destination arrived
            	_cur_x = _end_x;
            	_cur_y = _end_x;
            }
            g.drawLine(_start_x, _start_y, _cur_x, _cur_y);
            
		}

		// Этот код "рисует" все 9 видов отрезков. Наклонные (из начала в конец
		// и из конца в начало каждый), вертикальный и горизонтальный - тоже из
		// начала в конец и из конца в начало, и точку.
		private int sign(int x) {
			return (x > 0) ? 1 : (x < 0) ? -1 : 0;
			// возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1,
			// если x > 0.
		}

		public void drawBresenhamLine(int xstart, int ystart, int xend,
				int yend, Graphics g)
		/**
		 * xstart, ystart - начало; xend, yend - конец;
		 * "g.drawLine (x, y, x, y);" используем в качестве "setPixel (x, y);"
		 * Можно писать что-нибудь вроде g.fillRect (x, y, 1, 1);
		 */
		{
			int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

			dx = xend - xstart;// проекция на ось икс
			dy = yend - ystart;// проекция на ось игрек

			incx = sign(dx);
			/*
			 * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0,
			 * т.е. отрезок идёт справа налево по иксу, то incx будет равен -1.
			 * Это будет использоваться в цикле постороения.
			 */
			incy = sign(dy);
			/*
			 * Аналогично. Если рисуем отрезок снизу вверх - это будет
			 * отрицательный сдвиг для y (иначе - положительный).
			 */

			if (dx < 0)
				dx = -dx;// далее мы будем сравнивать: "if (dx < dy)"
			if (dy < 0)
				dy = -dy;// поэтому необходимо сделать dx = |dx|; dy = |dy|
			// эти две строчки можно записать и так: dx = Math.abs(dx); dy =
			// Math.abs(dy);

			if (dx > dy)
			// определяем наклон отрезка:
			{
				/*
				 * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е.
				 * он скорее длинный, чем высокий. Значит в цикле нужно будет
				 * идти по икс (строчка el = dx;), значит "протягивать" прямую
				 * по иксу надо в соответствии с тем, слева направо и справа
				 * налево она идёт (pdx = incx;), при этом по y сдвиг такой
				 * отсутствует.
				 */
				pdx = incx;
				pdy = 0;
				es = dy;
				el = dx;
			} else// случай, когда прямая скорее "высокая", чем длинная, т.е.
					// вытянута по оси y
			{
				pdx = 0;
				pdy = incy;
				es = dx;
				el = dy;// тогда в цикле будем двигаться по y
			}

			x = xstart;
			y = ystart;
			err = el / 2;
			g.drawLine(x, y, x, y);// ставим первую точку
			// все последующие точки возможно надо сдвигать, поэтому первую
			// ставим вне цикла

			for (int t = 0; t < el; t++)// идём по всем точкам, начиная со
										// второй и до последней
			{
				err -= es;
				if (err < 0) {
					err += el;
					x += incx;// сдвинуть прямую (сместить вверх или вниз, если
								// цикл проходит по иксам)
					y += incy;// или сместить влево-вправо, если цикл проходит
								// по y
				} else {
					x += pdx;// продолжить тянуть прямую дальше, т.е. сдвинуть
								// влево или вправо, если
					y += pdy;// цикл идёт по иксу; сдвинуть вверх или вниз, если
								// по y
				}

				g.drawLine(x, y, x, y);
			}
		}

	}

}
