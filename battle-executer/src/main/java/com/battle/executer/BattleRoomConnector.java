package com.battle.executer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
@Service
public  class BattleRoomConnector{
	
	public BattleRoomConnector(){
		
		System.out.println("...........BattleRoomConnector:"+this);
	}
	private Map<String, BattleRoomExecuter> battleRoomExecuterMap = new ConcurrentHashMap<>();

	
	final static Logger logger = LoggerFactory.getLogger(BattleRoomConnector.class);
	
	public void registerExecuter(final String roomId,BattleRoomExecuter battleRoomExecuter){
		
		
		
		System.out.println("***********************...........registerExecuter........roomId:"+roomId+",battleRoomExecuter:"+battleRoomExecuter);
		battleRoomExecuterMap.put(roomId, battleRoomExecuter);
		
		
		System.out.println("...................battleRoomExecuterMap:"+battleRoomExecuterMap);
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
			List<BattleRoomMemberVo> battleRoomMembers = null;
			try {
				battleRoomMembers = battleRoomExecuter.getRoom().getMembers();
			} catch (BattleRoomExecuterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataRoomManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return battleRoomMembers;
		}else{
			return null;
		}
	}
	
	public BattleRoomMemberVo takepart(String roomId,UserInfo userInfo){
		
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		
		System.out.println(".............battleRoomExecuter:"+battleRoomExecuter+",roomId:"+roomId);
		System.out.println("............battleRoomExecuterMap:"+battleRoomExecuterMap);
		if(battleRoomExecuter!=null){
			BattleRoomMemberVo battleRoomMemberVo = null;
			try {
				battleRoomMemberVo = battleRoomExecuter.takepart(userInfo);
			} catch (BattleRoomExecuterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleRoomMemberTakepartException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleQuestionManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EndJudgeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleRoomStageExceptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataRoomManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(".............battleRoomMemberVo:"+battleRoomMemberVo);
			return battleRoomMemberVo;
		}else{
			return null;
		}
	}
	
	public boolean superLove(String roomId,UserInfo userInfo){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		if(battleRoomExecuter!=null){
			try {
				return battleRoomExecuter.superLove(userInfo);
			} catch (BattleRoomExecuterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataRoomManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return false;
		}
		return false;
	}
	
	public void removeExecuter(final String roomId){
		
		System.out.println(".......................BattleRoomConnector.remove");
		battleRoomExecuterMap.remove(roomId);
		//scheduledExecutorService.shutdown();
	}
	
	
	public void startRoom(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		try {
			battleRoomExecuter.startRoom();
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleDataManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleDataRoomManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleQuestionManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EndJudgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleRoomStageExceptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void answerQuestion(QuestionAnswerVo questionAnswer){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(questionAnswer.getRoomId());
		try {
			battleRoomExecuter.answerQuestion(questionAnswer);
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleRoomQuestionExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleDataManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void signOut(String roomId,String userId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		
		if(battleRoomExecuter!=null){
			try {
				battleRoomExecuter.signOut(userId);
			} catch (BattleRoomExecuterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataRoomManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void subjectReady(String roomId,String userId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		try {
			battleRoomExecuter.subjectReady(userId);
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void doDouble(String roomId,String userId) {
		
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		try {
			battleRoomExecuter.doDouble(userId);
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void doNotDouble(String roomId,String userId) {
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		try {
			battleRoomExecuter.doNotDouble(userId);
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void endRoom(String roomId){
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		try {
			battleRoomExecuter.endRoom();
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleDataManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BattleDataRoomManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			try {
				return battleRoomExecuter.getRoom();
			} catch (BattleRoomExecuterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BattleDataRoomManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return null;
		}
		return null;
	}

	public void subjectSelect(String roomId, String subjectId, String userId) {
		
		
		BattleRoomExecuter battleRoomExecuter = battleRoomExecuterMap.get(roomId);
		
		
		try {
			battleRoomExecuter.subjectSelect(subjectId,userId);
		} catch (BattleRoomExecuterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
