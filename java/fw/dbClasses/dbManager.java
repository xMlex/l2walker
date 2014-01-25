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

					//System.out.println(_obj);
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
					_obj.setIcon(rs.getString("icon"));
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

		dbItem _item = dbManager.getInstance().getItem(6529);
		System.out.println(_item+" BodyPart: "+_item.getBodypart()+" "+(_item.isWeapon()));

		
	}

}
