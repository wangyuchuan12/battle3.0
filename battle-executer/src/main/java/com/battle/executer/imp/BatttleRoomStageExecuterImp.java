package com.battle.executer.imp;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.vo.BattleStageVo;

public class BatttleRoomStageExecuterImp implements BattleRoomStageExecuter{

	private BattleDataManager battleRoomDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	
	private EventManager eventManager;
	
	private ScheduledExecuter scheduledExecuter;
	
	@Override
	public void init(ExecuterStore executerStore){
		this.battleRoomDataManager = executerStore.getBattleDataManager();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
		this.eventManager = executerStore.getBattleDataManager().getEventManager();
		this.scheduledExecuter = executerStore.getScheduledExecuter();
	}
	
	@Override
	public void startStage() {
		
		System.out.println("........startStage1");
		try{
			BattleStageVo battleStageVo = battleRoomDataManager.currentStage();
			battleStageVo.setStatus(BattleStageVo.STATUS_IN);
			battleRoomPublish.publishShowSubjects(battleStageVo);
			scheduledExecuter.schedule(new Runnable() {
				@Override
				public void run() {
					eventManager.publishEvent(Event.START_QUESTIONS, null);
				}
			}, battleStageVo.getTimeLong());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("........startStage2");
	}

}
