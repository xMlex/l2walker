package fw.game;

import fw.dbClasses.DbL2ItemBase;

public class L2Item
{
	public Object visualObject=null;
	public short type1 = 0;
	public int objectId = 0;
	public int item_id = 0;
	public int itemCount = 0;
	public short type2 = 0;
	public short customType1 = 0;
	public short isEquiped = 0;
	public int bodyPart = 0;
	public short enchantLevel = 0;
	public short customType2 = 0;
	public DbL2ItemBase dbL2Item=null;
	public int x,y,z;
}