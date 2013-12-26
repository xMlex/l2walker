package fw.dbClasses;

public class DbL2Skill
{
	int skill_id;
	String name="Unknow";
	String desc="";
	int icon_id=0;
	
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public int getIcon_id()
	{
		return icon_id;
	}
	public void setIcon_id(int icon_id)
	{
		this.icon_id = icon_id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getSkill_id()
	{
		return skill_id;
	}
	public void setSkill_id(int skill_id)
	{
		this.skill_id = skill_id;
	}
}
