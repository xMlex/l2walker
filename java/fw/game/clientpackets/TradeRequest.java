package fw.game.clientpackets;

import fw.game.GameEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TradeRequest extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _objectId;
	
	public TradeRequest(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_objectId = readD();
	}
	
	public TradeRequest(GameEngine clientThread, int objectId) {
		_clientThread = clientThread;
		_objectId = objectId;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	} 
	
	@Override
	protected void writeImpl() {
		writeC(0x15);
		writeD(_objectId);
	}
	
}
