package com.battle.executer.vo;

public class BattleRoomMemberVo {
	private String id;
	private Integer remainLove;
	private Integer limitLove;
	private Integer processGogal;
	private String roomId;
	private String battleId;
	private String periodId;
	private Integer status;
	private String imgUrl;
	private String nickname;
	private String userId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getRemainLove() {
		return remainLove;
	}
	public void setRemainLove(Integer remainLove) {
		this.remainLove = remainLove;
	}
	public Integer getLimitLove() {
		return limitLove;
	}
	public void setLimitLove(Integer limitLove) {
		this.limitLove = limitLove;
	}
	public Integer getProcessGogal() {
		return processGogal;
	}
	public void setProcessGogal(Integer processGogal) {
		this.processGogal = processGogal;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getBattleId() {
		return battleId;
	}
	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}
	public String getPeriodId() {
		return periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
