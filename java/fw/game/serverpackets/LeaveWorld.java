package fw.game.serverpackets;

import fw.game.GameEngine;

public class LeaveWorld extends ServerBasePacket {

	private GameEngine _clientThread;

	public LeaveWorld(GameEngine clientThread, byte data[]) {
		_clientThread = clientThread;
	}

	public void runImpl() {
		_clientThread.setLogout();
	}
}
