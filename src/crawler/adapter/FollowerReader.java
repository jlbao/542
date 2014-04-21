package crawler.adapter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FollowerReader {
	public static final String DBDRIVER = "com.mysql.jdbc.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/tag";
	public static final String DBUSER = "root";
	public static final String DBPASS = "";

	public String run() throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT CompanyName,FollowerID,Tags from follower";
		Class.forName(DBDRIVER);
		conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		String fileName = "followerTags.txt";
		FileWriter fw = new FileWriter(fileName);
		BufferedWriter bw = new BufferedWriter(fw);
		while (rs.next()) {
			String companyName = rs.getString("CompanyName");
			String followerID = rs.getString("FollowerID");
			String tags = rs.getString("Tags");
			String output = companyName + "," + followerID + "," + tags;
			bw.write(output);
			if (!rs.isLast()) {
				bw.write("\n");
			}
		}
		bw.close();
		fw.close();
		rs.close();
		stmt.close();
		conn.close();
		return fileName;
	}
}
