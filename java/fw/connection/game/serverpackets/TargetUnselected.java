package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Character;

public class TargetUnselected extends L2GameServerPacket {

	private int objectId;
	private Location loc;
	
	@Override
	public void read() {
		objectId = readD();
		loc = new Location(readD(), readD(), readD());
	}

	@Override
	public void excecute() {
		L2Character _obj = (L2Character)getClient().getGameEngine().getWorld().getObject(objectId);
		if(_obj == null) return; 
		_obj.setTarget(null);
		_obj.setLoc(loc);
	}

}
