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

	public void answerQuestion(QuestionAnswerVo questionAnswer);
	
	public void signOut(String userId);
	
	public void subjectReady(String userId);
	
	public void doDouble(String userId);
	
	public void doNotDouble(String userId);
	
	public void endRoom();
	
	public void init(EventManager eventManager,ExecuterStore executerStore);

	public void subjectSelect(String subjectId, String userId);

	public void startRoom();
	
	public void submitResults();

	public void roomInfo();
	
	public BattleRoomMemberVo takepart(UserInfo userInfo);
	
	public BattleRoomVo getRoom();

	public boolean superLove(UserInfo userInfo);

	public void submitResult();

	public int share(String userId);
}
