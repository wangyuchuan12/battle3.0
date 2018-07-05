package com.battle.executer.imp;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleEndHandle;
import com.battle.executer.BattleRoomConnector;
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventCallback;
import com.battle.executer.EventHandle;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.vo.BattleRoomMemberVo;

public class EventHandleImp implements EventHandle{

	private BattleRoomDataManager battleRoomDataManager;
	
	private BattleRoomStageExecuter battleRoomStageExecuter;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	
	private BattleRoomExecuter battleRoomExecuter;
	
	private BattleEndHandle battleEndHandle;
	
	private ScheduledExecuter scheduledExecuter;
	
	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	public void init(ExecuterStore executerStore){
		this.battleRoomDataManager = executerStore.getBattleRoomDataManager();
		this.battleRoomStageExecuter = executerStore.getBattleRoomStageExecuter();
		this.battleRoomQuestionExecuter = executerStore.getBattleQuestionExecuter();
		this.battleRoomExecuter = executerStore.getBattleRoomExecuter();
		this.battleEndHandle = executerStore.getBattleEndHandle();
		this.scheduledExecuter = executerStore.getScheduledExecuter();
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
		
		this.startRoomEvent();
		this.restEndEvent();
		this.startQuestions();
		this.submitResult();
		this.roomEnd();
	}
	
	//等待结束事件
	@Override
	public void restEndEvent() {
		
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.REST_END_CODE, new EventCallback() {
			
			@Override
			public void callback(Map<String, Object> data) {
				battleRoomDataManager.getBattlePaper().setStageIndex(battleRoomDataManager.getBattlePaper().getStageIndex()+1);
				if(battleRoomDataManager.getBattleRoom().getStageCount()>=battleRoomDataManager.getBattlePaper().getStageIndex()){
					List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN);
					System.out.println("...............battleRoomMembers:"+battleRoomMemberVos);
					boolean flag = false;
					for(BattleRoomMemberVo battleRoomMember:battleRoomMemberVos){
						System.out.println("...............battleRoomMember:"+battleRoomMember.getRemainLove()+",nickname:"+battleRoomMember.getNickname());
						if(battleRoomMember.getRemainLove()>0){
							flag = true;
							break;
						}
					}
					if(flag){
						battleRoomStageExecuter.startStage();
					}else{
						eventManager.publishEvent(Event.ROOM_END_CODE, null);
					}
				}else{
					eventManager.publishEvent(Event.ROOM_END_CODE, null);
				}
				
			}
		});
		
	}

	@Override
	public void startRoomEvent() {
		EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.START_ROOM_CODE, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				battleRoomExecuter.members();
				battleRoomStageExecuter.startStage();
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
				battleRoomExecuter.endRoom();
				battleEndHandle.end(battleRoomDataManager);
				scheduledExecuter.shutdown();
				battleRoomConnector.removeExecuter(battleRoomDataManager.getBattleRoom().getId());
				battleRoomQuestionExecuter.roomEnd();

				battleRoomDataManager = null;
				
				battleRoomStageExecuter = null;
				
				battleRoomQuestionExecuter = null;
				
				battleRoomExecuter = null;
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

}
