package com.battle.executer.vo;

public class BattleRoomVo {

	private String id;
	
	private Integer num;
	
	private Integer rangeGogal;
	
	private String battleId;
	
	private String periodId;
	
	private Integer stageCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getRangeGogal() {
		return rangeGogal;
	}

	public void setRangeGogal(Integer rangeGogal) {
		this.rangeGogal = rangeGogal;
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

	public Integer getStageCount() {
		return stageCount;
	}

	public void setStageCount(Integer stageCount) {
		this.stageCount = stageCount;
	}
}
