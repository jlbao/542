package test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Mongo mongo;

		try {
			mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("library");

			DBCollection books = db.getCollection("books");
			
			/*
			for(int i = 0; i < 1000; i++){
				BasicDBObject book = new BasicDBObject();
				book.put("name", "Understanding JAVA" + i);
				book.put("pages", 100);
				books.insert(book);
			}
			*/
			/*
			book = new BasicDBObject();
			book.put("name", "Understanding JSON");
			book.put("pages", 200);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding XML");
			book.put("pages", 300);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding Web Services");
			book.put("pages", 400);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding Axis2");
			book.put("pages", 150);
			books.insert(book);
			*/

			String map = "function() { " + "var category; "
					+ "if ( this.pages >= 250 ) " + "category = 'Big Books'; "
					+ "else " + "category = 'Small Books'; "
					+ "emit(category, {name: this.name});}";

			String reduce = "function(key, values) { " + "var sum = 0; "
					+ "values.forEach(function(doc) { " + "sum += 1; " + "}); "
					+ "return {books: sum};} ";

			MapReduceCommand cmd = new MapReduceCommand(books, map, reduce,
					null, MapReduceCommand.OutputType.INLINE, null);

			MapReduceOutput out = books.mapReduce(cmd);
			
			System.out.println(out);
			
			for (DBObject o : out.results()) {
				System.out.println(o.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
