package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Object;

public class ValidateLocation extends L2GameServerPacket {

	private int objectId;
	private Location _loc;
	
	@Override
	public void read() {
		objectId = readD();
		_loc = new Location(readD(), readD(), readD(), readD());
	}

	@Override
	public void excecute() {
		L2Object _o = getClient().getGameEngine().getWorld().getObject(objectId);
		if(_o == null )
			return;
		_o.setLoc(_loc);
	}

}
