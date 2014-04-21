package crawler.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import config.config.Config;

public class HttpOperation {

	/*
	 *  get page data
	 */
	public static String getPageContent(String url) throws Exception{
		HttpURLConnection conn = Config.getHttpConnection(url);
      	BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      	String line;
      	StringBuilder sb = new StringBuilder();
	    while ((line = rd.readLine()) != null) {
	       sb.append(line);
	    }
        rd.close();
        return sb.toString();
	}
}
