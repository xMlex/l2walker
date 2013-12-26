package fw.dbClasses;

public class DbL2Weapon extends DbL2ItemBase
{	
	String bodyPart;
	int soulShots,spiritShots;
	String crystalType;
	int p_Atack,m_Atack,rnd_dam;
	String weaponType;
	int atackSpeed;
	public int getAtackSpeed()
	{
		return atackSpeed;
	}
	public void setAtackSpeed(int atackSpeed)
	{
		this.atackSpeed = atackSpeed;
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
	 
	public int getM_Atack()
	{
		return m_Atack;
	}
	public void setM_Atack(int atack)
	{
		m_Atack = atack;
	}
 
	public int getP_Atack()
	{
		return p_Atack;
	}
	public void setP_Atack(int atack)
	{
		p_Atack = atack;
	}
	public int getRnd_dam()
	{
		return rnd_dam;
	}
	public void setRnd_dam(int rnd_dam)
	{
		this.rnd_dam = rnd_dam;
	}
	public int getSoulShots()
	{
		return soulShots;
	}
	public void setSoulShots(int soulShots)
	{
		this.soulShots = soulShots;
	}
	public int getSpiritShots()
	{
		return spiritShots;
	}
	public void setSpiritShots(int spiritShots)
	{
		this.spiritShots = spiritShots;
	}
	public String getWeaponType()
	{
		return weaponType;
	}
	public void setWeaponType(String weaponType)
	{
		this.weaponType = weaponType;
	}
}
