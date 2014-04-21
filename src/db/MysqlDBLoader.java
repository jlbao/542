package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MysqlDBLoader {
	
	/*
	 * get the follower List from database
	 */
	public static List<Follower> getFollowerlist() throws Exception{
		Connection conn = DBConnector.getInstance().getConn();
		String sql = "select * from follower";
		PreparedStatement pstatement = conn.prepareStatement(sql);
		ResultSet rs = pstatement.executeQuery();
		List<Follower> followerList = new LinkedList<Follower>();
		while(rs.next()){
			String followerID = rs.getString("FollowerID");
			String companyName = rs.getString("CompanyName");
			String[] tags = rs.getString("Tags").split(",");
			List<String> tagList = new LinkedList<String>();
			for(String tag : tags){
				tagList.add(tag);
			}
			followerList.add(new Follower(followerID, companyName, tagList));
		}
		return followerList;
	}	
}
