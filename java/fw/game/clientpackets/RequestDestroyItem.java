package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestDestroyItem extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _objectId;
	private int _count;
	/**
	 * 59
	 * 0b 00 00 40		// object id
	 * 01 00 00 00		// count
	 *
	 * format:		cdd
	 */
	public RequestDestroyItem(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_objectId = readD();
		_count = readD();
	}

	public RequestDestroyItem(GameEngine clientThread, int objectId, int count)
	{
		_clientThread = clientThread;
		_objectId = objectId;
		_count = count;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x59);
		writeD(_objectId);
		writeD(_count);
	}

}
