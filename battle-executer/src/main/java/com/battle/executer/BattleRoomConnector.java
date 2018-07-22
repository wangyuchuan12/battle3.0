package com.battle.executer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.wyc.common.wx.domain.UserInfo;
@Service
public  class BattleRoomConnector{
	private Map<String, BattleRoomExecuter> battleRoomExecuterMap = new ConcurrentHashMap<>();

	
	final static Logger logger = LoggerFactory.getLogger(BattleRoomConnector.class);
	
	public void registerExecuter(final String roomId,BattleRoomExecuter battleRoomExecuter){
		
		
		
		System.out.println("***********************...........registerExecuter........roomId:"+roomId+",battleRoomExecuter:"+battleRoomExecuter);
		battleRoomExecuterMap.put(roomId, battleRoomExecuter);
		
		/*scheduledExecutorService.schedule(new Runnable() {
			
			@Override
			public void run() {
				endRoom(roomId);
				logger.debug("auto end room,the is is {}",roomId);
				
				
			}
		}, 1, TimeUnit.HOURS);*/
		
	
	}
	
	public List<BattleRoomMemberVo> members(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		if(battleRoomExecuter!=null){
			List<BattleRoomMemberVo> battleRoomMembers = battleRoomExecuter.getRoom().getMembers();
			return battleRoomMembers;
		}else{
			return null;
		}
	}
	
	public BattleRoomMemberVo takepart(String roomId,UserInfo userInfo){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		if(battleRoomExecuter!=null){
			BattleRoomMemberVo battleRoomMemberVo = battleRoomExecuter.takepart(userInfo);
			return battleRoomMemberVo;
		}else{
			return null;
		}
	}
	
	public boolean superLove(String roomId,UserInfo userInfo){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		if(battleRoomExecuter!=null){
			return battleRoomExecuter.superLove(userInfo);
		}else{
			return false;
		}
	}
	
	public void removeExecuter(final String roomId){
		battleRoomExecuterMap.remove(roomId);
		//scheduledExecutorService.shutdown();
	}
	
	
	public void startRoom(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.startRoom();
	}
	
	public void answerQuestion(QuestionAnswerVo questionAnswer){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(questionAnswer.getRoomId());
		battleRoomExecuter.answerQuestion(questionAnswer);
	}
	
	public void signOut(String roomId,String userId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		
		if(battleRoomExecuter!=null){
			battleRoomExecuter.signOut(userId);
		}
	}
	
	public void subjectReady(String roomId,String userId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.subjectReady(userId);
	}
	

	public void doDouble(String roomId,String userId) {
		
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.doDouble(userId);
		
	}


	public void doNotDouble(String roomId,String userId) {
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.doNotDouble(userId);
		
	}
	
	public void endRoom(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		battleRoomExecuter.endRoom();
		battleRoomExecuterMap.remove(roomId);
	}
	
	public boolean isInProgress(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		if(battleRoomExecuter!=null){
			return true;
		}else{
			return false;
		}
	}
	
	public BattleRoomVo getRoom(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		if(battleRoomExecuter!=null){
			return battleRoomExecuter.getRoom();
		}else{
			return null;
		}
	}

	public void subjectSelect(String roomId, String subjectId, String userId) {
		
		
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		
		
		battleRoomExecuter.subjectSelect(subjectId,userId);
	}
}
