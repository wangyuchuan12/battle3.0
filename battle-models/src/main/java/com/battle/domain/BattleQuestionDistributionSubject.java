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
@Table(name="battle_question_distribution_subject")
public class BattleQuestionDistributionSubject {
	@Id
	private String id;
	
	@Column(name="distribution_id")
	private String distributionId;
	
	@Column(name="distribution_stage_id")
	private String distributionStageId;
	
	@Column
	private String name;
	
	@Column(name="img_url")
	private String imgUrl;
	
	@Column(name="stage_index")
	private Integer stageIndex;
	
	@Column(name="z_index")
	private Integer index;
	
	@Column(name="battle_subject_id")
	private String battleSubjectId;
	
	@Column(name="is_del")
	private Integer isDel;
	
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	
	public String getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

	public String getDistributionStageId() {
		return distributionStageId;
	}

	public void setDistributionStageId(String distributionStageId) {
		this.distributionStageId = distributionStageId;
	}
	
	

	public String getBattleSubjectId() {
		return battleSubjectId;
	}

	public void setBattleSubjectId(String battleSubjectId) {
		this.battleSubjectId = battleSubjectId;
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
