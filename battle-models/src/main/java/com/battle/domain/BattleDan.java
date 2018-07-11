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

@ParamEntityAnnotation
@Entity
@Table(name="battle_dan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BattleDan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Integer STATUS_FREE = 0;
	
	public static Integer STATUS_IN = 1;
	
	public static Integer STATUS_SUCCESS = 2;
	
	public static Integer STATUS_FAIL = 3;
	@Id
	@IdAnnotation
	private String id;
	
	
	@ParamAnnotation
	@Column
	private String name;
	
	@ParamAnnotation
	@Column
	private Integer level;
	
	@ParamAnnotation
	@Column(name="point_id")
	private String pointId;
	
	@ParamAnnotation
	@Column(name="img_url")
	private String imgUrl;
	
	
	@ParamAnnotation
	@Column(name="score_gogal")
	private Integer scoreGogal;
	
	@ParamAnnotation
	@Column(name="process_gogal")
	private Integer processGogal;
	
	@ParamAnnotation
	@Column(name="battle_id")
	private String battleId;
	
	@ParamAnnotation
	@Column(name="period_id")
	private String periodId;
	
	@ParamAnnotation
	@Column(name="group_id")
	private String groupId;
	
	//名额数量
	@ParamAnnotation
	@Column
	private Integer places;
	
	@ParamAnnotation
	@Column(name="sign_1_bean_cost")
	private Integer sign1BeanCost;
	
	@ParamAnnotation
	@Column(name="sign_2_bean_cost")
	private Integer sign2BeanCost;
	
	@ParamAnnotation
	@Column(name="sign_3_bean_cost")
	private Integer sign3BeanCost;
	
	@ParamAnnotation
	@Column(name="sign_4_bean_cost")
	private Integer sign4BeanCost;
	
	@ParamAnnotation
	@Column(name="max_num")
	private Integer maxNum;
	
	@ParamAnnotation
	@Column(name="min_num")
	private Integer minNum;
	
	@ParamAnnotation
	@Column(name="time_long")
	private Integer timeLong;
	
	
	@ParamAnnotation
	@Column(name="love_count")
	private Integer loveCount;
	
	private Integer rewardBean1;
	
	private Integer rewardBean2;
	
	private Integer rewardBean3;
	
	private Integer rewardBean4;
	
	private Integer rewardBean5;
	
	private Integer rewardBean6;
	
	private Integer rewardBean7;
	
	private Integer rewardBean8;
	
	private Integer rewardBean9;
	
	private Integer rewardBean10;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getScoreGogal() {
		return scoreGogal;
	}

	public void setScoreGogal(Integer scoreGogal) {
		this.scoreGogal = scoreGogal;
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

	public Integer getPlaces() {
		return places;
	}

	public void setPlaces(Integer places) {
		this.places = places;
	}
	
	public Integer getSign1BeanCost() {
		return sign1BeanCost;
	}

	public void setSign1BeanCost(Integer sign1BeanCost) {
		this.sign1BeanCost = sign1BeanCost;
	}

	public Integer getSign2BeanCost() {
		return sign2BeanCost;
	}

	public void setSign2BeanCost(Integer sign2BeanCost) {
		this.sign2BeanCost = sign2BeanCost;
	}

	public Integer getSign3BeanCost() {
		return sign3BeanCost;
	}

	public void setSign3BeanCost(Integer sign3BeanCost) {
		this.sign3BeanCost = sign3BeanCost;
	}

	public Integer getSign4BeanCost() {
		return sign4BeanCost;
	}

	public void setSign4BeanCost(Integer sign4BeanCost) {
		this.sign4BeanCost = sign4BeanCost;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	
	public Integer getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(Integer timeLong) {
		this.timeLong = timeLong;
	}

	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	
	public Integer getLoveCount() {
		return loveCount;
	}

	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}
	
	public Integer getProcessGogal() {
		return processGogal;
	}

	public void setProcessGogal(Integer processGogal) {
		this.processGogal = processGogal;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	

	public Integer getRewardBean1() {
		return rewardBean1;
	}

	public void setRewardBean1(Integer rewardBean1) {
		this.rewardBean1 = rewardBean1;
	}

	public Integer getRewardBean2() {
		return rewardBean2;
	}

	public void setRewardBean2(Integer rewardBean2) {
		this.rewardBean2 = rewardBean2;
	}

	public Integer getRewardBean3() {
		return rewardBean3;
	}

	public void setRewardBean3(Integer rewardBean3) {
		this.rewardBean3 = rewardBean3;
	}

	public Integer getRewardBean4() {
		return rewardBean4;
	}

	public void setRewardBean4(Integer rewardBean4) {
		this.rewardBean4 = rewardBean4;
	}

	public Integer getRewardBean5() {
		return rewardBean5;
	}

	public void setRewardBean5(Integer rewardBean5) {
		this.rewardBean5 = rewardBean5;
	}

	public Integer getRewardBean6() {
		return rewardBean6;
	}

	public void setRewardBean6(Integer rewardBean6) {
		this.rewardBean6 = rewardBean6;
	}

	public Integer getRewardBean7() {
		return rewardBean7;
	}

	public void setRewardBean7(Integer rewardBean7) {
		this.rewardBean7 = rewardBean7;
	}

	public Integer getRewardBean8() {
		return rewardBean8;
	}

	public void setRewardBean8(Integer rewardBean8) {
		this.rewardBean8 = rewardBean8;
	}

	public Integer getRewardBean9() {
		return rewardBean9;
	}

	public void setRewardBean9(Integer rewardBean9) {
		this.rewardBean9 = rewardBean9;
	}

	public Integer getRewardBean10() {
		return rewardBean10;
	}

	public void setRewardBean10(Integer rewardBean10) {
		this.rewardBean10 = rewardBean10;
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
