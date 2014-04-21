package crawler.adapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FollowerWriter {
	public static final String DBDRIVER = "com.mysql.jdbc.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/tag";
	public static final String DBUSER = "root";
	public static final String DBPASS = "";

	public void run() throws Exception {
		Connection conn = null;
		Statement stmt = null;
		Class.forName(DBDRIVER);
		String sql = "INSERT INTO follower(CompanyName,FollowerID,Tags) "
				+ " VALUES ('Microsoft','1','Java')";
		conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.close(); 
		conn.close();
	}
}
