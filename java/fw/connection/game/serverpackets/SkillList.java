package fw.connection.game.serverpackets;

import fw.extensions.util.GCArray;
import fw.game.model.L2Skill;

public class SkillList extends L2GameServerPacket {
	
	private GCArray<L2Skill> _skills;

	@Override
	public void read() {		
		_skills = new  GCArray<L2Skill>(readD());
		for (int i = 0; i < _skills.size(); i++) {
			L2Skill _c = new L2Skill();
			_c.setPassive((readD()==1?true:false));
			_c.setLevel(readD());
			_c.setSkill_id(readD());
			readC();
			_skills.add(_c);			
		}
	}

	@Override
	public void excecute() {
		getClient().getGameEngine().getSelfChar().setSkills(_skills);
	}

}
