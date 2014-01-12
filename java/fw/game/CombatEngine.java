package fw.game;

import java.util.ArrayList;

import fw.common.ThreadPoolManager;
import fw.connection.game.clientpackets.*;
import fw.extensions.geodata.GeoEngine;
import fw.extensions.geodata.GeoMove;
import fw.extensions.geodata.PathFindBuffers;
import fw.extensions.util.Location;
import fw.game.model.*;
import fw.game.model.instances.L2NpcInstance;

public class CombatEngine implements Runnable {
	private final GameEngine _ge;
	private boolean _started = false;
	
	private L2Player _self = null;
	
	private int _tmp = 0;
	
	public CombatEngine(GameEngine ge) {
		_ge = ge;
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 1000, 3000);
		PathFindBuffers.initBuffers("8x100;8x128;8x192;4x256;2x320;2x384;1x500");
		GeoEngine.loadGeo();
	}
	
	public void run() {		
		if(!_ge.isEnabled()) return;
		if(_ge.getSelfChar() == null){			
			return;
		}		
		_self = _ge.getSelfChar();
		Location me = _self.getLoc();
		Location end = new Location(-96708, 238316, -3376);
		ArrayList<Location> _path = GeoMove.findPath(_self.getX(), _self.getY(), _self.getZ(), end, null, false, 0);
		
		
		_self.sendPacket(new MoveBackwardToLocation(_path.get(1)));
		System.out.println("Move to: "+_path.get(1)+" Distace: "+end.distance3D(me));
		_tmp++;
		
		if(true) return;
		
		
		
		if(_self.isMove()) return;
		if(_self.isDead()) return;
		
		L2Drop _drop = _ge.getWorld().getDropInRadius(_self.getLoc(), 300);
		if(_drop != null){
			_self.sendPacket(new Action(_drop.getObjectId()));
			return;
		}
		
		
		L2NpcInstance _mob = null;
		L2Object _target = _self.getTarget();
		
		if(_target != null && _target.isNpc()){
			L2NpcInstance _trg = (L2NpcInstance)_target;
			if(!_trg.isDead() && _trg.isAttackable())
				_mob = _trg;
				
		}
		
		if(_mob == null){
			if(_self.getCurrentHpPercents() < 80){ 
				if(!_self.isSitting()){
					_self.sendPacket(new RequestActionUse(0));
				}
				return;		
			}			
			_mob =  _ge.getWorld().getMobInRadius(_self.getLoc(), 1500);
		}
		if(_mob == null) return;			
		
		
		if(_self.isSitting()){
			_self.sendPacket(new RequestActionUse(0));
			return;
		}
		
		if(_self.getDistance(_mob) <= 100){
			if(!_started){
				_self.useSkill(1001); 
				_started = true;
			}
			_self.sendPacket(new Action(_mob.getObjectId()));
			return;
		}else{			
			_self.sendPacket(new MoveBackwardToLocation(_mob.getLoc()));
			return;
		}
		
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		ThreadPoolManager.getInstance().getGeneralScheduledThreadPool().remove(this);
		super.finalize();
	}

}
