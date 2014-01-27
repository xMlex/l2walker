package fw.connection.game.serverpackets;

import fw.game.model.L2Drop;

public class SpawnItem extends L2GameServerPacket {

	@Override
	public void read() {		
		int objectId = readD();
		L2Drop _drop = getClient().getGameEngine().getWorld().getOrCreateDrop(objectId);
		_drop.setItemID(readD());
		_drop.setXYZ(readD(), readD(), readD());
		_drop.setStackable( (readD()==1)?true:false );
		_drop.setCount(readD());
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub

	}

}
