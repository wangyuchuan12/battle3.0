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


//比赛问题
@ParamEntityAnnotation
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="battle_question")
public class BattleQuestion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@IdAnnotation
	private String id;
	
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
	@Column(name="subject_id")
	private String subjectId;
	
	//比赛引用id
	@ParamAnnotation
	@Column(name="battle_id")
	private String battleId;
	
	//期数id
	@ParamAnnotation
	@Column(name="period_id")
	private String periodId;
	
	
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
	
	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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
