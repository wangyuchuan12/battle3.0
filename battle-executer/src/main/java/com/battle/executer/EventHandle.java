package com.battle.executer;

public interface EventHandle {

	public void restEndEvent();
	
	public void startRoomEvent();
	
	public void startQuestions();
	
	public void submitResult();
	
	public void roomEnd();
	
	public void init(ExecuterStore executerStore);
}
