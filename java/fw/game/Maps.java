package fw.game;

import fw.dbClasses.DbObjects;
import javolution.util.FastMap;

public class Maps
{
	private DbObjects dbObjects = null;
	private FastMap<Integer, L2Char> mapL2CharsInfo = new FastMap<Integer, L2Char>();
	private FastMap<Integer, ClanInfo> mapClansInfo = new FastMap<Integer, ClanInfo>();
	private FastMap<Integer, L2Skill> mapL2Skill = new FastMap<Integer, L2Skill>();
	private FastMap<Integer, L2PartyChar> mapL2PartyChar = new FastMap<Integer, L2PartyChar>();
	private FastMap<Integer, L2Item> mapItems = new FastMap<Integer, L2Item>();

	public Maps()
	{
		// ---------//
		ClanInfo clanInfo = new ClanInfo();
		clanInfo.clanId = 0;
		clanInfo.clanName = "";
		clanInfo.allyName = "";
		mapClansInfo.put(0, clanInfo);
		// ---------//

	}

	public L2PartyChar getOrCreatePartyChar(final int objIdPartyChar)
	{
		L2PartyChar l2PartyChar = mapL2PartyChar.get(objIdPartyChar);

		if (l2PartyChar != null)
		{
			return l2PartyChar;
		}

		l2PartyChar = new L2PartyChar();
		l2PartyChar.objIdPartyChar = objIdPartyChar;
		mapL2PartyChar.put(objIdPartyChar, l2PartyChar);

		return l2PartyChar;
	}

	public L2PartyChar getPartyChar(final int objIdPartyChar)
	{
		return   mapL2PartyChar.get(objIdPartyChar);
	}

	public void removePartyChar(final int objIdPartyChar)
	{
		mapL2PartyChar.remove(objIdPartyChar);
	}

	public PlayerChar getOrCreatePlayerChar(final int objId)
	{
		L2Char l2char = mapL2CharsInfo.get(objId);

		if (l2char != null)
		{
			return (PlayerChar) l2char;
		}

		PlayerChar charInfo = new PlayerChar();
		charInfo.objId = objId;
		mapL2CharsInfo.put(objId, charInfo);

		return charInfo;
	}

	public NpcChar getOrCreateNpcChar(final int objId, final int idTemplate)
	{
		L2Char l2char = mapL2CharsInfo.get(objId);

		if (l2char != null)
		{
			return (NpcChar) l2char;
		}

		NpcChar charInfo = new NpcChar();
		charInfo.objId = objId;
		charInfo.idTemplate = idTemplate;
		charInfo.dbl2npc = dbObjects.getDbL2Npc(idTemplate);
		charInfo.realName = charInfo.dbl2npc.getName();
		mapL2CharsInfo.put(objId, charInfo);

		return charInfo;
	}

	public L2Item getOrCreateItems(final int objId, final int itemId, final int itemCount, int x, int y , int z)
	{
		L2Item item = mapItems.get(objId);

		if (item != null)
		{
			return item;
		}

		L2Item newItem = new L2Item();
		newItem.objectId = objId;
		newItem.item_id = itemId;
		newItem.itemCount = itemCount;
		newItem.x = x;
		newItem.y = y;
		newItem.z = z;
		newItem.dbL2Item = dbObjects.getDbL2Item(itemId);
		mapItems.put(objId, newItem);

		return newItem;
	}

	public L2Item getItems(final int objId)
	{
		L2Item item = mapItems.get(objId);

		if (item != null)
		{
			return item;
		}
		return null;
	}

	public L2Skill getOrCreateUserCharSkill(final int skill_id, final int level, final boolean passive)
	{
		L2Skill l2Skill = mapL2Skill.get(skill_id);

		if (l2Skill != null)
		{
			l2Skill.level = level;
			l2Skill.passive = passive;
			return l2Skill;
		}

		l2Skill = new L2Skill();
		l2Skill.skill_id = skill_id;
		l2Skill.level = level;
		l2Skill.passive = passive;
		l2Skill.dbL2Skill = dbObjects.getDbL2Skill(skill_id);

		mapL2Skill.put(skill_id, l2Skill);

		return l2Skill;
	}

	public L2Char getChar(final int objId)
	{
		return mapL2CharsInfo.get(objId);
	}
	
	public L2Item getItem(final int objId)
	{
		return mapItems.get(objId);
	}
	
	public void removeChar(final int objId)
	{
		mapL2CharsInfo.remove(objId);
	}
	
	public void removeItem(final int objId)
	{
		mapItems.remove(objId);
	}

	public ClanInfo getOrCreateClanInfo(final int clanId)
	{
		ClanInfo clanInfo = mapClansInfo.get(clanId);

		if (clanInfo != null)
		{
			return clanInfo;
		}

		clanInfo = new ClanInfo();
		clanInfo.clanId = clanId;
		clanInfo.clanName = "-";
		clanInfo.allyName = "-";
		mapClansInfo.put(clanId, clanInfo);

		return clanInfo;
	}

	public DbObjects getDbObjects()
	{
		return dbObjects;
	}

	public void setDbObjects(DbObjects dbObjects)
	{
		this.dbObjects = dbObjects;
	}

	public void removeAll()
	{
		mapL2CharsInfo.clear();
		mapClansInfo.clear();
		mapL2Skill.clear();
		mapL2PartyChar.clear();
		mapItems.clear();
	}

	public FastMap<Integer, L2PartyChar> getMapL2PartyChar()
	{
		return mapL2PartyChar;
	}


	public void teleportClear()
	{
		mapL2CharsInfo.clear();
	}


}
