package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;
import fw.extensions.util.Location;

public class MoveBackwardToLocation extends L2GameSocksClientPacket{

	private Location _to,_from;
	
	@Override
	public void excecute() {
		_to = new Location(readD(), readD(), readD());
		//_log.info("ToLoc: "+_to);
		_from = new Location(readD(), readD(), readD());
		
		writeC(0x01);		
		writeLoc(_to);
		writeLoc(_from);
		writeD(1);
	}

}
