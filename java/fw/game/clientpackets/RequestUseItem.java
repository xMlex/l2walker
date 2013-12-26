package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestUseItem extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _objectId;

	public RequestUseItem(byte[] message, GameEngine clientThread) {
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_objectId = readD();
	}

	public RequestUseItem(GameEngine clientThread, int objectId) {
		_clientThread = clientThread;
		_objectId = objectId;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x14);
		writeD(_objectId);
	}
}
