package fw.game.model;

public class L2Skill {

	private int skill_id;
	private boolean isPassive=false;
	private int level=1;	
	
	private long initReuseDelayTime=0;
	private long endReuseDelayTime=0;
	private long initCastTime=0;
	private long endCastTime=0;
	
	public int getSkill_id() {
		return skill_id;
	}
	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
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
}
