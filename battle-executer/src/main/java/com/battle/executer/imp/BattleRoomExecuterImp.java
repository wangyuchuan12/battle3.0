package com.battle.executer.imp;

import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.vo.QuestionAnswerVo;

public class BattleRoomExecuterImp implements BattleRoomExecuter{

	private BattleRoomDataManager battleRoomDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	@Override
	public void answerQuestion(QuestionAnswerVo questionAnswer) {
		battleRoomQuestionExecuter.answerQuestion(questionAnswer);
	}

	@Override
	public void signOut(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(BattleRoomDataManager battleRoomDataManager, BattleRoomPublish battleRoomPublish,
			BattleRoomQuestionExecuter battleRoomQuestionExecuter) {
		this.battleRoomDataManager = battleRoomDataManager;
		this.battleRoomPublish = battleRoomPublish;
		this.battleRoomQuestionExecuter = battleRoomQuestionExecuter;
	}

	@Override
	public void startStage(Integer stageIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subjectReady(String userId) {
		// TODO Auto-generated method stub
		
	}

}
