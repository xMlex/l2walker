package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import fw.game.GameEngine;
import fw.game.L2Item;

/**
 * 0x0c
 * d6 6d c0 4b		player id who dropped it
 * ee cc 11 43 		object id
 * 39 00 00 00 		item id
 * 8f 14 00 00 		x
 * b7 f1 00 00 		y
 * 60 f2 ff ff 		z
 * 01 00 00 00 		show item-count 1=yes
 * 7a 00 00 00      count                                         .
 *
 * format  dddddddd    rev 377
 *         ddddddddd   rev 417
 */
public class DropItem extends ServerBasePacket
{
	private GameEngine _clientThread;
	private int _playerId;
	private int _objectId;
	private int _itemId;
	private int _x, _y, _z;
	private int _stackable; //0x01 - have count, 0x00 - no count
	private int _count;
	private int _unk;

	public DropItem(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH();				//read message lenght
		readC();				// (1) message type

		set_playerId(readD());
		_objectId = readD();
		_itemId = readD();
		_x = readD();
		_y = readD();
		_z = readD();
		set_stackable(readD());
		_count = readD();
		set_unk(readD());

	}

	public void runImpl()
	{
		L2Item myItem = _clientThread.getMaps().getOrCreateItems(_objectId, _itemId,_count,_x,_y,_z);
		_clientThread.getVisualInterface().procAddEnvItem(myItem);
		//_clientThread.getVisualInterface().putMessage("Drop item detected",GameEngine.MSG_SYSTEM_NORMAL,true);
	}

	public void set_playerId(int _playerId) {
		this._playerId = _playerId;
	}

	public int get_playerId() {
		return _playerId;
	}

	public void set_stackable(int _stackable) {
		this._stackable = _stackable;
	}

	public int get_stackable() {
		return _stackable;
	}

	public void set_unk(int _unk) {
		this._unk = _unk;
	}

	public int get_unk() {
		return _unk;
	}
}
