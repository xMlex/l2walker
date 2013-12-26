package fw.game;

import org.eclipse.swt.graphics.Color;

public class PlayerChar extends L2Char
{
	public String name = "";
	public String prefix = "";
	public boolean enemy = false;
	public ClanInfo clanInfo=null;
	public String className = "";
	public String classRace = "";
	public int race = 0;
	public int sex = 0;
	public int classId = 0;
	public int unkNow1 = 0;
	public int head = 0;
	public int gloves = 0;
	public int chest = 0;
	public int legs = 0;
	public int feet = 0;
	public int back = 0;
	public int hair = 0;
	public int hairStyle = 0;
	public int hairColor = 0;
	public int face = 0;
	public int clanId = 0;
	public int clanCrestId = 0;
	public int allyId = 0;
	public int allyCrestId = 0;
	public int siegFlags = 0;
	public byte getInvisible = 0x00;
	public byte getMountType = 0x00;
	public byte getPrivateStoreType = 0x00;
	public short cubicsSet[] = null;
	public byte findPartyMembers = 0x00;
	public int abnormalEffect = 0;
	public byte unknow1 = 0x00;
	public short recomHave = 0; // Blue value for name (0 = white, 255 = pure blue)
	public int unknow2 = 0;
	public int unknow3 = 0;
	public int unknow4 = 0;
	public byte isMounted = 0x00;
	public byte unknow5 = 0x00;
	public int clanCrestLargeId = 0;
	public byte haveSymbol = 0x00; // Symbol on char menu ctrl+I
	public byte haveAura = 0x00; // Hero Aura
	public byte fishingMode = 0x00; // 0x01: Fishing Mode
	public int unknow6 = 0;
	public int unknow7 = 0;
	public int unknow8 = 0;
	public int nameColor = 0;


	public void updateCharItemName()
	{

	}

	public void updateCharForDie()
	{

	}

	public void updateCharItemColor(final Color color)
	{

	}

}
