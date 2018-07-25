package com.battle.executer;

import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.BattleRoomExecuterException;
import com.battle.executer.exception.BattleRoomMemberTakepartException;
import com.battle.executer.exception.BattleRoomQuestionExecuterException;
import com.battle.executer.exception.BattleRoomStageExceptionException;
import com.battle.executer.exception.EndJudgeException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.wyc.common.wx.domain.UserInfo;

public interface BattleRoomExecuter {

	public void answerQuestion(QuestionAnswerVo questionAnswer) throws BattleRoomExecuterException, BattleRoomQuestionExecuterException, BattleDataManagerException;
	
	public void signOut(String userId) throws BattleRoomExecuterException, BattleDataManagerException, BattleDataRoomManagerException;
	
	public void subjectReady(String userId) throws BattleRoomExecuterException;
	
	public void doDouble(String userId)  throws BattleRoomExecuterException;
	
	public void doNotDouble(String userId)  throws BattleRoomExecuterException;
	
	public void endRoom()  throws BattleRoomExecuterException, BattleDataManagerException, BattleDataRoomManagerException, PublishException;
	
	public void init(EventManager eventManager,ExecuterStore executerStore)  throws BattleRoomExecuterException, PublishException, BattleDataManagerException, BattleDataRoomManagerException, BattleQuestionManagerException, EndJudgeException, BattleRoomStageExceptionException;

	public void subjectSelect(String subjectId, String userId)  throws BattleRoomExecuterException;

	public void startRoom()  throws BattleRoomExecuterException, PublishException, BattleDataManagerException, BattleDataRoomManagerException, BattleQuestionManagerException, EndJudgeException, BattleRoomStageExceptionException;
	
	public void submitResults()  throws BattleRoomExecuterException;

	public void members()  throws BattleRoomExecuterException;
	
	public BattleRoomMemberVo takepart(UserInfo userInfo) throws BattleRoomExecuterException, BattleRoomMemberTakepartException, BattleDataManagerException, BattleQuestionManagerException, EndJudgeException, BattleRoomStageExceptionException, BattleDataRoomManagerException;
	
	public BattleRoomVo getRoom() throws BattleRoomExecuterException, BattleDataManagerException, BattleDataRoomManagerException;

	public boolean superLove(UserInfo userInfo) throws BattleRoomExecuterException, BattleDataManagerException, BattleDataRoomManagerException;

	public void submitResult() throws BattleRoomExecuterException, BattleDataManagerException, BattleQuestionManagerException, BattleDataRoomManagerException;

}
