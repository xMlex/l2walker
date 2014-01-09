package fw.dbClasses;

import java.sql.*;

import javolution.util.FastMap;

public class dbManager {
	
	private Connection _con;
	private FastMap<Integer, dbItem> _items = new FastMap<Integer, dbItem>();	
	
	private static dbManager _instance = null;
	
	public static dbManager getInstance(){
		if(_instance == null)
			_instance = new dbManager();
		return _instance;
	}
	
	public dbManager(){		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			_con = DriverManager.getConnection("jdbc:sqlite:data/db/db.sqlite");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public dbItem getItem(int id){
		dbItem _obj = _items.get(id);
		if(_obj == null){
			_obj = new dbItem();
			
		}
		return _obj;
	}s
	
	private ResultSet exSQL(String q){
		
	}
	

	public static void main(String[] args) throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");
		Class.forName("com.mysql.jdbc.Driver");

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
			PreparedStatement sqlPs = _sqlServer.prepareStatement("SELECT * FROM `etcitem`;");
			int i = 0;
			if (!sqlPs.execute()) {
				rs = sqlPs.getResultSet();
				rs.last();
				int rowCount = rs.getRow(), lastPercent = -1,curPercent;
				rs.first();
				System.out.println("Start export items...");
				while (rs.next()) {
					i++;
					curPercent =(i*100/rowCount);
					if(lastPercent != curPercent){
						lastPercent = curPercent;
						System.out.println("\titem-"+curPercent+"%");
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
							+ "id integer PRIMARY KEY,"
							+ "name string,"
							+ "title string,"
							+ "level integer,"
							+ "type string,"
							+ "aggro integer,"
							+ "isUndead boolean )");
			i=0;
			sqlPs = _sqlServer.prepareStatement("SELECT * FROM `npc`;");
			if (!sqlPs.execute()) {
				rs = sqlPs.getResultSet();
				rs.last();
				int rowCount = rs.getRow(), lastPercent = -1,curPercent;
				rs.first();
				System.out.println("Start export NPC...");
				while (rs.next()) {
					i++;
					curPercent =(i*100/rowCount);
					if(lastPercent != curPercent){
						lastPercent = curPercent;
						System.out.println("\tnpc-"+curPercent+"%");
					}
					// System.out.println(rs.getString("Database"));
					_q = "insert into npc values("
							+ rs.getInt("id")
							+ ", '"
							+ rs.getString("name").replace("'", "\"")
							+ "', '" + rs.getString("title").replace("'", "\"")+ "', "
							+ rs.getInt("level")+ ", "
							+ "'" + rs.getString("type")+ "', "
							+ rs.getInt("aggro")+ ", "
							+ rs.getInt("isUndead") + ")";
					
					_sqlFileStatment.executeUpdate(_q);
					
				}
				rs.close();
				System.out.println("End export NPC...");
			}
			System.out.println("End export all.");
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage()+" Query: "+_q);
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
