package com.battle.executer.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.battle.executer.BattleRoomConnector;
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;

@Service
public class BattleRoomFactoryImp extends BattleRoomFactory{

	@Autowired
    private AutowireCapableBeanFactory factory;
	
	@Autowired
	private BattleRoomConnector battleRoomConnetor;
	
	@Override
	public FactoryData init() {
		FactoryData factoryData =  super.init();
		
		BattleRoomDataManager battleRoomDataManager = factoryData.getBattleRoomDataManager();
		
		BattleRoomExecuter battleRoomExecuter = factoryData.getBattleRoomExecuter();
		
		battleRoomConnetor.registerExecuter(battleRoomDataManager.getBattleRoom().getId(), battleRoomExecuter);
		
		return factoryData;
	}
	
	@Override
	public BattleRoomExecuter createBatteRoomExecuter() {
		BattleRoomExecuter battleRoomExecuter = new BattleRoomExecuterImp();
		factory.autowireBean(battleRoomExecuter);
		return battleRoomExecuter;
	}

	@Override
	public BattleRoomDataManager createBattleRoomDataManager() {
		BattleRoomDataManager battleRoomDataManager = new BattleRoomDataManagerImp();
		return battleRoomDataManager;
	}

	@Override
	public BattleRoomPublish createBattleRoomPublish() {
		BattleRoomPublish battleRoomPublish = new BattleRoomPublishImp();
		factory.autowireBean(battleRoomPublish);
		return battleRoomPublish;
	}

	@Override
	public BattleRoomQuestionExecuter createBattleRoomQuestionExecuter() {
		BattleRoomQuestionExecuter battleRoomQuestionExecuter = new BattleRoomQuestionExecuterImp();
		factory.autowireBean(battleRoomQuestionExecuter);
		return battleRoomQuestionExecuter;
	}

	
}
