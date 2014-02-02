package fw.connection.socks.interlude.server;

import fw.connection.socks.interlude.L2GameSocksServerPacket;

public class KeyPacket extends L2GameSocksServerPacket{

	private byte[] _key = new byte[16];
	private int status = 0;
	
	@Override
	public void read() {
		status = readC();		
		readB(_key);		
	}

	@Override
	public void excecute() {
		_log.info("Key Status: "+status);
		getClient().setGameCrypt(_key);		
	}

}
