package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Char;

public class CharInfo extends L2GameServerPacket {

	private L2Char _char;
	
	@Override
	public void read() {
		Location loc = new Location(readD(), readD(), readD(), readD());
		_char = getClient().getGameEngine().getWorld().getOrCreateChar(readD());
		_char.setLoc(loc);
		_char.setName(readS());
	}

	@Override
	public void excecute() {

	}

}
