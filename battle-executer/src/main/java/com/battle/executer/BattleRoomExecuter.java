package com.battle.executer;

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

	public void members();
	
	public BattleRoomMemberVo takepart(UserInfo userInfo);
	
	public BattleRoomVo getRoom();

	public boolean superLove(UserInfo userInfo);

	public void submitResult();

}
