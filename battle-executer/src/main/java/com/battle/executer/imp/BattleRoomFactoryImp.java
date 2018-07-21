package com.battle.executer.imp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.battle.executer.BattleQuestionManager;
import com.battle.executer.BattleRoomConnector;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleDataRoomManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.questionManager.DistributionQuestionManager;
import com.battle.executer.roomManager.DefaultRoomDataManager;

@Service
public class BattleRoomFactoryImp extends BattleRoomFactory{

	@Autowired
    private AutowireCapableBeanFactory factory;
	
	@Autowired
	private BattleRoomConnector battleRoomConnetor;
	
	@Override
	public ExecuterStore init(String groupId,List<String> userIds,Integer type,Map<String, Object> data) {

		try{
			ExecuterStore executerStore =  super.init(groupId,userIds,type,data);
			
			BattleDataManager battleRoomDataManager = executerStore.getBattleDataManager();
			
			BattleRoomExecuter battleRoomExecuter = executerStore.getBattleRoomExecuter();
			
			battleRoomConnetor.registerExecuter(battleRoomDataManager.getBattleRoom().getId(), battleRoomExecuter);
			return executerStore;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected BattleRoomExecuter createBatteRoomExecuter() {
		BattleRoomExecuter battleRoomExecuter = new BattleRoomExecuterImp();
		factory.autowireBean(battleRoomExecuter);
		return battleRoomExecuter;
	}
	
	@Override
	protected BattleRoomStageExecuter createBatteRoomStageExecuter() {
		BattleRoomStageExecuter battleRoomStageExecuter = new BatttleRoomStageExecuterImp();
		factory.autowireBean(battleRoomStageExecuter);
		return battleRoomStageExecuter;
	}

	@Override
	protected BattleDataManager createBattleDataManager() {
		BattleDataManager battleDataManager = new BattleDataManagerImp();
		return battleDataManager;
	}

	@Override
	protected BattleRoomPublish createBattleRoomPublish() {
		BattleRoomPublish battleRoomPublish = new BattleRoomPublishImp();
		factory.autowireBean(battleRoomPublish);
		return battleRoomPublish;
	}

	@Override
	protected BattleRoomQuestionExecuter createBattleRoomQuestionExecuter() {
		BattleRoomQuestionExecuter battleRoomQuestionExecuter = new BattleRoomQuestionExecuterImp();
		factory.autowireBean(battleRoomQuestionExecuter);
		return battleRoomQuestionExecuter;
	}

	@Override
	protected ScheduledExecuter createScheduledExecuter() {
		ScheduledExecuter scheduledExecuter = new ScheduledExecuterImp();
		return scheduledExecuter;
	}

	@Override
	protected BattleQuestionManager createQuestionManager() {
		BattleQuestionManager battleQuestionManager = new DistributionQuestionManager();
		return battleQuestionManager;
	}

	@Override
	protected BattleDataRoomManager createBattleDataRoomManager() {
		BattleDataRoomManager battleDataRoomManager = new DefaultRoomDataManager();
		return battleDataRoomManager;
	}

	
}
