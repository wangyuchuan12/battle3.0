package com.battle.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wyc.annotation.IdAnnotation;
import com.wyc.annotation.ParamAnnotation;
import com.wyc.annotation.ParamEntityAnnotation;

@Entity
@Table(name="user_status")
@ParamEntityAnnotation
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
public class UserStatus implements Serializable{
	
	public final static Integer FREE_STATUS = 0;
	
	public final static Integer IN_STATUS = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@IdAnnotation
	private String id;
	
	@ParamAnnotation
	@Column(name="user_id",unique=true)
	private String userId;
	
	@ParamAnnotation
	@Column(name="is_line")
	private Integer isLine;
	
	@ParamAnnotation
	@Column
	private String token;
	
	@ParamAnnotation
	@Column(name="on_line_count")
	private Integer onLineCount;
	
	@ParamAnnotation
	@Column(name="down_line_count")
	private Integer downLineCount;
	
	
	@ParamAnnotation
	@Column(name="room_id")
	private String roomId;
	
	
	//0表示游离状态  1表示战斗中
	@ParamAnnotation
	@Column
	private Integer status;
	
	@ParamAnnotation
	@Column(name = "on_line_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
	private DateTime onLineAt;
	
	@ParamAnnotation
	@Column(name = "down_line_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
	private DateTime downLineAt;
	
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsLine() {
		return isLine;
	}

	public void setIsLine(Integer isLine) {
		this.isLine = isLine;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public DateTime getOnLineAt() {
		return onLineAt;
	}

	public void setOnLineAt(DateTime onLineAt) {
		this.onLineAt = onLineAt;
	}

	public DateTime getDownLineAt() {
		return downLineAt;
	}

	public void setDownLineAt(DateTime downLineAt) {
		this.downLineAt = downLineAt;
	}
	
	

	public Integer getOnLineCount() {
		return onLineCount;
	}

	public void setOnLineCount(Integer onLineCount) {
		this.onLineCount = onLineCount;
	}

	public Integer getDownLineCount() {
		return downLineCount;
	}

	public void setDownLineCount(Integer downLineCount) {
		this.downLineCount = downLineCount;
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

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
