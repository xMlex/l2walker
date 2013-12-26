package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import fw.game.GameEngine;
import fw.game.PlayerChar;


public class SendTradeRequest extends ServerBasePacket
{
	private int _objectId;
	private GameEngine _clientThread;
	
	public SendTradeRequest(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type
		_objectId=readD();
	}

	public void runImpl()
	{
		PlayerChar playerChar = null;
		playerChar = _clientThread.getMaps().getOrCreatePlayerChar(_objectId);
		_clientThread.getVisualInterface().requestTrade(playerChar);
	}
}
