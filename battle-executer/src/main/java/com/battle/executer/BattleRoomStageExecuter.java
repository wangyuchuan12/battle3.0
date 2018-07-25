package com.battle.executer;

import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleRoomStageExceptionException;

public interface BattleRoomStageExecuter {
	public void init(ExecuterStore executerStore)throws BattleRoomStageExceptionException, BattleDataManagerException;
	public void startStage() throws BattleRoomStageExceptionException;
}
