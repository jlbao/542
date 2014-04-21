package config.config;

import java.util.ArrayList;

import crawler.tools.Parser;


public class Company {
	public String companyName;
	public String companyURL;
	public int currentPage;
	public int taskNum;
	
	public Company(String companyName){
		this.companyName = companyName;
		this.companyURL = String.format("http://www.linkedin.com/company/%s/followers", this.companyName);
		this.currentPage = 1;
	}
	
	public boolean increaseTaskNum(){
		taskNum++;
		if(taskNum > Config.TaskNumPerCompany)
			return false;
		return true;
	}

	public void increaseCurrentPage(){
		this.currentPage++;
	}
	
	
	public String getCurrentURL() {
		return String.format("http://www.linkedin.com/company/%s/followers?page_num=%s", this.companyName, this.currentPage);
	}
	
	// use regular expression to put the tasks into the task queue
	public void putTasks(String pageContent){
		// parse
		try{
			ArrayList<Task> list = Parser.parseFollowerList(pageContent);
			if(list.isEmpty()){
				System.out.println(pageContent);
			}
			for(Task task : list){
				Config.TaskQueue.add(task);
				
				// if task num exceed the maximum
				if(!increaseTaskNum()){
					return;
				}
			}
			increaseCurrentPage();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
