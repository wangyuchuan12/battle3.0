package com.battle.executer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.battle.executer.imp.EventHandleImp;

public abstract class BattleRoomFactory {
	
	@Autowired
    public AutowireCapableBeanFactory factory;
	
	
	public ExecuterStore init(String battleId,String periodId,List<String> userIds){
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
		
		battleRoomDataManager.init(battleId, periodId, userIds);
		
		
		
		ExecuterStore executerStore = new ExecuterStore() {
			
			@Override
			public BattleRoomStageExecuter getBattleRoomStageExecuter() {
				// TODO Auto-generated method stub
				return battleRoomStageExecuter;
			}
			
			@Override
			public BattleRoomPublish getBattleRoomPublish() {
				// TODO Auto-generated method stub
				return battleRoomPublish;
			}
			
			@Override
			public BattleRoomExecuter getBattleRoomExecuter() {
				// TODO Auto-generated method stub
				return battleRoomExecuter;
			}
			
			@Override
			public BattleRoomQuestionExecuter getBattleQuestionExecuter() {
				// TODO Auto-generated method stub
				return battleRoomQuestionExecuter;
			}

			@Override
			public BattleRoomDataManager getBattleRoomDataManager() {
				// TODO Auto-generated method stub
				return battleRoomDataManager;
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
}
