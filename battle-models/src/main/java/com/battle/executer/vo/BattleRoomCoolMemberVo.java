package com.battle.executer.vo;

import javax.persistence.Column;
import javax.persistence.Id;

import org.joda.time.DateTime;

public class BattleRoomCoolMemberVo {
	public static final Integer STATUS_IN = 1;
	public static final Integer STATUS_COMPLETE = 2;


	private String id;
	
	private Integer schedule;
    
	private Integer coolLoveSeq;
	
    private Integer status;
	
	private String roomId;
	
	private DateTime startDatetime;
	
	private Integer loveCount;
	
	private Integer loveLimit;
	
	private Integer upperLimit;
	
	private Integer millisec;
	
	private Integer unit;
	
	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSchedule() {
		return schedule;
	}

	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}

	public Integer getCoolLoveSeq() {
		return coolLoveSeq;
	}

	public void setCoolLoveSeq(Integer coolLoveSeq) {
		this.coolLoveSeq = coolLoveSeq;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public DateTime getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(DateTime startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Integer getLoveCount() {
		return loveCount;
	}

	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}

	public Integer getLoveLimit() {
		return loveLimit;
	}

	public void setLoveLimit(Integer loveLimit) {
		this.loveLimit = loveLimit;
	}

	public Integer getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Integer upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Integer getMillisec() {
		return millisec;
	}

	public void setMillisec(Integer millisec) {
		this.millisec = millisec;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
