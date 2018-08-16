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
@Table(name="battle_redpack_task")
public class BattleRedpackTask {
	
	public static final Integer BEAN_REWARD_TYPE = 0;
	
	public static final Integer RANK_TYPE = 0;
	public static final Integer DAN_TYPE = 1;
	@Id
	private String id;
	
	//红包id
	@Column(name="redpack_id")
	private String redpackId;
	
	//任务类型
	@Column
	private Integer type;
	
	//奖励类型
	@Column(name="reward_type")
	private Integer rewardType;
	
	//智慧豆数量
	@Column(name="bean_num")
	private Integer beanNum;
	
	@Column(name="z_index")
	private Integer index;
	
	@Column(name="rank_id")
	private String rankId;
	
	@Column
	private Integer process;
	
	@Column
	private String name;
	
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRewardType() {
		return rewardType;
	}

	public void setRewardType(Integer rewardType) {
		this.rewardType = rewardType;
	}

	public Integer getBeanNum() {
		return beanNum;
	}

	public void setBeanNum(Integer beanNum) {
		this.beanNum = beanNum;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}
	

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
