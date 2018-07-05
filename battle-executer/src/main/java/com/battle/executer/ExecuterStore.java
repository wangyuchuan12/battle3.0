package com.battle.executer;

public interface ExecuterStore {
	public BattleRoomExecuter getBattleRoomExecuter();
	public BattleRoomPublish getBattleRoomPublish();
	public BattleRoomDataManager getBattleRoomDataManager();
	public BattleRoomQuestionExecuter getBattleQuestionExecuter();
	public BattleRoomStageExecuter getBattleRoomStageExecuter();
	public ScheduledExecuter getScheduledExecuter();
	public BattleEndHandle getBattleEndHandle();
}
