package fw.game.model;

import fw.dbClasses.dbManager;
import fw.dbClasses.dbSkill;

public class L2Skill {

	private int skill_id;
	private boolean isPassive=false;
	private int level=1;	
	private dbSkill _dbskill;
	
	// Skill
	private long initReuseDelayTime=0;
	private long endReuseDelayTime=0;
	private long initCastTime=0;
	private long endCastTime=0;
	//Buff
	private long startBuffTime=0;
	private long initbuffTime=-1;
	
	public int getSkill_id() {
		return skill_id;
	}
	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
		_dbskill = dbManager.getInstance().getSkill(skill_id);
	}
	public boolean isPassive() {
		return isPassive;
	}
	public void setPassive(boolean isPassive) {
		this.isPassive = isPassive;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return _dbskill.getName();
	}
	public String getIcon() {
		return _dbskill.getIcon();
	}
	
	public void setInitBuff(long timecount) {
		this.startBuffTime = System.currentTimeMillis();
		this.initbuffTime = timecount;
	}
	public boolean isMyBuff(){
		return (initbuffTime != -1);
	}
	public boolean isMyBuffEnd(){
		return ( (System.currentTimeMillis()-startBuffTime) >=  initbuffTime);
	}	
	public int getBuffLeft(){		
		return (int) ((System.currentTimeMillis()-(startBuffTime+(initbuffTime*1000)))/1000);
	}
	
	public long getInitReuseDelayTime() {
		return initReuseDelayTime;
	}
	public void setInitReuseDelayTime(long initReuseDelayTime) {
		this.initReuseDelayTime = initReuseDelayTime;
	}
	public long getEndReuseDelayTime() {
		return endReuseDelayTime;
	}
	public void setEndReuseDelayTime(long endReuseDelayTime) {
		this.endReuseDelayTime = endReuseDelayTime;
	}
	public long getInitCastTime() {
		return initCastTime;
	}
	public void setInitCastTime(long initCastTime) {
		this.initCastTime = initCastTime;
	}
	public long getEndCastTime() {
		return endCastTime;
	}
	public void setEndCastTime(long endCastTime) {
		this.endCastTime = endCastTime;
	}	
	
	@Override
	public String toString() {
		return "L2Skill id: "+getSkill_id()+" Name: "+getName()+" Icon: "+getIcon();
	}
}
