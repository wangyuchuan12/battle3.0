package com.battle.executer.vo;

import java.util.List;

public class BattlePaperVo {
	private String id;
	private Integer stageIndex;
	
	private List<BattleStageVo> battleStages;
	
	private List<BattlePaperQuestionVo> battlePaperQuestions;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStageIndex() {
		return stageIndex;
	}
	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}
	public List<BattleStageVo> getBattleStages() {
		return battleStages;
	}
	public void setBattleStages(List<BattleStageVo> battleStages) {
		this.battleStages = battleStages;
	}
	public List<BattlePaperQuestionVo> getBattlePaperQuestions() {
		return battlePaperQuestions;
	}
	
	public BattleStageVo currentStage(){
		for(BattleStageVo battleStageVo:battleStages){
			if(battleStageVo.getStageIndex().intValue()==stageIndex.intValue()){
				return battleStageVo;
			}
		}
		return null;
	}
	
	public void setBattlePaperQuestions(List<BattlePaperQuestionVo> battlePaperQuestions) {
		this.battlePaperQuestions = battlePaperQuestions;
	}
}
