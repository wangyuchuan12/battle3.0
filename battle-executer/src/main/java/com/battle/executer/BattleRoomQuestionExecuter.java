package com.battle.executer;

import com.battle.executer.vo.QuestionAnswerVo;

public interface BattleRoomQuestionExecuter {
	
	public void init(ExecuterStore executerStore);
	
	public void roomEnd();
	
	public void startQuestions();
	
	public void startQuestion();
	
	public void answerQuestion(QuestionAnswerVo questionAnswer);
}
