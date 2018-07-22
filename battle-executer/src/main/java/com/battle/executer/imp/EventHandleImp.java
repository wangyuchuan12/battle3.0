package com.battle.executer.imp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.battle.executer.BattleEndHandle;
import com.battle.executer.BattleRestEvent;
import com.battle.executer.BattleRoomConnector;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.EndJudge;
import com.battle.executer.Event;
import com.battle.executer.EventCallback;
import com.battle.executer.EventHandle;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.wyc.ApplicationContextProvider;

public class EventHandleImp implements EventHandle{

	private BattleDataManager battleRoomDataManager;
	
	private BattleRoomStageExecuter battleRoomStageExecuter;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	
	private BattleRoomExecuter battleRoomExecuter;
	
	private BattleEndHandle battleEndHandle;
	
	private ScheduledExecuter scheduledExecuter;
	
	private BattleRoomPublish battleRoomPublish;
	
	private EndJudge endJudge;
	
	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	public void init(ExecuterStore executerStore){
		this.battleRoomDataManager = executerStore.getBattleDataManager();
		this.battleRoomStageExecuter = executerStore.getBattleRoomStageExecuter();
		this.battleRoomQuestionExecuter = executerStore.getBattleQuestionExecuter();
		this.battleRoomExecuter = executerStore.getBattleRoomExecuter();
		this.battleEndHandle = executerStore.getBattleEndHandle();
		this.scheduledExecuter = executerStore.getScheduledExecuter();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
		this.endJudge = executerStore.getEndJudge();
		EventManager eventManager = battleRoomDataManager.getEventManager();
		Event eventRest = new Event();
		eventRest.setCode(Event.REST_END_CODE);
		eventManager.addEvent(eventRest);
		
		Event eventStartRoom = new Event();
		eventStartRoom.setCode(Event.START_ROOM_CODE);
		eventManager.addEvent(eventStartRoom);
		
		Event eventStartQuestions = new Event();
		eventStartQuestions.setCode(Event.START_QUESTIONS);
		eventManager.addEvent(eventStartQuestions);
		
		Event eventSubmitResult = new Event();
		eventSubmitResult.setCode(Event.SUBMIT_RESULT);
		eventManager.addEvent(eventSubmitResult);
		
		Event roomEnd = new Event();
		roomEnd.setCode(Event.ROOM_END_CODE);
		eventManager.addEvent(roomEnd);
		
		Event publishDie = new Event();
		publishDie.setCode(Event.PUBLISH_DIE);
		eventManager.addEvent(publishDie);
		
		this.startRoomEvent();
		this.restEndEvent();
		this.startQuestions();
		this.submitResult();
		this.roomEnd();
		this.publishDie();
	}
	
	//等待结束事件
	@Override
	public void restEndEvent() {
		
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.REST_END_CODE, new EventCallback() {
			
			@Override
			public void callback(Map<String, Object> data) {
				battleRoomDataManager.nextStage();
				
				boolean isEnd = endJudge.isEnd();
				if(isEnd){
					eventManager.publishEvent(Event.ROOM_END_CODE, null);
				}else{
					battleRoomStageExecuter.startStage();
				}
				
				battleRoomDataManager.clearMembers();
				
				ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
				
				applicationContext.publishEvent(new BattleRestEvent(battleRoomDataManager.getBattleMembers()));
				
				eventManager.publishEvent(Event.PUBLISH_DIE, null);
			}
		});
		
	}

	@Override
	public void startRoomEvent() {
		EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.START_ROOM_CODE, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				try{
					battleRoomExecuter.members();
					battleRoomStageExecuter.startStage();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		
	}

	@Override
	public void startQuestions() {
		EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.START_QUESTIONS, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				battleRoomQuestionExecuter.startQuestions();
			}
		});
		
	}

	@Override
	public void roomEnd() {
		
		EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.ROOM_END_CODE, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				
				try{
					battleRoomExecuter.endRoom();
					battleEndHandle.end(battleRoomDataManager);
					scheduledExecuter.shutdown();
					battleRoomConnector.removeExecuter(battleRoomDataManager.getBattleRoom().getId());
					battleRoomQuestionExecuter.roomEnd();
	
					battleRoomDataManager = null;
					
					battleRoomStageExecuter = null;
					
					battleRoomQuestionExecuter = null;
					
					battleRoomExecuter = null;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void submitResult() {
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.SUBMIT_RESULT, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				battleRoomExecuter.submitResult();
				
				scheduledExecuter.schedule(new Runnable() {
					
					@Override
					public void run() {
						eventManager.publishEvent(Event.REST_END_CODE, null);
					}
				}, 5);
			}
		});
	}

	@Override
	public void publishDie() {
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.PUBLISH_DIE, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				try{
					
					if(data!=null&&data.get("member")!=null){
						BattleRoomMemberVo battleRoomMember = (BattleRoomMemberVo)data.get("member");
						if(battleRoomMember.getRemainLove()<=0){
							battleRoomPublish.publishDie(battleRoomMember,BattleRoomPublish.LOVE_DIE_TYPE);
						}else if(battleRoomMember.getBeanNum()!=null&&battleRoomMember.getBeanNum()<=0){
							battleRoomPublish.publishDie(battleRoomMember,BattleRoomPublish.BEAN_DIE_TYPE);
						}
						
					}else{
						List<BattleRoomMemberVo> battleRoomMembers = battleRoomDataManager.getBattleMembers();
						for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
							if(battleRoomMember.getRemainLove()<=0){
								battleRoomPublish.publishDie(battleRoomMember,BattleRoomPublish.LOVE_DIE_TYPE);
							}else if(battleRoomMember.getBeanNum()!=null&&battleRoomMember.getBeanNum()<=0){
								battleRoomPublish.publishDie(battleRoomMember,BattleRoomPublish.BEAN_DIE_TYPE);
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}

}
