package fw.connection.socks.interlude.server;

import fw.connection.socks.interlude.L2GameSocksServerPacket;

public class DropItem extends L2GameSocksServerPacket {

	private int id;
	
	@Override
	public void read() {
		readD();
		readD();
		
		id = readD();
		
	}

	@Override
	public boolean excecute() {
		if( id > 33000 && id < 33010 ){
			_log.info("Not send(DropItem): "+id);
			return false;
		}
		//_log.info("Send: "+id);
		return true;
	}

}
