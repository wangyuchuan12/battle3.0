package com.battle.executer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.battle.executer.endHandle.DanBattleEndHandle;
import com.battle.executer.endHandle.RoomBattleEndHandle;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.BattleRoomExecuterException;
import com.battle.executer.exception.BattleRoomFactoryException;
import com.battle.executer.exception.BattleRoomMemberTakepartException;
import com.battle.executer.exception.BattleRoomQuestionExecuterException;
import com.battle.executer.exception.BattleRoomStageExceptionException;
import com.battle.executer.exception.EndJudgeException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.imp.EventHandleImp;
import com.battle.executer.param.RoomParam;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRoomVo;

public abstract class BattleRoomFactory {
	
	@Autowired
    public AutowireCapableBeanFactory factory;

	public ExecuterStore init(RoomParam roomParam){
		
	
		List<UserParam> userParams = roomParam.getUserParams();
		int type = roomParam.getType();
		Map<String, Object> data = roomParam.getData();
		final BattleDataManager battleDataManager = createBattleDataManager();
		factory.autowireBean(battleDataManager);
		final BattleRoomExecuter battleRoomExecuter = createBatteRoomExecuter();
		factory.autowireBean(battleRoomExecuter);
		final BattleRoomPublish battleRoomPublish = createBattleRoomPublish();
		factory.autowireBean(battleRoomPublish);
		final BattleRoomQuestionExecuter battleRoomQuestionExecuter = createBattleRoomQuestionExecuter();	
		factory.autowireBean(battleRoomQuestionExecuter);
		final BattleRoomStageExecuter battleRoomStageExecuter = createBatteRoomStageExecuter();
		factory.autowireBean(battleRoomStageExecuter);
		
		final ScheduledExecuter scheduledExecuter = createScheduledExecuter();
		factory.autowireBean(scheduledExecuter);
		
		final BattleQuestionManager battleQuestionManager = createQuestionManager();
		factory.autowireBean(battleQuestionManager);
		
		final BattleDataRoomManager battleDataRoomManager = createBattleDataRoomManager();
		factory.autowireBean(battleDataRoomManager);
		
		final EndJudge endJudge = createEndJudge();
		factory.autowireBean(endJudge);
		
		final BattleRoomMemberTakepart battleRoomMemberTakepart = createBattleRoomMemberTakepart();
		factory.autowireBean(battleRoomMemberTakepart);
		
		battleDataRoomManager.init(userParams, type, data);
		
		endJudge.init(battleDataManager);
		
		List<String> userIds = new ArrayList<>();
		
		for(UserParam userParam:userParams){
			userIds.add(userParam.getUserId());
		}
		
		BattleRoomVo battleRoomVo = battleDataRoomManager.getBattleRoom();
		
		Map<String, Object> questionData = new HashMap<>();
		questionData.put("groupId", roomParam.getGroupId());
		questionData.put("userIds", userIds);
		questionData.put("roomId", battleRoomVo.getId());
		
		battleQuestionManager.init(questionData);
	
		battleDataManager.init(battleQuestionManager,battleDataRoomManager);
		
		BattleEndHandle battleEndHandle = null;
		
		if(type==BattleRoomVo.DAN_TYPE){
			battleEndHandle = new DanBattleEndHandle();
		}else if(type==BattleRoomVo.ROOM_TYPE){
			battleEndHandle = new RoomBattleEndHandle();
		}else{
			battleEndHandle = new RoomBattleEndHandle();
		}
		
		
		final BattleEndHandle outBattleEndHandle = battleEndHandle;
		
		factory.autowireBean(outBattleEndHandle);
		
		ExecuterStore executerStore = new ExecuterStore() {
			
			@Override
			public BattleRoomStageExecuter getBattleRoomStageExecuter() {
				return battleRoomStageExecuter;
			}
			
			@Override
			public BattleRoomPublish getBattleRoomPublish() {
				return battleRoomPublish;
			}
			
			@Override
			public BattleRoomExecuter getBattleRoomExecuter() {
				return battleRoomExecuter;
			}
			
			@Override
			public BattleRoomQuestionExecuter getBattleQuestionExecuter() {
				return battleRoomQuestionExecuter;
			}

			@Override
			public BattleDataManager getBattleDataManager() {
				return battleDataManager;
			}

			@Override
			public BattleEndHandle getBattleEndHandle() {
				return outBattleEndHandle;
			}

			@Override
			public ScheduledExecuter getScheduledExecuter() {
				return scheduledExecuter;
			}

			@Override
			public BattleQuestionManager getBattleQuestionManager() {
				return battleQuestionManager;
			}

			@Override
			public BattleDataRoomManager getBattleDataRoomManager() {
				return battleDataRoomManager;
			}

			@Override
			public EndJudge getEndJudge() {
				return endJudge;
			}

			@Override
			public BattleRoomMemberTakepart getBattleRoomMemberTakepart() {
				
				return battleRoomMemberTakepart;
			}
		};
		
		battleRoomMemberTakepart.init(executerStore);
		
		
		EventHandle eventHandle = new EventHandleImp();
		eventHandle.init(executerStore);
		
		factory.autowireBean(eventHandle);
		battleRoomPublish.init(executerStore);
		
		battleRoomStageExecuter.init(executerStore);
		
		battleRoomExecuter.init(battleDataManager.getEventManager(),executerStore);
		
		battleRoomQuestionExecuter.init(executerStore);
		return executerStore;
		
	}

	protected  abstract BattleRoomExecuter createBatteRoomExecuter();
	
	protected  abstract BattleDataManager createBattleDataManager();
	
	protected  abstract BattleRoomPublish createBattleRoomPublish();
	
	protected  abstract BattleRoomQuestionExecuter createBattleRoomQuestionExecuter();
	
	protected abstract BattleRoomStageExecuter createBatteRoomStageExecuter();
	
	protected abstract ScheduledExecuter createScheduledExecuter();
	
	protected abstract BattleQuestionManager createQuestionManager();
	
	protected abstract BattleDataRoomManager createBattleDataRoomManager();
	
	public abstract BattleRoomMemberTakepart createBattleRoomMemberTakepart();
	
	protected abstract EndJudge createEndJudge();
}
