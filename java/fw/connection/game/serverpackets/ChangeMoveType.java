package fw.connection.game.serverpackets;

import fw.game.model.L2Character;
import fw.game.model.L2Object;

public class ChangeMoveType extends L2GameServerPacket {

	private int objectId,MoveType;
	@Override
	public void read() {
		objectId = readD();
		MoveType = readD();
	}

	@Override
	public void excecute() {
		L2Object _o = getClient().getGameEngine().getWorld().getObject(objectId);
		if(_o == null || !(_o instanceof L2Character))return;
		if(MoveType == 1)
			((L2Character)_o).setRunning(true);
		else
			((L2Character)_o).setRunning(false);
	}

}
