package com.battle.executer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.battle.executer.vo.QuestionAnswerVo;

@Service
public  class BattleRoomConnector {
	private Map<String, BattleRoomExecuter> battleRoomExecuterMap = new ConcurrentHashMap<>();
	
	public void registerExecuter(String roomId,BattleRoomExecuter battleRoomExecuter){
		battleRoomExecuterMap.put(roomId, battleRoomExecuter);
	}
	
	public void answerQuestion(QuestionAnswerVo questionAnswer){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(questionAnswer.getRoomId());
		battleRoomExecuter.answerQuestion(questionAnswer);
	}
	
	public void signOut(String roomId,String userId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.signOut(userId);
	}
	
	public void startStage(String roomId,Integer stageIndex){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.startStage(stageIndex);
	}
	
	public void subjectReady(String roomId,String userId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.subjectReady(userId);
	}
}
