package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 15
 * ee cc 11 43 		object id
 * 39 00 00 00 		item id
 * 8f 14 00 00 		x
 * b7 f1 00 00 		y
 * 60 f2 ff ff 		z
 * 01 00 00 00 		show item count
 * 7a 00 00 00      count                                         .
 *
 * format  dddddddd
 */
public class SpawnItem extends ServerBasePacket
{
	private GameEngine _clientThread;
	private int _objectId;
	private int _itemId;
	private int _x, _y, _z;
	int _stackable, _count;
	int _unk;

	public SpawnItem(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH();				//read message lenght
		readC();				// (1) message type

		_objectId = readD();
		_itemId = readD();
		_x = readD();
		_y = readD();
		_z = readD();
		_stackable = readD();
		_count = readD();
		_unk = readD();
	}

	public void runImpl()
	{
		L2Item myItem = _clientThread.getMaps().getOrCreateItems(_objectId, _itemId,_count,_x,_y,_z);
		_clientThread.getVisualInterface().procAddEnvItem(myItem);
//		_clientThread.getVisualInterface().putMessage("Spawn item detected",GameEngine.MSG_SYSTEM_NORMAL,true);
	}
}
