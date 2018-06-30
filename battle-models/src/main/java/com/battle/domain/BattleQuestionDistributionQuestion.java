package com.battle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wyc.annotation.IdAnnotation;
import com.wyc.annotation.ParamAnnotation;


@Entity
@Table(name="battle_question_distribution_question")
public class BattleQuestionDistributionQuestion {

	@Id
	@IdAnnotation
	private String id;
	
	@Column(name="distribution_id")
	private String distributionId;
	
	@Column(name="distribution_subject_id")
	private String distributionSubjectId;
	
	@Column(name="distribution_stage_id")
	private String distributionStageId;
	
	//引用问题
	@ParamAnnotation
	@Column(name="question_id")
	private String questionId;
	
	//问题名称
	@ParamAnnotation
	@Column
	private String name;
	
	//图片地址
	@ParamAnnotation
	@Column(name="img_url")
	private String imgUrl;
	
	//序号
	@ParamAnnotation
	@Column
	private Integer seq;
	
	//主题
	@ParamAnnotation
	@Column(name="battle_subject_id")
	private String battleSubjectId;
	
	//比赛引用id
	@ParamAnnotation
	@Column(name="battle_id")
	private String battleId;
	
	//期数id
	@ParamAnnotation
	@Column(name="battle_period_id")
	private String battlePeriodId;
	
	//阶段id
	@ParamAnnotation
	@Column(name="period_stage_id")
	private String periodStageId;
	
	
	//0选择题 1填空题 2填词题
	@Column
	@ParamAnnotation
	private Integer type;
	
	@Column
	@ParamAnnotation
	private String question;
	
	//答案（把可见部分的答案提取出来放进去）
	@Column
	@ParamAnnotation
	private String answer;
	
	//正确答案
	@Column(name="right_answer")
	@ParamAnnotation
	private String rightAnswer;
	
	//选项
	@Column
	@ParamAnnotation
	private String options;
	
	@Column(name="is_del")
	@ParamAnnotation
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

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
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

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getBattleSubjectId() {
		return battleSubjectId;
	}

	public void setBattleSubjectId(String battleSubjectId) {
		this.battleSubjectId = battleSubjectId;
	}

	public String getBattleId() {
		return battleId;
	}

	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}

	public String getBattlePeriodId() {
		return battlePeriodId;
	}

	public void setBattlePeriodId(String battlePeriodId) {
		this.battlePeriodId = battlePeriodId;
	}

	public String getPeriodStageId() {
		return periodStageId;
	}

	public void setPeriodStageId(String periodStageId) {
		this.periodStageId = periodStageId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
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

	public String getDistributionSubjectId() {
		return distributionSubjectId;
	}

	public void setDistributionSubjectId(String distributionSubjectId) {
		this.distributionSubjectId = distributionSubjectId;
	}

	public String getDistributionStageId() {
		return distributionStageId;
	}

	public void setDistributionStageId(String distributionStageId) {
		this.distributionStageId = distributionStageId;
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
