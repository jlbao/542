package crawler.adapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class CompanyWriter {
	static final String DBDRIVER = "com.mysql.jdbc.Driver";
	static final String DBURL = "jdbc:mysql://localhost:3306/tag";
	static final String DBUSER = "root";
	static final String DBPASS = "";
	static String fileName;
	
	public CompanyWriter(String fileName){
		CompanyWriter.fileName = fileName;
	}
	
	public void run() throws Exception {
		Connection conn = null;
		Statement stmt = null;
		Class.forName(DBDRIVER);
		conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		stmt = conn.createStatement();
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		HashMap<String, String> map = new HashMap<String, String>();
		String str;
		while ((str = br.readLine()) != null) {
			String company = str.split("\t")[0];
			String tags = str.split("\t")[1];
			map.put(company, tags);
		}
		br.close();
		fr.close();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String sqlSelect = "SELECT CompanyName from company WHERE CompanyName = '" + entry.getKey() + "' LIMIT 0, 1";
			
			ResultSet rs = stmt.executeQuery(sqlSelect);
			boolean empty = true;
			while(rs.next()){
				empty = false;
			}
			if(empty){
				String sqlInsert = "INSERT INTO company(CompanyName,TopTags) "
						+ " VALUES ('" + entry.getKey() + "','" + entry.getValue()
						+ "')";
				stmt.executeUpdate(sqlInsert);
			}else{
				String sqlUpdate = "UPDATE company SET TopTags='" + entry.getValue() + "'" + "WHERE CompanyName='" + entry.getKey() + "'";
				stmt.executeUpdate(sqlUpdate);
			}
			rs.close();
		}
		stmt.close();
		conn.close();
	}
}
