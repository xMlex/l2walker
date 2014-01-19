package fw.test.move;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import fw.common.ThreadPoolManager;
import fw.game.L2World;
import fw.game.model.GameTimeController;
import fw.game.model.L2Object;
import fw.game.model.instances.L2NpcInstance;

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
		
		GameTimeController.getInstance();
	}

	class MyCanvas extends Canvas implements Runnable {

		private long _lastTime = 0, _curTime;
		private int _start_x=0,_start_y=0,_end_x = 250,_end_y = 250;
		private int _cur_x=0,_cur_y=0;
		private float speed = 1.1f;
		
		private L2World _world = new L2World();

		public MyCanvas() {
			setBackground(Color.GRAY);
			// setSize(300, 300);
			
			L2NpcInstance _npc =  _world.getOrCreateNpc(0);
			_npc.setXYZ(0, 0, 0);
			_npc.moveToLocation(200,200,200,20);
			
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

			ArrayList<L2Object> _list = _world.getObjectList();
			for(L2Object el: _list)
				g2.drawRect(el.getX(), el.getY(), 5, 5);
			
			//drawBresenhamLine(0, 0, 200, 200, g);
			//move(g,delta);
			_lastTime = _curTime;
		}
		
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
	}

}
