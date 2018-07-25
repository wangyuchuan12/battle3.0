package com.battle.executer.imp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.battle.executer.BattleEndHandle;
import com.battle.executer.BattleRestEvent;
import com.battle.executer.BattleRoomConnector;
import com.battle.exception.SendMessageException;
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
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.BattleRoomExecuterException;
import com.battle.executer.exception.BattleRoomQuestionExecuterException;
import com.battle.executer.exception.BattleRoomStageExceptionException;
import com.battle.executer.exception.EndJudgeException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.service.other.BattleRoomCoolHandle;
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
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;

	
	public void init(ExecuterStore executerStore) throws BattleDataManagerException{
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
		
		Event eventSubmitResults = new Event();
		eventSubmitResults.setCode(Event.SUBMIT_RESULTS);
		eventManager.addEvent(eventSubmitResults);
		
		Event eventSubmitResult = new Event();
		eventSubmitResult.setCode(Event.SUBMIT_RESULT);
		eventManager.addEvent(eventSubmitResult);
		
		Event roomEnd = new Event();
		roomEnd.setCode(Event.ROOM_END_CODE);
		eventManager.addEvent(roomEnd);
		
		Event publishDie = new Event();
		publishDie.setCode(Event.PUBLISH_DIE);
		eventManager.addEvent(publishDie);
		
		Event myInfo = new Event();
		myInfo.setCode(Event.MY_INFO);
		eventManager.addEvent(myInfo);
		
		this.startRoomEvent();
		this.restEndEvent();
		this.startQuestions();
		this.submitResults();
		this.submitResult();
		this.roomEnd();
		this.publishDie();
		this.myInfo();
	}
	
	//等待结束事件
	@Override
	public void restEndEvent() throws BattleDataManagerException {
		
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.REST_END_CODE, new EventCallback() {
			
			@Override
			public void callback(Map<String, Object> data) throws BattleQuestionManagerException, EndJudgeException, BattleDataManagerException, BattleRoomStageExceptionException, BattleRoomExecuterException, BattleDataRoomManagerException, BattleRoomQuestionExecuterException, PublishException, SendMessageException {
				
				System.out.println("restEndEvent");
				battleRoomDataManager.nextStage();
				
				boolean isEnd = endJudge.isEnd();
				if(isEnd){
					eventManager.publishEvent(Event.ROOM_END_CODE, null);
				}else{
					battleRoomStageExecuter.startStage();
				}

			}
		});
		
	}

	@Override
	public void startRoomEvent() throws BattleDataManagerException {
		EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.START_ROOM_CODE, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				try{
					System.out.println(".............................*******************startRoomEvent");
					battleRoomExecuter.members();
					battleRoomStageExecuter.startStage();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		
	}

	@Override
	public void startQuestions() throws BattleDataManagerException {
		EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.START_QUESTIONS, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) throws BattleRoomQuestionExecuterException, BattleDataManagerException, BattleQuestionManagerException, EndJudgeException, PublishException, BattleDataRoomManagerException, SendMessageException, BattleRoomStageExceptionException {
				battleRoomQuestionExecuter.startQuestions();
			}
		});
		
	}

	@Override
	public void roomEnd() throws BattleDataManagerException {
		
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
	public void submitResults() throws BattleDataManagerException {
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.SUBMIT_RESULTS, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) throws BattleRoomExecuterException {
				battleRoomExecuter.submitResults();
				scheduledExecuter.schedule(new Runnable() {
					
					@Override
					public void run() {
						try {
							eventManager.publishEvent(Event.REST_END_CODE, null);
						} catch (BattleQuestionManagerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (EndJudgeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BattleDataManagerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BattleRoomStageExceptionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BattleRoomExecuterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BattleDataRoomManagerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BattleRoomQuestionExecuterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (PublishException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SendMessageException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 5);
			}
		});
	}
	
	
	@Override
	public void submitResult() {
		EventManager eventManager;
		try {
			eventManager = battleRoomDataManager.getEventManager();
			eventManager.addCallback(Event.SUBMIT_RESULT, new EventCallback() {
				@Override
				public void callback(Map<String, Object> data) throws BattleRoomExecuterException, BattleDataManagerException, BattleQuestionManagerException, BattleDataRoomManagerException, BattleRoomQuestionExecuterException, EndJudgeException, PublishException, SendMessageException, BattleRoomStageExceptionException {
					
					battleRoomExecuter.submitResult();
					battleRoomDataManager.nextQuestion();
					battleRoomQuestionExecuter.startQuestion();
					
				}
			});
		} catch (BattleDataManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void publishDie() throws BattleDataManagerException {
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.PUBLISH_DIE, new EventCallback() {
			@Override
			public void callback(Map<String, Object> data) {
				try{
					Integer type = (Integer)data.get("type");
					Boolean beanCheck = (Boolean)data.get("beanCheck");
					if(beanCheck==null){
						beanCheck = false;
					}
					if(data!=null&&data.get("member")!=null){
						BattleRoomMemberVo battleRoomMember = (BattleRoomMemberVo)data.get("member");
						boolean flag = false;
						if(type==BattleRoomPublish.LOVE_DIE_TYPE&&battleRoomMember.getRemainLove()<=0){
							battleRoomPublish.publishDie(battleRoomMember,BattleRoomPublish.LOVE_DIE_TYPE);
							flag = true;
						}
						
						if(beanCheck&&type==BattleRoomPublish.BEAN_DIE_TYPE&&battleRoomMember.getBeanNum()!=null&&battleRoomMember.getBeanNum()<=0){
							battleRoomPublish.publishDie(battleRoomMember,BattleRoomPublish.BEAN_DIE_TYPE);
							flag = true;
						}
						
						if(flag){
							battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_DIE);
						}else{
							battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_IN);
						}
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public void myInfo() throws BattleDataManagerException {
		final EventManager eventManager = battleRoomDataManager.getEventManager();
		eventManager.addCallback(Event.MY_INFO, new EventCallback() {
			@Override
			public void callback(final Map<String, Object> data) {
				try{
					BattleRoomMemberVo battleRoomMember = (BattleRoomMemberVo)data.get("myInfo");
					
					BattleRoomCoolMemberVo battleRoomCoolMember = battleRoomMember.getBattleRoomCoolMemberVo();
					if(battleRoomCoolMember!=null){
						battleRoomCoolMember.setLoveCount(battleRoomMember.getRemainLove());
						battleRoomCoolMember = battleRoomCoolHandle.filterAndSaveCoolMember(battleRoomCoolMember);
						battleRoomMember.setRemainLove(battleRoomCoolMember.getLoveCount());
					}
					battleRoomPublish.publishMyInfo(battleRoomMember);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}

}
