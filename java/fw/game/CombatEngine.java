package fw.game;

import fw.common.ThreadPoolManager;
import fw.connection.game.clientpackets.*;
import fw.game.model.*;
import fw.game.model.instances.L2NpcInstance;

public class CombatEngine implements Runnable {
	private final GameEngine _ge;
	public CombatEngine(GameEngine ge) {
		_ge = ge;
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 1000, 1000);
	}
	
	public void run() {		
		if(_ge.getSelfChar() == null) return;	
		
		if(_ge.getSelfChar().isMoving()) return;
		if(_ge.getSelfChar().isDead()) return;
		
		L2Drop _drop = _ge.getWorld().getDropInRadius(_ge.getSelfChar().getLoc(), 300);
		if(_drop != null){
			_ge.getGameConnection().sendPacket(new Action(_drop.getObjectId()));
			return;
		}
		
		if(_ge.getSelfChar().getCurrentHpPercents() <= 60) return;
		
		L2NpcInstance _mob =  _ge.getWorld().getMobInRadius(_ge.getSelfChar().getLoc(), 1000);
		if(_mob == null) return;
		
		
		
		if(_ge.getSelfChar().getDistance(_mob) <= 350){
			_ge.getGameConnection().sendPacket(new Action(_mob.getObjectId()));
			return;
		}else{
			_ge.getGameConnection().sendPacket(new MoveBackwardToLocation(_mob.getLoc()));
			return;
		}
		
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		ThreadPoolManager.getInstance().getGeneralScheduledThreadPool().remove(this);
		super.finalize();
	}

}
