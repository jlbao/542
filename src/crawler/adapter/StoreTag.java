package crawler.adapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import config.config.Config;
import config.config.Task;

public class StoreTag implements Runnable {

	public static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/tag";
	public static final String DBUSER = "root";
	public static final String DBPASS = "";

	// store the data into the database
	public StoreTag() {

	}

	@Override
	public void run() {
		Connection conn = null;
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (!(Config.CompanyQueue.isEmpty() && Config.CurrentCompany == null)) {
			try {
				if (!Config.StoreQueue.isEmpty()) {
					Task task = Config.StoreQueue.poll();

					Statement stmt = null;

					String followerID = task.userID;
					String companyName = Config.CurrentCompany.companyName;
					String tags = task.tags;

					if(!tags.isEmpty()){
						String sql = "INSERT INTO follower(FollowerID, CompanyName,Tags,Datetime) "
								+ " VALUES ('"
								+ followerID
								+ "','"
								+ companyName
								+ "','" + tags + "',NOW())";
	
						stmt = conn.createStatement();
						stmt.executeUpdate(sql);
						stmt.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// close connection
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
