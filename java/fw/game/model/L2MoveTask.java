package fw.game.model;

import java.util.Map.Entry;

import fw.extensions.util.Location;
import fw.game.GameEngine;

public class L2MoveTask implements Runnable {

	private final GameEngine _ge;
	private long _lastTime = 0L;
	
	public L2MoveTask(GameEngine ge){ 
		this._ge = ge;
	}
	
	public void run() {
		
		long _curTime = System.currentTimeMillis();
		double d,dx,dy,dz,
		axy,az,rxy,speed;
		
		synchronized (_ge.getWorld().getObjects()) {
			
			for (Entry<Integer, L2Object> obj : _ge.getWorld().getObjects()
					.entrySet()) {		
				L2Object _el = obj.getValue();
				if( !_el.isLive() ){
					//System.out.println("Move, non L2Charaster: "+_el);
					return;				
				}
				
				L2Character _obj = (L2Character)_ge.getWorld().getObject(_el.getObjectId());
				
				//if(!_obj.isMove()) return;
				//System.out.println("L2MoveTask");
				
				Location _to = _obj.getToLoc();
				if(_obj.getToX() == 0) return;
				
				Location _orig = _obj.getLoc();
				
				if(_orig.distance3D(_to) <= 15 ){
					_obj.setLoc(_to);
					_obj.setMove(false);
					System.out.println("_orig.distance3D(_to) <= 15");
					return;
				}
				
				d = (_curTime-_lastTime)/1000;
				
				dx = _obj.getX()-_obj.getToX();
				dy = _obj.getY()-_obj.getToY();
				dz = _obj.getZ()-_obj.getToZ();
				
				//speed = _obj.getRunSpeed();
				axy = Math.atan2(dy,dx+0.001);
				rxy = Math.sqrt(dy*dy+dx*dx);
				az = Math.atan2(dz,rxy);
				
				_obj.setHeading( (int)Math.round(axy * 32768 / Math.PI) );
				
				speed = _obj.getRunSpeed()*d*_obj.getMovementSpeedMultiplier();
				
				if (rxy < speed) {
					_obj.setLoc(_obj.getToLoc());	
					_obj.setMove(false);
					System.out.println("rxy < speed");
				}else{
					System.out.println("Calculiate");
					_obj.setXYZ(
							(int)(_obj.getX()+speed*Math.cos(axy)), 
							(int)(_obj.getY()+speed*Math.sin(axy)), 
							(int)(_obj.getZ()+speed*Math.tan(az)));
					System.out.println("Calculiate: speed: "+speed);
				}
				
				
			}
		}
		_lastTime = _curTime;
		
	}

}
