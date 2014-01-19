package fw.connection.game.serverpackets;

import java.util.HashMap;
import java.util.Map.Entry;

import fw.game.model.L2Character;
import fw.game.model.L2PlayerEvent;

public class StatusUpdate extends L2GameServerPacket {

	private int objectId;
	private HashMap<Integer, Integer> _attrlist = new HashMap<Integer, Integer>();

	@Override
	public void read() {
		objectId = readD();
		int size = readD();
		for (int i = 0; i < size; i++)
			_attrlist.put(readD(), readD());

	}

	@Override
	public void excecute() {
		L2Character _o = (L2Character) getClient().getGameEngine().getWorld()
				.getObject(objectId);
		for (Entry<Integer, Integer> el : _attrlist.entrySet()) {

			switch (el.getKey()) {
			case 1:// lvl
				break;
			case 2://  EXP
				break;
			case 9://  cur_HP
				_o.setCurrentHp(el.getValue());
				break;
			case 10://  max_HP
				_o.setMax_hp(el.getValue());
				break;
			case 11://  cur_MP
				_o.setCurrentMp(el.getValue());
				break;
			case 12://  max_MP
				_o.setMax_mp(el.getValue());
				break;
			case 33://  cur_CP
				_o.setCurrentCp(el.getValue());
				break;
			case 34://  max_CP
				_o.setMax_cp(el.getValue());
				break;
			default:
				break;
			}
		}
		if(getClient().getGameEngine().getSelfChar().getObjectId() == _o.getObjectId()){
			getClient().getVisualInterface().procSetUserChar(getClient().getGameEngine().getSelfChar());
			getPlayer().onEvent(L2PlayerEvent.StatusUpdate);
		}
		
		//getClient().getVisualInterface().procPlayerChar(playerChar);
	}

}
