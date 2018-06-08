package com.battle.executer;

public abstract class BattleRoomFactory {
	
	public class FactoryData{
		private BattleRoomDataManager battleRoomDataManager;
		private BattleRoomExecuter battleRoomExecuter;
		private BattleRoomPublish battleRoomPublish;
		private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
		public BattleRoomDataManager getBattleRoomDataManager() {
			return battleRoomDataManager;
		}
		public void setBattleRoomDataManager(BattleRoomDataManager battleRoomDataManager) {
			this.battleRoomDataManager = battleRoomDataManager;
		}
		public BattleRoomExecuter getBattleRoomExecuter() {
			return battleRoomExecuter;
		}
		public void setBattleRoomExecuter(BattleRoomExecuter battleRoomExecuter) {
			this.battleRoomExecuter = battleRoomExecuter;
		}
		public BattleRoomPublish getBattleRoomPublish() {
			return battleRoomPublish;
		}
		public void setBattleRoomPublish(BattleRoomPublish battleRoomPublish) {
			this.battleRoomPublish = battleRoomPublish;
		}
		public BattleRoomQuestionExecuter getBattleRoomQuestionExecuter() {
			return battleRoomQuestionExecuter;
		}
		public void setBattleRoomQuestionExecuter(BattleRoomQuestionExecuter battleRoomQuestionExecuter) {
			this.battleRoomQuestionExecuter = battleRoomQuestionExecuter;
		}
		
	}
	
	public FactoryData init(){
		BattleRoomDataManager battleRoomDataManager = createBattleRoomDataManager();
		
		BattleRoomExecuter battleRoomExecuter = createBatteRoomExecuter();
		
		BattleRoomPublish battleRoomPublish = createBattleRoomPublish();
		
		BattleRoomQuestionExecuter battleRoomQuestionExecuter = createBattleRoomQuestionExecuter();
		
		battleRoomExecuter.init(battleRoomDataManager, battleRoomPublish, battleRoomQuestionExecuter);
		
		battleRoomPublish.init(battleRoomDataManager);
		
		FactoryData factoryData = new FactoryData();
		factoryData.battleRoomDataManager = battleRoomDataManager;
		factoryData.battleRoomExecuter = battleRoomExecuter;
		factoryData.battleRoomPublish = battleRoomPublish;
		factoryData.battleRoomQuestionExecuter = battleRoomQuestionExecuter;
		
		return factoryData;
		
	}

	public  abstract BattleRoomExecuter createBatteRoomExecuter();
	
	public  abstract BattleRoomDataManager createBattleRoomDataManager();
	
	public  abstract BattleRoomPublish createBattleRoomPublish();
	
	public  abstract BattleRoomQuestionExecuter createBattleRoomQuestionExecuter();
}
