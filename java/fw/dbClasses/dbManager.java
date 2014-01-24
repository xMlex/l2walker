package fw.dbClasses;

import java.sql.*;

import javolution.util.FastMap;

public class dbManager {

	private Connection _con;
	private FastMap<Integer, dbItem> _items = new FastMap<Integer, dbItem>();
	private FastMap<Integer, dbSkill> _skills = new FastMap<Integer, dbSkill>();

	private static dbManager _instance = null;

	public static dbManager getInstance() {
		if (_instance == null)
			_instance = new dbManager();
		return _instance;
	}

	public dbManager() {
		try {
			Class.forName("org.sqlite.JDBC");
			_con = DriverManager
					.getConnection("jdbc:sqlite:./data/db/db.sqlite");
			// exSQL("PRAGMA locking_mode = EXCLUSIVE");
			// exSQL("PRAGMA count_changes = false");
			// exSQL("PRAGMA journal_mode = OFF");
			// exSQL("PPRAGMA temp_store = MEMORY");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public synchronized dbItem getItem(int id) {
		dbItem _obj = _items.get(id);
		if (_obj == null) {
			_obj = new dbItem();
			try {
				ResultSet rs = exSQL("select * from etcitem WHERE item_id = "
						+ id + " LIMIT 1;");
				if (rs != null && rs.next()) {
					_obj.setName(rs.getString("name"));
					_obj.setType(rs.getString("item_type"));
					_obj.setPrice(rs.getInt("price"));

					_obj.setDestroyable(rs.getBoolean("destroyable"));
					// _obj.setSellable(rs.getBoolean("sellable "));
					_obj.setDropable(rs.getBoolean("dropable"));
					_obj.setTradeable(rs.getBoolean("tradeable"));

					_obj.setIcon(rs.getString("icon"));
					_obj.setBodypart(rs.getString("bodypart"));

					System.out.println(_obj);
					_items.put(id, _obj);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return _obj;
	}
	public synchronized dbSkill getSkill(int id) {
		dbSkill _obj = _skills.get(id);
		if (_obj == null) {
			_obj = new dbSkill();
			try {
				ResultSet rs = exSQL("select * from skills WHERE skill_id = "
						+ id + " LIMIT 1;");
				if (rs != null && rs.next()) {
					_obj.setName(rs.getString("name"));
					//_obj.setType(rs.getString("item_type"));
					//_obj.setPrice(rs.getInt("price"));

					//_obj.setDestroyable(rs.getBoolean("destroyable"));
					// _obj.setSellable(rs.getBoolean("sellable "));
					//_obj.setDropable(rs.getBoolean("dropable"));
					//_obj.setTradeable(rs.getBoolean("tradeable"));

					_obj.setIcon(rs.getString("icon"));
					//_obj.setBodypart(rs.getString("bodypart"));

					System.out.println(_obj);
					_skills.put(id, _obj);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return _obj;
	}

	private ResultSet exSQL(String q) {
		ResultSet rs = null;
		Statement stmt;
		try {
			stmt = _con.createStatement();
			rs = stmt.executeQuery(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader

		dbManager.getInstance().getItem(57);

		if (true)
			return;
		Class.forName("org.sqlite.JDBC");
		Class.forName("com.mysql.jdbc.Driver");

		dbManager.getInstance().getItem(57);
		if (true)
			return;

		Connection _sqlFile = null;
		Connection _sqlServer = null;
		String _q = "";
		try {
			// create a database connection
			_sqlFile = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement _sqlFileStatment = _sqlFile.createStatement();
			_sqlFileStatment.setQueryTimeout(30); // set timeout to 30 sec.

			// create mysql connection
			_sqlServer = DriverManager
					.getConnection("jdbc:mysql://localhost/l2_interlude?user=root&password=");

			Statement _sqlServerStatment = _sqlServer.createStatement();

			_sqlFileStatment.executeUpdate("drop table if exists etcitem");
			_sqlFileStatment
					.executeUpdate("CREATE TABLE etcitem ( item_id integer PRIMARY KEY,"
							+ "name string,"
							+ "item_type string,"
							+ "price integer," + "destroyable boolean )");

			ResultSet rs = null;
			// ITEMS
			PreparedStatement sqlPs = _sqlServer
					.prepareStatement("SELECT * FROM `etcitem`;");
			int i = 0;
			if (!sqlPs.execute()) {
				rs = sqlPs.getResultSet();
				rs.last();
				int rowCount = rs.getRow(), lastPercent = -1, curPercent;
				rs.first();
				System.out.println("Start export items...");
				while (rs.next()) {
					i++;
					curPercent = (i * 100 / rowCount);
					if (lastPercent != curPercent) {
						lastPercent = curPercent;
						System.out.println("\titem-" + curPercent + "%");
					}
					// System.out.println(rs.getString("Database"));
					_q = "insert into etcitem values("
							+ rs.getInt("item_id")
							+ ", '"
							+ rs.getString("name").replace("'", "\"")
							+ "', '"
							+ rs.getString("item_type")
							+ "', "
							+ rs.getInt("price")
							+ ", "
							+ (rs.getString("destroyable").equalsIgnoreCase(
									"true") ? 1 : 0) + ")";
					_sqlFileStatment.executeUpdate(_q);

				}
				rs.close();
				System.out.println("End export items...");
			}
			// NPC
			_sqlFileStatment.executeUpdate("drop table if exists npc");
			_sqlFileStatment.executeUpdate("CREATE TABLE npc ( "
					+ "id integer PRIMARY KEY," + "name string,"
					+ "title string," + "level integer," + "type string,"
					+ "aggro integer," + "isUndead boolean )");
			i = 0;
			sqlPs = _sqlServer.prepareStatement("SELECT * FROM `npc`;");
			if (!sqlPs.execute()) {
				rs = sqlPs.getResultSet();
				rs.last();
				int rowCount = rs.getRow(), lastPercent = -1, curPercent;
				rs.first();
				System.out.println("Start export NPC...");
				while (rs.next()) {
					i++;
					curPercent = (i * 100 / rowCount);
					if (lastPercent != curPercent) {
						lastPercent = curPercent;
						System.out.println("\tnpc-" + curPercent + "%");
					}
					// System.out.println(rs.getString("Database"));
					_q = "insert into npc values(" + rs.getInt("id") + ", '"
							+ rs.getString("name").replace("'", "\"") + "', '"
							+ rs.getString("title").replace("'", "\"") + "', "
							+ rs.getInt("level") + ", " + "'"
							+ rs.getString("type") + "', " + rs.getInt("aggro")
							+ ", " + rs.getInt("isUndead") + ")";

					_sqlFileStatment.executeUpdate(_q);

				}
				rs.close();
				System.out.println("End export NPC...");
			}
			System.out.println("End export all.");
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage() + " Query: " + _q);
			e.printStackTrace();
		} finally {
			try {
				if (_sqlFile != null)
					_sqlFile.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

}
