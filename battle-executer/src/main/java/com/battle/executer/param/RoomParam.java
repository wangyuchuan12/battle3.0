package com.battle.executer.param;

import java.util.List;
import java.util.Map;

public class RoomParam {

	private String groupId;

	private List<UserParam> userParams;
	
	private Integer type;
	
	private Map<String, Object> data;
	
	
	
	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<UserParam> getUserParams() {
		return userParams;
	}

	public void setUserParams(List<UserParam> userParams) {
		this.userParams = userParams;
	}
}
