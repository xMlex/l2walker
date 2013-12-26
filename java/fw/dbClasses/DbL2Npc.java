package fw.dbClasses;

public class DbL2Npc
{
	int idTemplate;
	String name="Unknow";
	String npcClass="Unknow";
	String type="Unknow";
	int isUndead=0;

	public int getIdTemplate()
	{
		return idTemplate;
	}

	public void setIdTemplate(int idTemplate)
	{
		this.idTemplate = idTemplate;
	}

	public int getIsUndead()
	{
		return isUndead;
	}

	public void setIsUndead(int isUndead)
	{
		this.isUndead = isUndead;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNpcClass()
	{
		return npcClass;
	}

	public void setNpcClass(String npcClass)
	{
		this.npcClass = npcClass;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
