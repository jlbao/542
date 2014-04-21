package crawler.tools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import config.config.Task;


public class Parser {

	/**
	 * parse data from the pages we crawled
	 * @throws Exception 
	 */
	
	// return the task urls
	public static ArrayList<Task> parseFollowerList(String str) throws Exception{
		String regex = "<p class=\"fn n\"><a href=\"/profile/view\\u003Fid=([0-9a-zA-Z]+)&amp;authType=name&amp;authToken=([0-9a-zA-Z]+)";
        ArrayList<Task> list = new ArrayList<Task>();
        
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(str);
        
        while (matcher.find()) {
        	String userID = matcher.group(1);
        	String token = matcher.group(2);
        	String url = String.format("http://www.linkedin.com/profile/view?id=%s&authType=name&authToken=%s&trk=hb_upphoto", userID, token);
            if(!userID.isEmpty()){
            	Task task = new Task(userID, url);
	        	list.add(task); 
            }
        }  
        return list;
	}
	
	// get tags from task page
	public static String getTags(String str) throws Exception{
		String regex = "name\":\"([\\s\\w\\\\]+)\",\"endorsementCount\":";
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
        	String tag = matcher.group(1);
        	if(tag.contains("\\u002d")){
        		tag = tag.replaceAll(Pattern.quote("\\u002d"), "-");
        	}
            sb.append(tag + ",");
        }
        if(sb.length() != 0)
        	sb.replace(sb.length() - 1,sb.length() , "");
        return sb.toString();
	}
	
}
