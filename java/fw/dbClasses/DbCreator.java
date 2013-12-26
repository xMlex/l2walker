package fw.dbClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class DbCreator
{
	static ObjectContainer db = null;

	static Connection con = null;

	public static void main(String[] args)
	{
		System.out.println("INIT CREATE DB");
		openDb();
		 
		try
		{
			importData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		closeDb();
		System.out.println("END CREATE DB");
	}

	private static void openDb()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			db = Db4o.openFile("data/db/db.db4o");		
			con = DriverManager.getConnection("jdbc:mysql:///l2jdbc4", "root", "fox");
			
			Db4o.configure().objectClass(DbL2Armor.class).objectField("item_id").indexed(true);
			Db4o.configure().objectClass(DbL2Weapon.class).objectField("item_id").indexed(true);
			Db4o.configure().objectClass(DbL2Item.class).objectField("item_id").indexed(true);
			Db4o.configure().objectClass(DbL2Npc.class).objectField("idTemplate").indexed(true);
			Db4o.configure().objectClass(DbL2Skill.class).objectField("skill_id").indexed(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void closeDb()
	{
		try
		{
			db.close();
			con.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void importData() throws Exception
	{
		importDataItens();
		importDataWeapons();
		importDataArmors();
		importDataNpcs();
		importSkills();
	}

	private static void importDataItens() throws Exception
	{
		Statement stmt = con.createStatement();

		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * FROM etcItem");

		// Loop through the result set
		while (rs.next())
		{
			DbL2Item item = new DbL2Item();
			item.setItem_id(rs.getInt("item_id"));
			item.setName(rs.getString("name"));
			item.setConsume_type(rs.getString("consume_type"));
			item.setItem_type(rs.getString("item_type"));

			db.set(item);
		}

		// Close the result set, statement and the connection
		rs.close();
	}

	private static void importDataWeapons() throws Exception
	{
		Statement stmt = con.createStatement();

		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * FROM weapon");

		// Loop through the result set
		while (rs.next())
		{
			DbL2Weapon item = new DbL2Weapon();
			item.setItem_id(rs.getInt("item_id"));
			item.setName(rs.getString("name"));
			item.setBodyPart(rs.getString("bodypart"));
			item.setSoulShots(rs.getInt("soulshots"));
			item.setSpiritShots(rs.getInt("spiritshots"));
			item.setCrystalType(rs.getString("crystal_type"));
			item.setP_Atack(rs.getInt("p_dam"));
			item.setM_Atack(rs.getInt("m_dam"));
			item.setRnd_dam(rs.getInt("rnd_dam"));
			item.setWeaponType(rs.getString("weaponType"));
			item.setAtackSpeed(rs.getInt("atk_speed"));

			db.set(item);
		}

		// Close the result set, statement and the connection
		rs.close();
	}

	private static void importDataNpcs() throws Exception
	{
		Statement stmt = con.createStatement();

		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * FROM npc");

		// Loop through the result set
		while (rs.next())
		{
			DbL2Npc item = new DbL2Npc();
			item.idTemplate = rs.getInt("idTemplate");
			item.name = rs.getString("name");
			item.npcClass = rs.getString("class");
			item.type = rs.getString("type").substring(2, rs.getString("type").length());
			item.isUndead = rs.getInt("isUndead");

			db.set(item);
		}

		// Close the result set, statement and the connection
		rs.close();
	}

	private static void importDataArmors() throws Exception
	{
		Statement stmt = con.createStatement();

		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * FROM armor");

		// Loop through the result set
		while (rs.next())
		{
			DbL2Armor item = new DbL2Armor();
			item.item_id = rs.getInt("item_id");
			item.name = rs.getString("name");
			item.bodyPart = rs.getString("bodyPart");
			item.crystalType = rs.getString("crystal_Type");
			item.armorType = rs.getString("armor_Type");
			item.p_Def = rs.getInt("p_Def");
			item.m_Def = rs.getInt("m_Def");
			item.mp_bonus = rs.getInt("mp_bonus");

			db.set(item);
		}

		// Close the result set, statement and the connection
		rs.close();
	}
	
	private static void importSkills() throws Exception
	{
		Statement stmt = con.createStatement();

		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT distinct skill_id,name FROM skill_trees");

		// Loop through the result set
		while (rs.next())
		{
			DbL2Skill skill = new DbL2Skill();
			skill.skill_id= rs.getInt("skill_id");
			skill.name=rs.getString("name");
			db.set(skill);
		}

		// Close the result set, statement and the connection
		rs.close();
	}

}
