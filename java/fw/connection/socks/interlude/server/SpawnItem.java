package fw.connection.socks.interlude.server;

import fw.connection.socks.interlude.L2GameSocksServerPacket;

public class SpawnItem extends L2GameSocksServerPacket {

	private int id;
	@Override
	public void read() {
		readD();
		id = readD();
	}

	@Override
	public boolean excecute() {
		if( id > 33000 && id < 33010 ){
			return false;
		}
		return true;
	}

}
