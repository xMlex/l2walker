package fw.game.clientpackets;

import fw.game.GameEngine;

public class Logout extends ClientBasePacket
{
	private GameEngine _clientThread;


    public Logout(GameEngine clientThread)
    {
    	_clientThread = clientThread;
    }

    public void runImpl()
    {
    	_clientThread.sendPacket(getMessage());
    }

	@Override
	protected void writeImpl() {
		writeC(0x09);
	}
}