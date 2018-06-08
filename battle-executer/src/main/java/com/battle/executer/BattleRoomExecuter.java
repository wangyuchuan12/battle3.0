package com.battle.executer;

import com.battle.executer.vo.QuestionAnswerVo;
import com.wyc.common.wx.domain.UserInfo;

public interface BattleRoomExecuter {

	public void answerQuestion(QuestionAnswerVo questionAnswer);
	
	public void signOut(String userId);
	
	public void startStage(Integer stageIndex);
	
	public void subjectReady(String userId);
	
	public void init(BattleRoomDataManager battleRoomDataManager,BattleRoomPublish battleRoomPublish,BattleRoomQuestionExecuter battleRoomQuestionExecuter);
}
