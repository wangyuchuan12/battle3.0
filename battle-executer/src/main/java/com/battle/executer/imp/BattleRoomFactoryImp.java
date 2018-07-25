package com.battle.executer.imp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.battle.executer.BattleQuestionManager;
import com.battle.executer.BattleRoomConnector;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleDataRoomManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.BattleRoomMemberTakepart;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.BattleRoomStageExecuter;
import com.battle.executer.EndJudge;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.endJudge.DefaultEndJudge;
import com.battle.executer.endJudge.EndlessModeEndJudge;
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
import com.battle.executer.param.RoomParam;
import com.battle.executer.questionManager.DistributionQuestionManager;
import com.battle.executer.questionManager.RankQuestionManager;
import com.battle.executer.roomManager.DefaultRoomDataManager;
import com.battle.executer.takepart.DefaultMemberTakepart;
import com.battle.executer.takepart.RankMemberTakepart;
import com.battle.executer.vo.BattleRoomVo;

@Service
public class BattleRoomFactoryImp extends BattleRoomFactory{

	@Autowired
    private AutowireCapableBeanFactory factory;
	
	@Autowired
	private BattleRoomConnector battleRoomConnetor;
	
	private RoomParam roomParam;
	@Override
	public ExecuterStore init(RoomParam roomParam) throws BattleRoomFactoryException, BattleDataRoomManagerException, BattleQuestionManagerException, BattleRoomExecuterException, BattleDataManagerException, EndJudgeException, BattleRoomMemberTakepartException, PublishException, BattleRoomStageExceptionException, BattleRoomQuestionExecuterException {

		try{
			this.roomParam = roomParam;
			ExecuterStore executerStore =  super.init(roomParam);
			
			BattleDataManager battleRoomDataManager = executerStore.getBattleDataManager();
			
			BattleRoomExecuter battleRoomExecuter = executerStore.getBattleRoomExecuter();
			
			battleRoomConnetor.registerExecuter(battleRoomDataManager.getBattleRoom().getId(), battleRoomExecuter);
			return executerStore;
		}catch(BattleRoomFactoryException e){
			throw e;
		}catch(BattleDataRoomManagerException e){
			throw e;
		}catch(BattleQuestionManagerException e){
			throw e;
		}catch(BattleRoomExecuterException e){
			throw e;
		}catch(BattleDataManagerException e){
			throw e;
		}catch(EndJudgeException e){
			throw e;
		}catch(BattleRoomMemberTakepartException e){
			throw e;
		}catch(PublishException e){
			throw e;
		}catch(BattleRoomStageExceptionException e){
			throw e;
		}catch(BattleRoomQuestionExecuterException e){
			throw e;
		}catch(Exception e){
			throw new BattleRoomFactoryException();
		}
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
		int type = roomParam.getType();
		if(type==BattleRoomVo.RANK_TYPE){
			BattleQuestionManager battleQuestionManager = new RankQuestionManager();
			return battleQuestionManager;
		}else{
			BattleQuestionManager battleQuestionManager = new DistributionQuestionManager();
			return battleQuestionManager;
		}
	}

	@Override
	protected BattleDataRoomManager createBattleDataRoomManager() {
		BattleDataRoomManager battleDataRoomManager = new DefaultRoomDataManager();
		return battleDataRoomManager;
	}

	@Override
	protected EndJudge createEndJudge() {
		int type = roomParam.getType();
		if(type==BattleRoomVo.RANK_TYPE){
			EndJudge endJudge = new EndlessModeEndJudge();
			return endJudge;
		}else{
			EndJudge endJudge = new DefaultEndJudge();
			return endJudge;
		}
		
	}

	@Override
	public BattleRoomMemberTakepart createBattleRoomMemberTakepart() {
		int type = roomParam.getType();
		if(type==BattleRoomVo.RANK_TYPE){
			BattleRoomMemberTakepart battleRoomMemberTakepart = new RankMemberTakepart();
			return battleRoomMemberTakepart;
		}else{
			BattleRoomMemberTakepart battleRoomMemberTakepart = new DefaultMemberTakepart();
			return battleRoomMemberTakepart;
		}
	}

	
}
