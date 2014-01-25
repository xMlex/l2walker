package fw.connection.game.serverpackets;

import fw.game.model.L2Item;

public class InventoryUpdate extends L2GameServerPacket {

	@Override
	public void read() {
		int count = readH();
		for (int i = 0; i < count; i++) {
			int upd_type = readH();// Update type : 01-add, 02-modify,  03-remove
			int Type1 = readH();
			L2Item _item = getPlayer().getInventory().getOrCreate(readD());
			
			_item.setItemType1(Type1);
			_item.setId(readD());
			_item.setCount(readD());
			_item.setItemType2(readH());
			_item.setCustType1(readH());
			_item.setEquipped( (readH()==0)?false:true );
			_item.setBodyPart(readD());
			_item.setEnchantLevel(readH());
			_item.setCustType2(readH());
			_item.setAugId(readD());
			_item.setShadowTime(readD());
			if(upd_type == 3 && _item.getCount() <= 0)
				getPlayer().getInventory().remove(_item);			
		}
	}

	@Override
	public void excecute() {
		getPlayer().getInventory().updateSlots();
	}

}
