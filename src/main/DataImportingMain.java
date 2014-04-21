package main;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import db.Follower;
import db.MysqlDBLoader;

public class DataImportingMain {

	/*
	 * import the data from MySQL to MongoDB
	 */
	public static void main(String[] args) throws Exception{
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("testCollection");
		
		List<Follower> followerList = MysqlDBLoader.getFollowerlist();
		
		for(Follower follower : followerList){
			BasicDBObject doc = new BasicDBObject("followerID", follower.getFollowerID())
				.append("companyName", follower.getCompanyName());
			coll.insert(doc);
		}
		
		/*
		
		for(Follower follower : followerList){
			BasicDBObject doc = new BasicDBObject("followerID", follower.getFollowerID())
			.append("companyName", follower.getCompanyName())
			.append("tags", follower.getTags());
			coll.insert(doc);
		}
		*/
	}
	

}
