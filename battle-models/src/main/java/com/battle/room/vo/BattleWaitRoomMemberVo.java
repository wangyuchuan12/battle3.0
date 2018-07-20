package com.battle.room.vo;

import org.joda.time.DateTime;

public class BattleWaitRoomMemberVo {
	public static final Integer FREE_STATUS = 1;
		
	public static final Integer READY_STATUS = 2;
	
	public static final Integer BATTLE_STATUS = 3;
	
	public static final Integer END_STATUS = 4;
	
	public static final Integer OUT_STATUS = 5;

	private String id;
	
	private String roomId;
	
	private String userId;
	
	private String nickname;
	
	private String imgUrl;

	private Integer status;
	
	private Integer isOwner;
	
	private String token;
	
	private Integer isEnd;
	
	private String endContent;
	
	private DateTime ownerTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(Integer isOwner) {
		this.isOwner = isOwner;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	public String getEndContent() {
		return endContent;
	}

	public void setEndContent(String endContent) {
		this.endContent = endContent;
	}

	public DateTime getOwnerTime() {
		return ownerTime;
	}

	public void setOwnerTime(DateTime ownerTime) {
		this.ownerTime = ownerTime;
	}
}
