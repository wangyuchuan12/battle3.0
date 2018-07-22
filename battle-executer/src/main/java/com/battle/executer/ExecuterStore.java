package com.battle.executer;

public interface ExecuterStore {
	public BattleRoomExecuter getBattleRoomExecuter();
	public BattleRoomPublish getBattleRoomPublish();
	public BattleDataManager getBattleDataManager();
	public BattleRoomQuestionExecuter getBattleQuestionExecuter();
	public BattleRoomStageExecuter getBattleRoomStageExecuter();
	public ScheduledExecuter getScheduledExecuter();
	public BattleEndHandle getBattleEndHandle();
	public BattleQuestionManager getBattleQuestionManager();
	public BattleDataRoomManager getBattleDataRoomManager();
	public BattleRoomMemberTakepart getBattleRoomMemberTakepart();
	public EndJudge getEndJudge();
}
