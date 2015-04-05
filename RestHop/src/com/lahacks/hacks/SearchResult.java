package com.lahacks.hacks;

public class SearchResult {
	private String GroupID;
	private String GroupName;
    
	public SearchResult() {}
    
	public SearchResult(String GroupID, String GroupName) {
		this.GroupID = GroupID;
		this.GroupName = GroupName;
	}
    
	public String getGroupID() {
		return GroupID;
	}
	
	public void setGroupID(String GroupID) {
		this.GroupID = GroupID;
	}
	
	public String getGroupName() {
		return GroupName;
	}
	
	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}
}
