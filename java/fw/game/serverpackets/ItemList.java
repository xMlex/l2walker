package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Item;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javolution.util.FastList;
import javolution.util.FastMap;

public class ItemList extends ServerBasePacket
{
	@SuppressWarnings("unused")
	private int _showWindow;
	private GameEngine _clientThread;
	private FastMap<Integer, L2Item> userCharItems;
	private FastList<L2Item> listNew;
	private FastList<L2Item> listUpdate;
	private FastMap<Integer, FastMap<Integer, L2Item>> userCharItemsFromId;

	public ItemList(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		userCharItems = _clientThread.getUserCharItems();
		listNew = new FastList<L2Item>();
		listUpdate = new FastList<L2Item>();
		userCharItemsFromId = _clientThread.getUserCharItemsFromId();

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH(); // msg length
		readC(); // msg type
		_showWindow = readH();
		int n = readH();

		for(int i=0;i<n;i++) {
			int type1 = readH();
			int objectId = readD();
			int itemId = readD();
			int itemCount = readD();
			int type2 = readH();
			int customType1 = readH();
			int isEquiped = readH();
			int bodyPart = readD();
			int enchantLevel = readH();
			int customType2 = readH();

			L2Item item = userCharItems.get(objectId);
			if (item != null)
			{
				item.type1 = (short)type1;
				item.objectId = objectId;
				item.item_id = itemId;
				item.itemCount = itemCount;
				item.type2 = (short)type2;
				item.customType1 = (short)customType1;
				item.isEquiped = (short)isEquiped;
				item.bodyPart = bodyPart;
				item.enchantLevel = (short)enchantLevel;
				item.customType2 = (short)customType2;
				listUpdate.add(item);
			} else
			{

				item = new L2Item();
				item.type1 = (short)type1;
				item.objectId = objectId;
				item.item_id = itemId;
				item.itemCount = itemCount;
				item.type2 = (short)type2;
				item.customType1 = (short)customType1;
				item.isEquiped = (short)isEquiped;
				item.bodyPart = bodyPart;
				item.enchantLevel = (short)enchantLevel;
				item.customType2 = (short)customType2;
				item.dbL2Item = _clientThread.getDbObjects().getDbL2Item(itemId);
				userCharItems.put(item.objectId, item);

				listNew.add(item);
			}

			FastMap<Integer, L2Item> mapItemsObjs = userCharItemsFromId.get(item.item_id);
			if (mapItemsObjs == null)
			{
				mapItemsObjs = new FastMap<Integer, L2Item>();
				userCharItemsFromId.put(item.item_id, mapItemsObjs);
			}
			mapItemsObjs.put(item.objectId, item);
		}

	}

	public void runImpl()
	{
		if (listNew.size()> 0)
		{
			L2Item newItems[] = new L2Item[listNew.size()];
			listNew.toArray(newItems);
			_clientThread.getVisualInterface().procAddItems(newItems);
		}
	}
}
