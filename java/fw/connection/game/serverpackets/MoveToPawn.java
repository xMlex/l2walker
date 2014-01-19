package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Character;
import fw.game.model.L2Object;

public class MoveToPawn extends L2GameServerPacket {

	
	private int objectId;
	private Location _loc;
	
	@Override
	public void read() {
		objectId = readD();
		readD();//targetId
		readD();//distance
		_loc = new Location(readD(), readD(), readD());
	}

	@Override
	public void excecute() {
		L2Object _o = getClient().getGameEngine().getWorld().getObject(objectId);
		if(_o == null )
			return;
		if(_o.isLive()){
			((L2Character)_o).moveToLocation(_loc.x, _loc.y, _loc.z);
		}
		else
			_o.setLoc(_loc);
	}

}
