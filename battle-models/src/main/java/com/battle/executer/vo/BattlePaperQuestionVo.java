package com.battle.executer.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattlePaperQuestionVo {
	private String questionId;
	private String name;
	private String imgUrl;
	private Integer seq;
	private String battleSubjectId;
	private String battleId;
	private String battlePeriodId;
	private String periodStageId;
	private String roomId;
	//0选择题 1填空题 2填词题
	private Integer type;
	private String question;
	//答案（把可见部分的答案提取出来放进去）
	private String answer;
	private String rightAnswer;
	private Integer questionIndex;
	private String fillWords;
	private Integer timeLong;
	
	
	private List<BattlePaperOptionVo> options = Collections.synchronizedList(new ArrayList<BattlePaperOptionVo>());
	
	private List<QuestionAnswerVo> questionAnswerVos = Collections.synchronizedList(new ArrayList<QuestionAnswerVo>());
	
	
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
	
	public List<BattlePaperOptionVo> getOptions() {
		return options;
	}
	public Integer getQuestionIndex() {
		return questionIndex;
	}
	public void setQuestionIndex(Integer questionIndex) {
		this.questionIndex = questionIndex;
	}
	public List<QuestionAnswerVo> getQuestionAnswerVos() {
		return questionAnswerVos;
	}
	public String getFillWords() {
		return fillWords;
	}
	public void setFillWords(String fillWords) {
		this.fillWords = fillWords;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public Integer getTimeLong() {
		return timeLong;
	}
	public void setTimeLong(Integer timeLong) {
		this.timeLong = timeLong;
	}
	
	
}
