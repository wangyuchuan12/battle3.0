package com.battle.executer;

import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleEndHandleException;

public interface BattleEndHandle {

	public void end(BattleDataManager battleRoomDataManager);
	
}
