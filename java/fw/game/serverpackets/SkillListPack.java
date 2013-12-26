package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Skill;
import fw.game.Maps;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

import javolution.util.FastMap;

public class SkillListPack extends ServerBasePacket
{
	private SkillListPack()
	{
	}

	public static void runImplementatiton(GameEngine gameEngine, byte data[])
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);

		Maps maps = gameEngine.getMaps();
		FastMap<String, L2Skill> mapSkills = new FastMap<String, L2Skill>();
		
		int attCount = buf.getInt();
		for (int i = 0; i < attCount; ++i)
		{
			boolean passive = (buf.getInt() == 1) ? true : false;
			int level = buf.getInt();
			int skill_id = buf.getInt();

			L2Skill l2Skill = maps.getOrCreateUserCharSkill(skill_id, level, passive);

		    String skill_idStr=String.valueOf(skill_id);		 
		    mapSkills.put(skill_idStr, l2Skill);
		}
		

		FastMap<String, L2Skill> userSkills = gameEngine.getUserCharSkills();

		/*remover skills*/
		for (Iterator<L2Skill> iter = userSkills.values().iterator(); iter.hasNext();)
		{
			L2Skill l2Skill = (L2Skill) iter.next();
			String skill_idStr=String.valueOf(l2Skill.skill_id);		
			
			if(!mapSkills.containsKey(skill_idStr))
			{
				userSkills.remove(skill_idStr);
				gameEngine.getVisualInterface().procRemoveSkill(l2Skill);
			}			
		}
		
		/*adicionar skills*/
		for (Iterator<L2Skill> iter = mapSkills.values().iterator(); iter.hasNext();)
		{
			L2Skill l2Skill = (L2Skill) iter.next();
			String skill_idStr=String.valueOf(l2Skill.skill_id);		
			
			if(!userSkills.containsKey(skill_idStr))
			{
				userSkills.put(skill_idStr,l2Skill);
				gameEngine.getVisualInterface().procAddSkill(l2Skill);
			}			
		}	
		
	}
}
