
package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestDropItem extends ClientBasePacket
{
	private GameEngine _clientThread;
	private final int _objectId;
	private final int _count;
	private final int _x;
	private final int _y;
	private final int _z;
	/**
	 * packet type id 0x12
	 *
	 * 12
	 * 09 00 00 40 		// object id
	 * 01 00 00 00 		// count
	 * fd e7 fe ff 		// x
	 * e5 eb 03 00 		// y
	 * bb f3 ff ff 		// z
	 *
	 * format:		cdd ddd
	 * @param decrypt
	 */
	public RequestDropItem(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_objectId = readD();
		_count    = readD();
		_x        = readD();
		_y        = readD();
		_z        = readD();
	}

	public RequestDropItem(GameEngine clientThread, int objectId, int count, int x, int y, int z)
	{
		_clientThread = clientThread;
		_objectId = objectId;
		_count    = count;
		_x        = x;
		_y        = y;
		_z        = z;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x12);
		writeD(_objectId);
		writeD(_count);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
