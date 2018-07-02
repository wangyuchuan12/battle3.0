package com.battle.executer.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.battle.executer.BattleRoomConnector;
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.ExecuterStore;

@Service
public class BattleRoomFactoryImp extends BattleRoomFactory{

	@Autowired
    private AutowireCapableBeanFactory factory;
	
	@Autowired
	private BattleRoomConnector battleRoomConnetor;
	
	private BattleRoomExecuter battleRoomExecuter;
	
	private BattleRoomStageExecuter battleRoomStageExecuter;
	
	private BattleRoomDataManager battleRoomDataManager;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	
	private BattleRoomPublish battleRoomPublish;
	
	@Override
	public ExecuterStore init(String battleId,String periodId,List<String> userIds,Integer type,Map<String, Object> data) {
		ExecuterStore executerStore =  super.init(battleId,periodId,userIds,type,data);
		
		BattleRoomDataManager battleRoomDataManager = executerStore.getBattleRoomDataManager();
		
		BattleRoomExecuter battleRoomExecuter = executerStore.getBattleRoomExecuter();
		
		battleRoomConnetor.registerExecuter(battleRoomDataManager.getBattleRoom().getId(), battleRoomExecuter);
		
		return executerStore;
	}
	
	@Override
	protected BattleRoomExecuter createBatteRoomExecuter() {
		BattleRoomExecuter battleRoomExecuter = new BattleRoomExecuterImp();
		factory.autowireBean(battleRoomExecuter);
		this.battleRoomExecuter = battleRoomExecuter;
		return battleRoomExecuter;
	}
	
	@Override
	protected BattleRoomStageExecuter createBatteRoomStageExecuter() {
		BattleRoomStageExecuter battleRoomStageExecuter = new BatttleRoomStageExecuterImp();
		factory.autowireBean(battleRoomStageExecuter);
		this.battleRoomStageExecuter = battleRoomStageExecuter;
		return battleRoomStageExecuter;
	}

	@Override
	protected BattleRoomDataManager createBattleRoomDataManager() {
		BattleRoomDataManager battleRoomDataManager = new BattleRoomDataManagerImp();
		this.battleRoomDataManager = battleRoomDataManager;
		return battleRoomDataManager;
	}

	@Override
	protected BattleRoomPublish createBattleRoomPublish() {
		BattleRoomPublish battleRoomPublish = new BattleRoomPublishImp();
		factory.autowireBean(battleRoomPublish);
		this.battleRoomPublish = battleRoomPublish;
		return battleRoomPublish;
	}

	@Override
	protected BattleRoomQuestionExecuter createBattleRoomQuestionExecuter() {
		BattleRoomQuestionExecuter battleRoomQuestionExecuter = new BattleRoomQuestionExecuterImp();
		factory.autowireBean(battleRoomQuestionExecuter);
		this.battleRoomQuestionExecuter = battleRoomQuestionExecuter;
		return battleRoomQuestionExecuter;
	}

	
}
