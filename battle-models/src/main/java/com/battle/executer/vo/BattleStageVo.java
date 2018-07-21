package com.battle.executer.vo;

import java.util.List;
import java.util.Map;

public class BattleStageVo {
	
	public static final Integer STATUS_FREE = 0;
	
	public static final Integer STATUS_IN = 0;
	
	public static final Integer STATUS_END = 0;

	private String id;
	
	private List<BattlePaperSubjectVo> battlePaperSubjects;
	
	private Map<String, List<BattlePaperQuestionVo>> battlePaperQuestions;
	
	private List<BattlePaperQuestionVo> selectBattlePaperQuestions;
	
	private Integer stageIndex;
	
	private Integer status;
	
	private Integer questionCount;
	
	private Integer subjectCount;
	
	private Integer timeLong;
	
	private Integer questionIndex;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BattlePaperSubjectVo> getBattlePaperSubjects() {
		return battlePaperSubjects;
	}

	public void setBattlePaperSubjects(List<BattlePaperSubjectVo> battlePaperSubjects) {
		this.battlePaperSubjects = battlePaperSubjects;
	}

	public Map<String, List<BattlePaperQuestionVo>> getBattlePaperQuestions() {
		return battlePaperQuestions;
	}

	public void setBattlePaperQuestions(Map<String, List<BattlePaperQuestionVo>> battlePaperQuestions) {
		this.battlePaperQuestions = battlePaperQuestions;
	}

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getSubjectCount() {
		return subjectCount;
	}

	public void setSubjectCount(Integer subjectCount) {
		this.subjectCount = subjectCount;
	}

	public Integer getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(Integer timeLong) {
		this.timeLong = timeLong;
	}
	
	public BattlePaperQuestionVo currentQuestion(){
		if(questionIndex<selectBattlePaperQuestions.size()){
			return selectBattlePaperQuestions.get(questionIndex);
		}else{
			return null;
		}
	}

	public List<BattlePaperQuestionVo> getSelectBattlePaperQuestions() {
		return selectBattlePaperQuestions;
	}

	public void setSelectBattlePaperQuestions(List<BattlePaperQuestionVo> selectQuestions) {
		
		this.selectBattlePaperQuestions = selectQuestions;
		
	}

	public Integer getQuestionIndex() {
		return questionIndex;
	}

	public void setQuestionIndex(Integer questionIndex) {
		this.questionIndex = questionIndex;
	}
}
