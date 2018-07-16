package com.battle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="battle_quick_room_member")
public class BattleQuickRoomMember {
	
	public static final Integer IN_STATUS = 1;
	
	public static final Integer END_STATUS = 2;
	
	//主题id
	@Id
	private String id;

	@Column(name="quick_room_id")
	private String quickRoomId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column
	private Integer status;
	
	@Column
	private Integer process;
	
	@Column(name="medal_num")
	private Integer medalNum;
	
	@Column(name="img_url")
	private String imgUrl;
	
	@Column
	private String nickname;
	
	@Column(name="is_del")
	private Integer isDel;
	
	@Column(name = "create_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime createAt;
	
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

	public String getQuickRoomId() {
		return quickRoomId;
	}

	public void setQuickRoomId(String quickRoomId) {
		this.quickRoomId = quickRoomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getMedalNum() {
		return medalNum;
	}

	public void setMedalNum(Integer medalNum) {
		this.medalNum = medalNum;
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
	
	

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
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
