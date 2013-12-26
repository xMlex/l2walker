package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Skill;
import fw.game.L2SkillUse;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javolution.util.FastMap;

public class SkillUsePack extends ServerBasePacket
{
	private SkillUsePack()
	{
	}

	public static void runImplementatiton(GameEngine gameEngine, byte data[])
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);
		L2SkillUse l2SkillUse = new L2SkillUse();

		l2SkillUse.objId = buf.getInt();
		l2SkillUse.targetObjId = buf.getInt();
		l2SkillUse.skill_id = buf.getInt();
		l2SkillUse.level = buf.getInt();
		l2SkillUse.hitTime = buf.getInt();
		l2SkillUse.reuseDelay = buf.getInt();

		l2SkillUse.x = buf.getInt();
		l2SkillUse.y = buf.getInt();
		l2SkillUse.z = buf.getInt();

		if (gameEngine.getUserChar().objId == l2SkillUse.objId)
		{
			FastMap<String, L2Skill> userSkills = gameEngine.getUserCharSkills();

			String skill_idStr = String.valueOf(l2SkillUse.skill_id);

			L2Skill l2Skill = userSkills.get(skill_idStr);
			/*if is not a normal/catalog skill*/
			if(l2Skill==null)return;

			long currTime= System.currentTimeMillis();
			l2Skill.initReuseDelayTime = currTime;
			l2Skill.initCastTime=currTime;
			l2Skill.endReuseDelayTime = l2Skill.initReuseDelayTime + l2SkillUse.reuseDelay;
			l2Skill.endCastTime = l2Skill.initCastTime + l2SkillUse.hitTime;

			if (l2Skill != null)
			{
				gameEngine.getVisualInterface().procSelfSkillUse(l2Skill, l2SkillUse);
			}
		}

	}
}
