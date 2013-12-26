package fw.dbClasses;

public class DbL2ItemBase
{
	int item_id;
	int icon_id;
	String name;
	String description;
	
	public int getItem_id()
	{
		return item_id;
	}

	public void setItem_id(int item_id)
	{
		this.item_id = item_id;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public int getIcon_id()
	{
		return icon_id;
	}
	public void setIcon_id(int icon_id)
	{
		this.icon_id = icon_id;
	}
}
