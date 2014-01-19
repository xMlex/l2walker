package fw.connection.game.serverpackets;

import fw.game.model.L2Character;
import fw.game.model.L2PlayerEvent;

public class Die extends L2GameServerPacket {

	private L2Character _char;
	@Override
	public void read() {		
		_char = (L2Character)getClient().getGameEngine().getWorld().getObject(readD());
		if(_char == null) return;
		_char.setAlikeDead(true);
		_char.setHasHideout( ((readD() == 1)?true:false) );
		_char.setHasCastle( ((readD() == 1)?true:false) );
		_char.setFlags(((readD() == 1)?true:false));
		_char.setSweepable(((readD() == 1)?true:false));
		_char.setAccess(((readD() == 1)?true:false));
	}

	@Override
	public void excecute() {
		if(_char == null) return;
		getPlayer().onEvent(L2PlayerEvent.Die, _char);
	}

}
