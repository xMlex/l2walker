package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Playable;

public class ChangeWaitType extends L2GameServerPacket {

	private int objectId;
	private Location loc;
	private boolean isSitting;
	@Override
	public void read() {		
		objectId = readD();
		isSitting =(readD() ==0)?true:false;
		loc = new Location(readD(),readD(),readD());
	}

	@Override
	public void excecute() {
		
		L2Playable _target = (L2Playable)getClient().getGameEngine().getWorld().getObject(objectId);
		if(_target == null){
			_log.warning("ChangeWaitType target null");
			return;
		}
		_target.setSitting(isSitting);
		_target.setLoc(loc);
		
	}

}
