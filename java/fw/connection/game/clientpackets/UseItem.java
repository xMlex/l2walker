package fw.connection.game.clientpackets;

import fw.game.model.L2Item;

public class UseItem extends L2GameClientPacket {

	private int _oid;
	
	public UseItem(int oid){
		_oid = oid;
	}
	public UseItem(L2Item item){
		_oid = item.getObjectId();
	}
	
	@Override
	public void excecute() {
		writeC(0x14);
		writeD(_oid);
		writeD(0);
	}

}
