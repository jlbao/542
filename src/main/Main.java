package main;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;

public class Main {

	public static void main(String[] args) throws Exception {
		
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("testCollection");
		
		
		File mapFile = new File("ext_src/map.js");
		String map = new Scanner(mapFile).useDelimiter("\\Z").next();
		
		File reduceFile = new File("ext_src/reduce.js");
		String reduce = new Scanner(reduceFile).useDelimiter("\\Z").next();

		File finalizeFile = new File("ext_src/finalize.js");
		String finalize = new Scanner(finalizeFile).useDelimiter("\\Z").next();

		MapReduceCommand cmd = new MapReduceCommand(coll, map, reduce,  
			     null, MapReduceCommand.OutputType.INLINE, null);  
			  
		//cmd.setFinalize(finalize);
		
		MapReduceOutput out = coll.mapReduce(cmd);  
		
		System.out.println(out);
		
		for (DBObject o : out.results()) {  
			System.out.println(o.toString());  
		}  
		
	}

}
