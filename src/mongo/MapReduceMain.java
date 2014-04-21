package mongo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;

public class MapReduceMain {

	/*
	 * Use map reduce to calculate the most popular tags for all company
	 */
	public static void main(String[] args) throws Exception {
		
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("company");
		
		String map = getMapCommand();
		String reduce = getReduceCommand();
		String finalize = getFinalizeCommand();

		MapReduceCommand cmd = new MapReduceCommand(coll, map, reduce,  
			     null, MapReduceCommand.OutputType.INLINE, null);  
			  
		cmd.setFinalize(finalize);
		
		MapReduceOutput out = coll.mapReduce(cmd);  
		
		/*
		 * print the logs
		 */
		PrintWriter output = new PrintWriter("result.txt");
		for (DBObject o : out.results()) {  
			DBObject val = (BasicDBObject) o.get("value");
			String companyName = o.get("_id").toString();
			String tags = val.get("tags").toString();
			System.out.println(companyName);
			System.out.println(tags);
			output.println(companyName + "\t" + tags);
		}  
		output.close();
	}
	
	/*
	 * load Map script from file
	 */
	public static String getMapCommand() throws FileNotFoundException{
		File mapFile = new File("ext_src/map.js");
		String map = new Scanner(mapFile).useDelimiter("\\Z").next();
		return map;
	}
	
	/*
	 * load Reduce script from file
	 */
	public static String getReduceCommand() throws FileNotFoundException{
		File reduceFile = new File("ext_src/reduce.js");
		String reduce = new Scanner(reduceFile).useDelimiter("\\Z").next();
		return reduce;
	}
	
	/*
	 * load Finalize script from file
	 */
	public static String getFinalizeCommand() throws FileNotFoundException{
		File finalizeFile = new File("ext_src/finalize.js");
		String finalize = new Scanner(finalizeFile).useDelimiter("\\Z").next();
		return finalize;
	}
	
}
