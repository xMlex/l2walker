package fw.connection.login.serverpackets;

import fw.connection.game.serverpackets.L2GameServerPacket;
import fw.game.model.L2Object;
import fw.game.model.L2Playable;

public class ChangeWaitType extends L2GameServerPacket {

	private L2Object _char;
	@Override
	public void read() {
		_char = getClient().getGameEngine().getWorld().getObject(readD());
		if(_char.isChar() || _char.isPlayable() || _char.isPlayer()){
			((L2Playable)_char).setSitting( (readD() == 1)?false:true );
			_char.setXYZ(readD(), readD(), readD());
		}			
	}

	@Override
	public void excecute() {
		
	}

}
