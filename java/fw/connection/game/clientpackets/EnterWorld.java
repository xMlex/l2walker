package fw.connection.game.clientpackets;

import fw.connection.game.CLIENT_STATE;

public class EnterWorld extends L2GameClientPacket{

	@Override
	public void excecute() {	
		getClient().setState(CLIENT_STATE.AUTHED);
		writeC(0x03);
		writeB(new byte[104]);
	}

}
