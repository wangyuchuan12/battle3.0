package com.battle.socket;

import java.util.List;

public class MessageVo {

	public final static int ALL_ONLINE_TYPE = 0;
	public final static int ROOM_TYPE = 1;
	public final static int USERS_TYPE = 2;
	
	
	public final static String SHOW_SUBJECTS = "showSubjects";
	
	public final static String SHOW_SUBJECT_STATUS = "showSubjectStatus";
	public final static String PUBLISH_SHOW_QUESTION = "publishShowQuestion";
	public final static String PUBLISH_DO_ANSWER = "publishDoAnswer";
	public final static String PUBLISH_REST = "publishRest";
	public final static String PUBLISH_ROOM_END = "publishRoomEnd";
	public final static String PUBLISH_ROOM_START = "publishRoomStart";
	public final static String PUBLISH_MEMBER_STATUS = "publishMemberStatus";
	public final static String PUBLISH_DO_SELECT_SUBJECT = "publishDoSelectSubject";
	public final static String WAIT_STATUS_CODE = "wait_status_code";
	public final static String SHOW_ANSWER_PLAYERS = "showAnswerPlayers";
	
	public final static String PUBLISH_REWARD = "publish_reward";
	
	public final static String PUBLISH_DIE = "publish_die";
	
	public final static String PUBLISH_MEMBERS = "publish_members";
	
	public final static String PUBLISH_BATTLE_WAIT_ROOM= "publish_battle_wait_room";
	
	public final static String WAIT_ROOM_MEMBER_STATUS_CODE= "waitRoomMemberStatusCode";
	
	//调用编号
	private String code;
	
	//调用类型 0表示 全范围 1表示某个房间 2表示userId结合
	private Integer type;
	
	//调用数据
	private Object data;
	
	//房间名称
	private String roomId;
	
	private List<String> excludeUserIds;
	
	//用户数据
	private List<String> userIds;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public List<String> getExcludeUserIds() {
		return excludeUserIds;
	}

	public void setExcludeUserIds(List<String> excludeUserIds) {
		this.excludeUserIds = excludeUserIds;
	}
}
