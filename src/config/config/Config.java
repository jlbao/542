package config.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import crawler.tools.ThreadPool;

/*
 * Config for the crawler
 */

public class Config {

	public final static HttpConfig httpConfig = new HttpConfig();
	public final static ConcurrentLinkedQueue<Company> CompanyQueue = new ConcurrentLinkedQueue<Company>();
	
	// this queue need to be crawled data, and put them into the storeQueue
	public final static ConcurrentLinkedQueue<Task> TaskQueue = new ConcurrentLinkedQueue<Task>();
	
	// this queue need to be stored into the database 
	public final static ConcurrentLinkedQueue<Task> StoreQueue = new ConcurrentLinkedQueue<Task>();
	
	public final static int TaskNumPerCompany = 1000;
	public final static ThreadPool Pool = new ThreadPool(100, 200, 30);
	public static Company CurrentCompany;
	
	private Config(){}

	public static void config(){
		try {
			configHttp();
			configCompany();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void configHttp() throws Exception{
		Properties p = new Properties();
		File f = new File("./HttpConfig.properties");
		p.load(new FileInputStream(f));
		httpConfig.accept = p.getProperty("Accept");
		httpConfig.cacheControl = p.getProperty("Cache-Control");
		httpConfig.connection = p.getProperty("Connection");
		httpConfig.cookie = p.getProperty("Cookie");
		httpConfig.host = p.getProperty("Host");
		httpConfig.referer = p.getProperty("Referer");
		httpConfig.userAgent = p.getProperty("User-Agent");
		httpConfig.connection = p.getProperty("Connection");
	}
	
	// store http config
	public static void saveHttpConfig(){
		Properties p = new Properties();
		File f = new File("./httpConfig.properties");
		try {
			String cookieString = "bcookie=\"v=2&e2179dd5-a078-45cc-86ad-cf1db8fae15a\"; __qca=P0-1060873076-1384747104000; visit=\"v=1&M\"; X-LI-IDC=C1; JSESSIONID=\"ajax:5963468495909667384\"; L1c=5d3f00e8; L1l=67117164; _lipt=\"0_swTsi0cghLZKl4sNSUok_9Dw8ANvPFCLmFUl611FnvDY9_XV0vJse6mb-1r3XMI29I_5ibHShkEg3dVQolwQWhbXofpVT-lPvlPYsSp525bNp4BaK7Gdeg6EllxBzEc0\"; L1e=469ad62c; lihc_auth_en=1386689024; leo_auth_token=\"LIM:308176431:a:1386689183:5acbc82ab186316d061d8b61ed0a15e29fc2371a\"; lidc=\"b=VB99:g=31:u=1:i=1386689183:t=1386775583:s=576262050\"; sdsc=22%3A1%2C1386689205385%7EMBR2%2C0wfQurgQcTZEdDf4Uraq2VYVy9JA%3D; __utma=23068709.1040337267.1386689039.1386689039.1386689039.1; __utmb=23068709.16.10.1386689039; __utmc=23068709; __utmz=23068709.1386689039.1.1.utmcsr=help.linkedin.com|utmccn=(referral)|utmcmd=referral|utmcct=/app/ask/path/hr/contacts.email/baojialiang_share@163.com; __utmv=23068709.user; RT=s=1386689267416&r=http%3A%2F%2Fwww.linkedin.com%2Fnhome%2Fget-started; lang=\"v=2&lang=en-us&c=\"";
			p.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			p.put("Cache-Control", "max-age=0");
			p.put("Connection", "keep-alive");
			p.put("Cookie", cookieString);
			p.put("Host", "www.linkedin.com");
			p.put("Referer", "http://www.linkedin.com/company/google/followers?page_num=1&trk=extra_biz_followers");
			p.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
			p.store(new FileOutputStream(f), "httpRequest cookie data");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// get http Connection
	public synchronized static HttpURLConnection getHttpConnection(String urlString) throws Exception{
		return httpConfig.getHttpConnection(urlString);
	}
	
	// get company queue config
	static void configCompany(){
		CompanyQueue.add(new Company("linkedin"));
		CompanyQueue.add(new Company("vmware"));
		CompanyQueue.add(new Company("twitter"));
		CompanyQueue.add(new Company("oracle"));
		CompanyQueue.add(new Company("dropbox"));
		CompanyQueue.add(new Company("apple"));
		CompanyQueue.add(new Company("dell"));
		CompanyQueue.add(new Company("amd"));
		CompanyQueue.add(new Company("nvidia"));
		CompanyQueue.add(new Company("qualcomm"));
		
		CompanyQueue.add(new Company("huawei"));
		CompanyQueue.add(new Company("ibm"));
		CompanyQueue.add(new Company("nokia"));
		CompanyQueue.add(new Company("samsung"));
		CompanyQueue.add(new Company("cisco"));
		CompanyQueue.add(new Company("ebay"));
		CompanyQueue.add(new Company("yahoo"));
		CompanyQueue.add(new Company("careerbuilder"));
		CompanyQueue.add(new Company("monster"));
		CompanyQueue.add(new Company("texas-instruments"));
		CompanyQueue.add(new Company("lenovo"));
		CompanyQueue.add(new Company("asus"));
		CompanyQueue.add(new Company("tencent"));
		CompanyQueue.add(new Company("htc"));
		CompanyQueue.add(new Company("sony"));
		CompanyQueue.add(new Company("seagate-technology"));

		CompanyQueue.add(new Company("amazon"));
		CompanyQueue.add(new Company("google"));
		CompanyQueue.add(new Company("microsoft"));
		CompanyQueue.add(new Company("facebook"));
		CompanyQueue.add(new Company("emc"));
		switchCompany();
	}
	
	// switch company
	public synchronized static void switchCompany(){
		CurrentCompany = CompanyQueue.poll();
		System.out.println();
		System.out.println("-------------switch to " + CurrentCompany.companyName + "-------------");
		System.out.println();
	}
	
	
}
