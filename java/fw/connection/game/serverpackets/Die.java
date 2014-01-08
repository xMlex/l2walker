package fw.connection.game.serverpackets;

import fw.game.model.L2Character;

public class Die extends L2GameServerPacket {

	private L2Character _char;
	@Override
	public void read() {		
		_char = (L2Character)getClient().getGameEngine().getWorld().getObject(readD());
		if(_char == null) return;
		_char.setDead(true);
		_char.setHasHideout( ((readD() == 1)?true:false) );
		_char.setHasCastle( ((readD() == 1)?true:false) );
		_char.setFlags(((readD() == 1)?true:false));
		_char.setSweepable(((readD() == 1)?true:false));
		_char.setAccess(((readD() == 1)?true:false));
	}

	@Override
	public void excecute() {
		if(_char == null) return;
	}

}
