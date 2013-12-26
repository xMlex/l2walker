package fw.dbClasses;

public class DbL2Item extends DbL2ItemBase
{  
	String item_type;
	String consume_type;

	public String getConsume_type()
	{
		return consume_type;
	}

	public void setConsume_type(String consume_type)
	{
		this.consume_type = consume_type;
	}
 
	public String getItem_type()
	{
		return item_type;
	}

	public void setItem_type(String item_type)
	{
		this.item_type = item_type;
	}



}
