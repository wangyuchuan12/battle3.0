package com.battle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wyc.annotation.ParamAnnotation;

@Entity
@Table(name="battle_room_member_cool")
public class BattleRoomCoolMember {
	public static final Integer STATUS_IN = 1;
	public static final Integer STATUS_COMPLETE = 2;

	@Id
	private String id;
	
	@Column
	private Integer schedule;
    
	@Column(name="cool_love_seq")
	private Integer coolLoveSeq;
	
	@Column(name="user_id")
	private String userId;
	
	@Column
    private Integer status;
	
	@Column(name="room_id")
	private String roomId;
	
	@Column(name="start_datetime")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime startDatetime;
	
	@Column(name="love_count")
	private Integer loveCount;
	
	@Column(name="love_limit")
	private Integer loveLimit;
	
	@Column(name="upper_limit")
	private Integer upperLimit;
	
	@Column
	private Integer millisec;
	
	@Column
	private Integer unit;
	
	@ParamAnnotation
	@Column(name = "create_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime createAt;
	
	@ParamAnnotation
    @Column(name = "update_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime updateAt;

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

	public DateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(DateTime createAt) {
		this.createAt = createAt;
	}

	public DateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(DateTime updateAt) {
		this.updateAt = updateAt;
	}
}
