package com.battle.executer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.battle.executer.endHandle.DanBattleEndHandle;
import com.battle.executer.endHandle.RoomBattleEndHandle;
import com.battle.executer.imp.EventHandleImp;
import com.battle.executer.vo.BattleRoomVo;

public abstract class BattleRoomFactory {
	
	@Autowired
    public AutowireCapableBeanFactory factory;
	
	
	public ExecuterStore init(String groupId,List<String> userIds,Integer type, Map<String, Object> data){
		final BattleRoomDataManager battleRoomDataManager = createBattleRoomDataManager();
		factory.autowireBean(battleRoomDataManager);
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
		
		battleRoomDataManager.init(groupId, userIds,type,data);
		
		BattleEndHandle battleEndHandle = null;
		
		if(type.intValue()==BattleRoomVo.DAN_TYPE){
			battleEndHandle = new DanBattleEndHandle();
		}else if(type.intValue()==BattleRoomVo.ROOM_TYPE){
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
			public BattleRoomDataManager getBattleRoomDataManager() {
				return battleRoomDataManager;
			}

			@Override
			public BattleEndHandle getBattleEndHandle() {
				return outBattleEndHandle;
			}

			@Override
			public ScheduledExecuter getScheduledExecuter() {
				return scheduledExecuter;
			}
		};
		
		
		EventHandle eventHandle = new EventHandleImp();
		eventHandle.init(executerStore);
		
		factory.autowireBean(eventHandle);
		battleRoomPublish.init(executerStore);
		
		battleRoomStageExecuter.init(executerStore);
		
		battleRoomExecuter.init(battleRoomDataManager.getEventManager(),executerStore);
		
		battleRoomQuestionExecuter.init(executerStore);
		return executerStore;
		
	}

	protected  abstract BattleRoomExecuter createBatteRoomExecuter();
	
	protected  abstract BattleRoomDataManager createBattleRoomDataManager();
	
	protected  abstract BattleRoomPublish createBattleRoomPublish();
	
	protected  abstract BattleRoomQuestionExecuter createBattleRoomQuestionExecuter();
	
	protected abstract BattleRoomStageExecuter createBatteRoomStageExecuter();
	
	protected abstract ScheduledExecuter createScheduledExecuter();
}
