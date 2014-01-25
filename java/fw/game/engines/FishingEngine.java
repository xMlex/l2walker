package fw.game.engines;

import fw.common.ThreadPoolManager;
import fw.connection.game.clientpackets.Action;
import fw.connection.game.clientpackets.MoveBackwardToLocation;
import fw.extensions.util.Location;
import fw.game.GameEngine;
import fw.game.model.L2Item;
import fw.game.model.L2Object;
import fw.game.model.L2Player;
import fw.game.model.L2PlayerInventory;
import fw.game.model.ai.L2FishingListener;
import fw.game.model.instances.L2NpcInstance;

public class FishingEngine extends IEngine implements Runnable,L2FishingListener {

	private int id_fishing = 1312,id_pumping = 1313,id_reling=1314; 
	
	private int _rod_id = 6529; 
	private int _korm_id = 7808;
	private boolean _init = false,_started = false;
	
	private L2Player _self;
	private long _lastStart = 0;
	private Location _fishingLoc;
	
	public FishingEngine(GameEngine ge) {
		super(ge);		
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 1000, 1000);
	}

	public void run() {
		if(!_ge.isReady()) return;
		if(!_init){
			_ge.getSelfChar().getFishingInfo()._listeners.add(this);
			_init = true;
		}
		
		_self = _ge.getSelfChar();
		L2PlayerInventory _inv = _self.getInventory();
		
		if(_inv.getCountById(_rod_id) <= 0) { _log.info("paperdol not found: "+_rod_id); return; }
		
		if(_inv.getRHand() == null || _inv.getRHand().getId() != _rod_id){
			_self.useItemId(_rod_id);
			return;
		}
		if(_inv.getLHand() == null || _inv.getLHand().getId() != _korm_id){
			_self.useItemId(_korm_id);
			return;
		}
		if(!_started){
			_self.useSkill(id_fishing);
			_started=true;	
			if(_fishingLoc == null)
				_fishingLoc = _self.getLoc();
		}
		
		// COMBAT 
		while(killAround()){}
		
		/*long _start = System.currentTimeMillis() - _lastStart;
		if(!_self.getFishingInfo().started && _start > 5000 && !_started){
			_self.useSkill(id_fishing);
			_lastStart = System.currentTimeMillis();
		}
		ThreadPoolManager.getInstance().getGeneralScheduledThreadPool().remove(this);	*/	
	}
	
	private boolean killAround(){
		L2NpcInstance _mob = null;
		L2Object _target = _self.getTarget();
		
		if(_target != null && _target.isNpc()){
			L2NpcInstance _trg = (L2NpcInstance)_target;
			if(!_trg.isDead() && _trg.isAttackable())
				_mob = _trg;
				
		}
		
		if( _mob == null)
			_mob =  _ge.getWorld().getMobInRadius(_self.getLoc(), 500);
		
		if(_mob == null){ 
			_self.sendPacket(new MoveBackwardToLocation(_fishingLoc)); 
			try { Thread.sleep(2000); } catch (InterruptedException e) {} 
			return false; 
		}
		
		if(_started){
			_started = false;
			_self.useSkill(id_fishing);
		}
		
		if(_self.getInventory().getRHand() == null || _self.getInventory().getRHand().getId() == _rod_id){
			for (L2Item _it:_self.getInventory().getObjectList()) {
				if(_it.isWeapon() && _it.getId() != _rod_id){
					_self.useItemId(_it.getId());
					break;
				}
			}
		}
		
		_self.sendPacket(new Action(_mob.getObjectId()));
		try { Thread.sleep(500); } catch (InterruptedException e) {}
		return true;
		
	}

	public void FishingStartEnd() {
		//_started = _self.getFishingInfo().started;
		if(!_started) return;
		if(!_self.getFishingInfo().started)
			_self.useSkill(id_fishing);
	}

	public void FishingHpRegen() {
		if(!_self.getFishingInfo().HPstop)
			_self.useSkill(id_reling);
		else
			_self.useSkill(id_pumping);
	}

}
