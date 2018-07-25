package com.battle.executer;

import com.battle.executer.exception.BattleDataManagerException;

public interface EventHandle {

	public void restEndEvent() throws BattleDataManagerException;
	
	public void startRoomEvent() throws BattleDataManagerException;
	
	public void startQuestions() throws BattleDataManagerException;
	
	public void submitResults() throws BattleDataManagerException;
	
	public void submitResult();
	
	public void publishDie() throws BattleDataManagerException;
	
	public void roomEnd() throws BattleDataManagerException;
	
	public void myInfo() throws BattleDataManagerException;
	
	public void init(ExecuterStore executerStore) throws BattleDataManagerException;
}
