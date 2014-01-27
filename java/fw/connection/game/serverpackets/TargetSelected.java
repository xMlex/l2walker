package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Character;
import fw.game.model.L2Object;

public class TargetSelected extends L2GameServerPacket {

	private int objectId,targetId;
	private Location loc;
	@Override
	public void read() {		
		objectId = readD();
		targetId = readD();
		loc = new Location(readD(), readD(), readD());
	}

	@Override
	public void excecute() {
		L2Object _o = getClient().getGameEngine().getWorld().getObject(objectId);
		if(_o == null ){
			_log.warning("TargetSelected obj = null");
			return;
		}
		_o.setLoc(loc);
		L2Object _target = getClient().getGameEngine().getWorld().getObject(targetId);
		((L2Character)_o).setTarget(_target);
		if(_o.getObjectId() == getPlayer().getObjectId()){
			getPlayer().getGameEngine().getVisualInterface().procMyTargetSelected(((L2Character)_target));
		}
	}

}
