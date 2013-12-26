package fw.dbClasses;

public class DbL2Armor extends DbL2ItemBase
{ 
	String bodyPart; 
	String crystalType;
	int p_Def,m_Def,mp_bonus;
	String armorType;
	public String getArmorType()
	{
		return armorType;
	}
	public void setArmorType(String armorType)
	{
		this.armorType = armorType;
	}
	public String getBodyPart()
	{
		return bodyPart;
	}
	public void setBodyPart(String bodyPart)
	{
		this.bodyPart = bodyPart;
	}
	public String getCrystalType()
	{
		return crystalType;
	}
	public void setCrystalType(String crystalType)
	{
		this.crystalType = crystalType;
	}
	 
	public int getM_Def()
	{
		return m_Def;
	}
	public void setM_Def(int def)
	{
		m_Def = def;
	}
	public int getMp_bonus()
	{
		return mp_bonus;
	}
	public void setMp_bonus(int mp_bonus)
	{
		this.mp_bonus = mp_bonus;
	}
 
	public int getP_Def()
	{
		return p_Def;
	}
	public void setP_Def(int def)
	{
		p_Def = def;
	} 
}
