package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fw.game.GameEngine;

public class GameGuardQuery extends ServerBasePacket
{
    private GameEngine _clientThread;

    public GameGuardQuery(GameEngine clientThread, byte data[]){
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH();				//read message lenght
		readC();				// (1) message type 0xf9
    }

    public void runImpl()
    {
    	_clientThread.getVisualInterface().putMessage("Game Guard Query!",GameEngine.MSG_SYSTEM_FAIL,true);
    }
}
