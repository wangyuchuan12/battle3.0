package com.battle.executer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.executer.vo.QuestionAnswerVo;
@Service
public  class BattleRoomConnector {
	private Map<String, BattleRoomExecuter> battleRoomExecuterMap = new ConcurrentHashMap<>();
	
	@Autowired
	private ScheduledExecutorService executorService; 
	
	final static Logger logger = LoggerFactory.getLogger(BattleRoomConnector.class);
	
	public void registerExecuter(final String roomId,BattleRoomExecuter battleRoomExecuter){
		
		
		System.out.println("................registerExecuter");
		System.out.println("...........roomId:"+roomId);
		battleRoomExecuterMap.put(roomId, battleRoomExecuter);
		
		executorService.schedule(new Runnable() {
			
			@Override
			public void run() {
				endRoom(roomId);
				logger.debug("auto end room,the is is {}",roomId);
				
				
			}
		}, 1, TimeUnit.HOURS);
	
	}
	
	public void removeExecuter(final String roomId){
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		System.out.println("...........battleRoomExecuter:"+battleRoomExecuter);
		battleRoomExecuterMap.remove(roomId);
		battleRoomExecuter = null;
		System.gc();
	}
	
	
	public void startRoom(String roomId){
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.startRoom();
	}
	
	public void answerQuestion(QuestionAnswerVo questionAnswer){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(questionAnswer.getRoomId());
		battleRoomExecuter.answerQuestion(questionAnswer);
	}
	
	public void signOut(String roomId,String userId){
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.signOut(userId);
	}
	
	public void subjectReady(String roomId,String userId){
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.subjectReady(userId);
	}
	

	public void doDouble(String roomId,String userId) {
		
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.doDouble(userId);
		
	}


	public void doNotDouble(String roomId,String userId) {
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.doNotDouble(userId);
		
	}
	
	public void endRoom(String roomId){
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.endRoom();
		battleRoomExecuterMap.remove(roomId);
	}

	public void subjectSelect(String roomId, String subjectId, String userId) {
		System.out.println("...........roomId:"+roomId);
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.subjectSelect(subjectId,userId);
	}
}
