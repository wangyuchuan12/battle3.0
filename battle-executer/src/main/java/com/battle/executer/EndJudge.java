package com.battle.executer;

import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.EndJudgeException;

public interface EndJudge {

	public boolean isEnd();
	
	public void init(BattleDataManager battleDataManager);
}
