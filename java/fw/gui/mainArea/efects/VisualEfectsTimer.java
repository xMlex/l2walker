package fw.gui.mainArea.efects;

import java.util.Iterator;

import fw.game.L2Skill;
import fw.game.L2SkillUse;
import fw.gui.mainArea.L2UserSkillVisualObject;
import javolution.util.FastMap;

public class VisualEfectsTimer extends Thread
{
	public static final int ticksPerSecond = 10;

	public static final int millisBetweenTicks = 1000 / ticksPerSecond;

	private FastMap<String, L2Skill> userCharSkillsInEfect = new FastMap<String, L2Skill>();

	public VisualEfectsTimer()
	{
	}

	public void addSkillInEfect(final L2Skill l2Skill, final L2SkillUse l2SkillUse)
	{
		L2UserSkillVisualObject visualObject = (L2UserSkillVisualObject) l2Skill.visualObject;
		if (visualObject == null)
			return;

		visualObject.currentVisualTick = 0;
		visualObject.valueVisualTick = l2SkillUse.reuseDelay / millisBetweenTicks;
		visualObject.maxVisualTick = visualObject.valueVisualTick * visualObject.valueVisualTick; 
		visualObject.skillAreaItem.setProgressBarValues(visualObject.maxVisualTick, 0);

		userCharSkillsInEfect.put(String.valueOf(l2Skill.skill_id), l2Skill);
	}

	public void removeSkillInEfect(final L2Skill l2Skill)
	{
		L2UserSkillVisualObject visualObject = (L2UserSkillVisualObject) l2Skill.visualObject;

		visualObject.skillAreaItem.setProgressBarValues(100, 100);
		userCharSkillsInEfect.remove(String.valueOf(l2Skill.skill_id));
	}
	
	public void removeAll()
	{
		userCharSkillsInEfect.clear();
	}

	@Override
	public void run()
	{
		long timeIni = 0, timeFim = 0;
		while (true)
		{
			timeIni = System.currentTimeMillis();
			// ----//
			try
			{
				processTick();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// ----//
			timeFim = System.currentTimeMillis();

			if (timeFim - timeIni >= millisBetweenTicks)
			{
				continue;
			} else
			{
				try
				{
					sleep(millisBetweenTicks - (timeFim - timeIni));
				}
				catch (InterruptedException e)
				{
					// nada
				}
			}
		}
	}

	private void processTick()
	{
		skillsProgressBar();
	}

	private void skillsProgressBar()
	{
		long currTime = System.currentTimeMillis();
		for (Iterator<L2Skill> iter = userCharSkillsInEfect.values().iterator(); iter.hasNext();)
		{
			L2Skill l2Skill = (L2Skill) iter.next();
			L2UserSkillVisualObject visualObject = (L2UserSkillVisualObject) l2Skill.visualObject;

			if (currTime >= l2Skill.endReuseDelayTime)
			{
				visualObject.skillAreaItem.setProgressBarValues(100, 100);
				userCharSkillsInEfect.remove(String.valueOf(l2Skill.skill_id));
				continue;
			}

			visualObject.currentVisualTick += visualObject.valueVisualTick;

			if (visualObject.currentVisualTick >= visualObject.maxVisualTick)
			{
				visualObject.skillAreaItem.setProgressBarValues(100, 100);
				userCharSkillsInEfect.remove(String.valueOf(l2Skill.skill_id));
				continue;
			}

			visualObject.skillAreaItem.setProgressBarValues(visualObject.maxVisualTick, visualObject.currentVisualTick);
		}

	}

}
