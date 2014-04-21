package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {
	public static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/tag";
	public static final String DBUSER = "root";
	public static final String DBPASS = "";
	public Connection conn;
	private static DBConnector dbConnector;
	
	private DBConnector() {
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DBConnector getInstance(){
		if(dbConnector == null){
			dbConnector = new DBConnector(); 
		}
		return dbConnector;
	}
	
	public Connection getConn(){
		return this.conn;
	}

}