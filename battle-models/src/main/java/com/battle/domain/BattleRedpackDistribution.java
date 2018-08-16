package com.battle.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wyc.annotation.ParamAnnotation;

@Entity
@Table(name="battle_redpack_distribution")
public class BattleRedpackDistribution {
	
	public static final Integer UN_RECEIVE_STATUS = 0;
	public static final Integer RECEIVED_STATUS = 1;
	@Id
	private String id;
	
	@Column(name = "redpack_id")
	private String redpackId;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_img")
	private String userImg;
	
	@Column
	private String nickname;
	
	@Column
	private BigDecimal amount;
	
	@Column(name = "receive_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime receiveAt;
	
	@Column
	private Integer status;
	
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

	public String getRedpackId() {
		return redpackId;
	}

	public void setRedpackId(String redpackId) {
		this.redpackId = redpackId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public DateTime getReceiveAt() {
		return receiveAt;
	}

	public void setReceiveAt(DateTime receiveAt) {
		this.receiveAt = receiveAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
