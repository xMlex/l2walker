package fw.dbClasses;

import java.util.List;

import javolution.util.FastMap;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

public class DbObjects
{
	ObjectContainer db = null;

	FastMap<Integer, DbL2ItemBase> dbL2ItemCache = new FastMap<Integer, DbL2ItemBase>();
	FastMap<Integer, DbL2Npc> dbL2NpcCache = new FastMap<Integer, DbL2Npc>();
	FastMap<Integer, DbL2Skill> dbL2SkillCache = new FastMap<Integer, DbL2Skill>();

	public DbObjects()
	{
		Db4o.configure().allowVersionUpdates(true);
		db = Db4o.openFile("data/db/db.db4o");
		Db4o.configure().objectClass(DbL2Armor.class).objectField("item_id").indexed(true);
		Db4o.configure().objectClass(DbL2Weapon.class).objectField("item_id").indexed(true);
		Db4o.configure().objectClass(DbL2Item.class).objectField("item_id").indexed(true);
		Db4o.configure().objectClass(DbL2Npc.class).objectField("idTemplate").indexed(true);
		Db4o.configure().objectClass(DbL2Skill.class).objectField("skill_id").indexed(true);
	}

	public void close()
	{
		db.close();
	}

	public DbL2ItemBase getDbL2Item(final int item_id)
	{
		//String item_id_str = String.valueOf(item_id);

		DbL2ItemBase dbL2Item = dbL2ItemCache.get(item_id);

		if (dbL2Item != null)
		{
			return dbL2Item;
		}

		/*{
			List<DbL2Item> listResult = db.query(new Predicate<DbL2Item>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(DbL2Item item)
				{
					return item.getItem_id() == item_id;
				}
			});

			if (listResult.size() > 0)
			{
				dbL2Item = listResult.get(0);
				dbL2ItemCache.put(item_id, dbL2Item);
				return dbL2Item;
			}
		}*/

		/*{
			List<DbL2Weapon> listResult = db.query(new Predicate<DbL2Weapon>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(DbL2Weapon item)
				{
					return item.getItem_id() == item_id;
				}
			});

			if (listResult.size() > 0)
			{
				dbL2Item = listResult.get(0);
				dbL2ItemCache.put(item_id, dbL2Item);
				return dbL2Item;
			}
		}*/

		/*{
			List<DbL2Armor> listResult = db.query(new Predicate<DbL2Armor>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(DbL2Armor item)
				{
					return item.getItem_id() == item_id;
				}
			});

			if (listResult.size() > 0)
			{
				dbL2Item = listResult.get(0);
				dbL2ItemCache.put(item_id, dbL2Item);
				return dbL2Item;
			}
		}*/

		dbL2Item = new DbL2ItemBase();
		dbL2Item.item_id = item_id;
		dbL2Item.name = "Unknow";
		dbL2ItemCache.put(item_id, dbL2Item);

		return dbL2Item;
	}

	public DbL2Npc getDbL2Npc(final int idTemplate)
	{
		//String idTemplate_str = String.valueOf(idTemplate);
		DbL2Npc dbL2Npc = dbL2NpcCache.get(idTemplate);
		if (dbL2Npc != null)
		{
			return dbL2Npc;
		}
/*
		List<DbL2Npc> listNpcs = db.query(new Predicate<DbL2Npc>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(DbL2Npc npc)
			{
				return npc.getIdTemplate() == idTemplate;
			}
		});

		if (listNpcs.size() > 0)
		{
			dbL2Npc = listNpcs.get(0);
			dbL2NpcCache.put(idTemplate, dbL2Npc);
			return dbL2Npc;
		}


		dbL2Npc = new DbL2Npc();
		dbL2Npc.idTemplate = idTemplate;
		dbL2NpcCache.put(idTemplate, dbL2Npc);*/
		return dbL2Npc;
	}

	public DbL2Skill getDbL2Skill(final int skill_id)
	{
		//String skill_id_str = String.valueOf(skill_id);
		DbL2Skill dbL2Skill = dbL2SkillCache.get(skill_id);
		if (dbL2Skill != null)
		{
			return dbL2Skill;
		}
/*
		List<DbL2Skill> listSkills = db.query(new Predicate<DbL2Skill>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(DbL2Skill skill)
			{
				return skill.skill_id == skill_id;
			}
		});

		if (listSkills.size() > 0)
		{
			dbL2Skill = listSkills.get(0);
			dbL2SkillCache.put(skill_id, dbL2Skill);
			return dbL2Skill;
		}

		dbL2Skill = new DbL2Skill();
		dbL2Skill.skill_id = skill_id;
		dbL2SkillCache.put(skill_id, dbL2Skill);*/
		return dbL2Skill;
	}


}
