package config.config;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConfig {

	public String accept;
	public String cacheControl;
	public String connection;
	public String cookie;
	public String host;
	public String referer;
	public String userAgent;
	
	public HttpURLConnection getHttpConnection(String urlString) throws Exception{
		HttpURLConnection conn;
		//URL url = new URL("HTTP", "199.190.49.230", 8080, urlString);
		URL url = new URL(urlString);
		conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
		
		conn.setRequestProperty("Accept", this.accept);
		conn.setRequestProperty("Cache-Control", this.cacheControl);
		conn.setRequestProperty("Connection", this.connection);
		conn.setRequestProperty("Cookie", this.cookie);
		conn.setRequestProperty("Host", this.cookie);
		conn.setRequestProperty("Referer", this.referer);
		conn.setRequestProperty("User-Agent", this.userAgent);
		
		return conn;
	}
}
