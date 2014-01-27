package fw.connection.game.serverpackets;

import java.util.ArrayList;

import fw.game.model.L2Skill;

public class MagicEffectIcons extends L2GameServerPacket {

	private ArrayList<L2Skill> _list = new ArrayList<L2Skill>();

	@Override
	public void read() {
		int size = readH();
		for (int i = 0; i < size; i++) {
			L2Skill _skill = new L2Skill();
			_skill.setSkill_id(readD());
			_skill.setLevel(readH());
			_skill.setInitBuff(readD());
			_list.add(_skill);
		}

	}

	@Override
	public void excecute() 
	{
		getPlayer().getBuffs().clear();
		
		if (_list.isEmpty())
			return;
		
		for (L2Skill _s : _list)
			getPlayer().getBuffs().add(_s);
	}

}
