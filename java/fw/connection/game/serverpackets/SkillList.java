package fw.connection.game.serverpackets;

import java.util.ArrayList;

import fw.connection.game.clientpackets.RequestSkillCoolTime;
import fw.extensions.util.GCArray;
import fw.game.model.L2Skill;

public class SkillList extends L2GameServerPacket {
	
	private ArrayList<L2Skill> _skills;

	@Override
	public void read() {
		int size = readD();
		_skills = new  ArrayList<L2Skill>();
		for (int i = 0; i < size; i++) {
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
		getClient().sendPacket(new RequestSkillCoolTime());
	}

}
