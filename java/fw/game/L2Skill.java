package fw.game;

import fw.dbClasses.DbL2Skill;

public class L2Skill
{
	public int skill_id;
	public boolean passive=false;
	public int level=1;
	public DbL2Skill dbL2Skill=null;	
	public Object visualObject=null;
	public long initReuseDelayTime=0;
	public long endReuseDelayTime=0;
	public long initCastTime=0;
	public long endCastTime=0;	
}
