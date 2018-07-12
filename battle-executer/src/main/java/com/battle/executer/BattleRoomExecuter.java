package com.battle.executer;

import com.battle.executer.vo.QuestionAnswerVo;

public interface BattleRoomExecuter {

	public void answerQuestion(QuestionAnswerVo questionAnswer);
	
	public void signOut(String userId);
	
	public void subjectReady(String userId);
	
	public void doDouble(String userId);
	
	public void doNotDouble(String userId);
	
	public void endRoom();
	
	public void init(EventManager eventManager,ExecuterStore executerStore);

	public void subjectSelect(String subjectId, String userId);

	public void startRoom();
	
	public void submitResult();

	public void members();

}
