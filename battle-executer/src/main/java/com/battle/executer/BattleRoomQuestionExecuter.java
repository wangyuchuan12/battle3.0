package com.battle.executer;

import com.battle.exception.SendMessageException;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.BattleRoomQuestionExecuterException;
import com.battle.executer.exception.BattleRoomStageExceptionException;
import com.battle.executer.exception.EndJudgeException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.vo.QuestionAnswerVo;

public interface BattleRoomQuestionExecuter {
	
	public void init(ExecuterStore executerStore)throws BattleRoomQuestionExecuterException;
	
	public void roomEnd()throws BattleRoomQuestionExecuterException;
	
	public void startQuestions() throws BattleRoomQuestionExecuterException, BattleDataManagerException, BattleQuestionManagerException, EndJudgeException, PublishException, BattleDataRoomManagerException, SendMessageException, BattleRoomStageExceptionException;
	
	public void startQuestion() throws BattleRoomQuestionExecuterException, BattleDataManagerException, BattleQuestionManagerException, EndJudgeException, PublishException, BattleDataRoomManagerException, SendMessageException, BattleRoomStageExceptionException;
	
	public void answerQuestion(QuestionAnswerVo questionAnswer) throws BattleRoomQuestionExecuterException, BattleDataManagerException;
}
