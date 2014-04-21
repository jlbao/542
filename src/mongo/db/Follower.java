package mongo.db;

import java.util.List;

public class Follower {
	/*
	 * Follower contains all tag information and company information
	 */
	
	private String followerID;
	private String companyName;
	private List<String> tags;
	
	public Follower(String followerID, String companyName, List<String> tags){
		this.followerID = followerID;
		this.companyName = companyName;
		this.setTags(tags);
	}
	
	public String getFollowerID() {
		return followerID;
	}
	public void setFollowerID(String followerID) {
		this.followerID = followerID;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}
