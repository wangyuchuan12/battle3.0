package com.battle.executer.imp;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.vo.BattleStageVo;

public class BatttleRoomStageExecuterImp implements BattleRoomStageExecuter{

	private BattleRoomDataManager battleRoomDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	
	private EventManager eventManager;
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;
	
	@Override
	public void init(ExecuterStore executerStore){
		this.battleRoomDataManager = executerStore.getBattleRoomDataManager();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
		this.battleRoomQuestionExecuter = executerStore.getBattleQuestionExecuter();
		this.eventManager = executerStore.getBattleRoomDataManager().getEventManager();
	}
	
	@Override
	public void startStage() {
		System.out.println(".........startStage");
		Integer stageIndex = battleRoomDataManager.getBattlePaper().getStageIndex();
		List<BattleStageVo> battleStages = battleRoomDataManager.getBattlePaper().getBattleStages();
		BattleStageVo battleStageVo = null;
		for(BattleStageVo thisBattleStageVo:battleStages){
			if(thisBattleStageVo.getStageIndex().intValue()==stageIndex.intValue()){
				battleStageVo = thisBattleStageVo;
			}
		}
		
		battleStageVo.setStatus(BattleStageVo.STATUS_IN);
		battleRoomPublish.publishShowSubjects(battleStageVo);
		
		scheduledExecutorService.schedule(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(".........startQuestions");
				eventManager.publishEvent(Event.START_QUESTIONS, null);
			}
		}, battleStageVo.getTimeLong(), TimeUnit.SECONDS);
	}

}
