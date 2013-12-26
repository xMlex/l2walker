package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javolution.util.FastList;
import javolution.util.FastMap;

public class InventoryUpdate extends ServerBasePacket
{
	private GameEngine _clientThread;
	private FastMap<Integer, L2Item> userCharItems;
	private FastMap<Integer, FastMap<Integer, L2Item>> userCharItemsFromId;
	private FastList<L2Item> listNew;
	private FastList<L2Item> listUpdate;
	private FastList<L2Item> listRemove;

	public InventoryUpdate(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;

		userCharItems = _clientThread.getUserCharItems();
		userCharItemsFromId = _clientThread.getUserCharItemsFromId();

		listNew = new FastList<L2Item>();
		listUpdate = new FastList<L2Item>();
		listRemove = new FastList<L2Item>();


		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH(); // msg lenght
		readC(); // msg type

		int count = readH();

		for (int i = 0; i < count; ++i)
		{// for
			int updateType = readH();// Update type : 01-add, 02-modify, 03-remove
			short type1 = (short)readH();
			int objectId = readD();
			final int itemId = readD();
			int itemCount = readD();
			short type2 = (short)readH();
			short customType1 = (short)readH();
			short isEquiped = (short)readH();
			int bodyPart = readD();
			short enchantLevel = (short)readH();
			short customType2 = (short)readH();

			L2Item item = userCharItems.get(objectId);
			if (item == null)
			{
				updateType = 1;
				item = new L2Item();
			}

			item.type1 = type1;
			item.objectId = objectId;
			item.item_id = itemId;
			item.itemCount = itemCount;
			item.type2 = type2;
			item.customType1 = customType1;
			item.isEquiped = isEquiped;
			item.bodyPart = bodyPart;
			item.enchantLevel = enchantLevel;
			item.customType2 = customType2;
			item.dbL2Item = _clientThread.getMaps().getDbObjects().getDbL2Item(itemId);

			FastMap<Integer, L2Item> mapItensObjs = userCharItemsFromId.get(item.item_id);
			if (mapItensObjs == null)
			{
				mapItensObjs = new FastMap<Integer, L2Item>();
				userCharItemsFromId.put(item.item_id, mapItensObjs);
			}

			if (updateType == 1)// add
			{
				listNew.add(item);
				mapItensObjs.put(item.objectId, item);
			} else if (updateType == 2)// modify
			{
				listUpdate.add(item);
			} else if (updateType == 3)// remove
			{
				listRemove.add(item);

				userCharItems.remove(item.objectId);
				mapItensObjs.remove(item.objectId);

				if (mapItensObjs.size() == 0)
					userCharItemsFromId.remove(item.item_id);
			}

		}
	}

	public void runImpl()
	{
		
			if (listNew.size() > 0)
			{
				L2Item items[] = new L2Item[listNew.size()];
				listNew.toArray(items);
				_clientThread.getVisualInterface().procAddItems(items);
			}

			if (listUpdate.size() > 0)
			{
				L2Item items[] = new L2Item[listUpdate.size()];
				listUpdate.toArray(items);
				_clientThread.getVisualInterface().procUpdateItems(items);
			}

			if (listRemove.size() > 0)
			{
				L2Item items[] = new L2Item[listRemove.size()];
				listRemove.toArray(items);
				_clientThread.getVisualInterface().procDeletItems(items);
			}
			
		}
		
}
