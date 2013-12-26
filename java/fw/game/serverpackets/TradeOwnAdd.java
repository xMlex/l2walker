package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fw.game.GameEngine;
import fw.game.L2Item;


public class TradeOwnAdd extends ServerBasePacket
{
	private GameEngine _clientThread;
	private L2Item _item;


	public TradeOwnAdd(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH();				//read message lenght
		readC();				// (1) message type - 0x20


		_item = new L2Item();

		readH();  // count
		_item.type1 = (short)readH();// item type1
		_item.objectId = readD();
		_item.item_id = readD();
		_item.itemCount = readD();
		_item.type2 = (short)readH();// item type2
		readH();	// ?
		_item.bodyPart = readD();	// rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
		_item.enchantLevel = (short)readH();
		readH();	// ?
		readH();	// ?
		_item.dbL2Item = _clientThread.getMaps().getDbObjects().getDbL2Item(_item.item_id);
	}



	public void runImpl()
	{
		if(_clientThread.getUserCharItems().containsKey(_item.objectId))
			_clientThread.getVisualInterface().procTradeAddOwnItem(_clientThread.getUserCharItems().get(_item.objectId));
		_clientThread.getVisualInterface().procTradeAddOwnItem(_item);
	}
}
