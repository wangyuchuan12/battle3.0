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
@Table(name="battle_question_distribution_stage")
public class BattleQuestionDistributionStage {
	@Id
	private String id;
	
	@Column(name="distribution_id")
	private String distributionId;
	
	@Column(name="stage_index")
	private Integer stageIndex;
	
	@Column(name="subject_count")
	private Integer subjectCount;

	//平均每个主题几道题目，不是实际题目数量
	@Column(name="average_question_count")
	private Integer averageQuestionCount;
	
	//实际答题题目数量
	@Column(name="question_count")
	private Integer questionCount;
	
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

	public String getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}
	
	

	public Integer getSubjectCount() {
		return subjectCount;
	}

	public void setSubjectCount(Integer subjectCount) {
		this.subjectCount = subjectCount;
	}

	public Integer getAverageQuestionCount() {
		return averageQuestionCount;
	}

	public void setAverageQuestionCount(Integer averageQuestionCount) {
		this.averageQuestionCount = averageQuestionCount;
	}
	
	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
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
