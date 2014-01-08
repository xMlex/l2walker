package fw.connection.game.serverpackets;

import fw.game.model.L2Drop;

public class DropItem extends L2GameServerPacket {

	private L2Drop _item;
	private int player,objectId;
	@Override
	public void read() {		
		player = readD();
		objectId = readD();
		_item = getClient().getGameEngine().getWorld().getOrCreateDrop(objectId);
		_item.setPlayerID(player);
		
		_item.setItemID(readD());
		_item.setXYZ(readD(), readD(), readD());
		_item.setStackable(((readD()==1)?true:false));
		_item.setCount(readD());
	}

	@Override
	public void excecute() {}

}
