package fw.connection.game.serverpackets;

import java.util.ArrayList;

import fw.game.model.L2Item;
import fw.game.model.L2PlayerInventory;

public class ItemList extends L2GameServerPacket {

	private ArrayList<L2Item> _list = new ArrayList<L2Item>();
	
	@Override
	public void read() {
		readH();// window
		int count = readH();
		int itemType1;
		for (int i = 0; i < count; i++) {
			itemType1 = readH();
			L2Item _item = getClient().getGameEngine().getWorld().getOrCreateItem(readD());
			_item.setItemType1(itemType1);
			
			_item.setId(readD());
			_item.setCount(readD());
			_item.setItemType2(readH());
			_item.setCustType1(readH());
			_item.setEquipped( (readH()==1)?true:false );
			_item.setBodyPart(readD());
			_item.setEnchantLevel(readH());
			_item.setCustType2(readH());
			_item.setAugId(readD());
			_item.setShadowTime(readD());
		}

	}

	@Override
	public void excecute() {
		
		L2PlayerInventory _inv =  getClient().getGameEngine().getSelfChar().getInventory();
		_inv.clear();
		for (L2Item el: _list) 
			_inv.add(el);		

	}

}
