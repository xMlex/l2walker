package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fw.game.GameEngine;
import fw.game.L2Item;


public class TradeStart extends ServerBasePacket{
	private GameEngine _clientThread;
	private L2Item[] _itemList;
	@SuppressWarnings("unused")
	private int _objectId;
	private short _count;


	public TradeStart(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type - 0x1e
		_objectId = readD();
		_count = (short)readH();
		_itemList = new L2Item[_count];
		for (int i=0;i<_count;i++)//int i = 0; i < count; i++)
		{
			_itemList[i] = new L2Item();
			_itemList[i].type1 = (short)readH();// item type
			_itemList[i].objectId = readD();
			_itemList[i].item_id = readD();
			_itemList[i].itemCount = readD();
			_itemList[i].type2 = (short)readH();// item type2
			readH();	// 0x00 ?
			_itemList[i].bodyPart = readD();// rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
			_itemList[i].enchantLevel = (short) readH();// enchant level
			readH();// 0x00?
			readH();// 0x00?
			_itemList[i].dbL2Item = _clientThread.getMaps().getDbObjects().getDbL2Item(_itemList[i].item_id);
		}

	}

	public void runImpl()
	{
		_clientThread.getVisualInterface().requestTradeDialog(_itemList);
	}
}
