package com.battle.executer.imp;
import java.util.List;
import com.battle.executer.BattleQuestionManager;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleDataRoomManager;
import com.battle.executer.EventManager;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
public class BattleDataManagerImp implements BattleDataManager{
	
	
	private EventManager eventManager;
	
	
	private BattleQuestionManager battleQuestionManager;
	
	private BattleDataRoomManager battleDataRoomManager;
	
	

	@Override
	public BattleRoomVo getBattleRoom(){
		
		return battleDataRoomManager.getBattleRoom();
	}

	@Override
	public BattlePaperVo getBattlePaper(){
		return battleQuestionManager.getBattlePaper();
	}

	@Override
	public List<BattleRoomMemberVo> getBattleMembers(){
		return battleDataRoomManager.getBattleMembers();
	}


	@Override
	public BattleRoomMemberVo getBattleMemberByUserId(String userId){
		return battleDataRoomManager.getBattleMemberByUserId(userId);
	}

	@Override
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex){
		return battleQuestionManager.getPaperSubjects(stageIndex);
	}

	@Override
	public void init(BattleQuestionManager battleQuestionManager,BattleDataRoomManager battleDataRoomManager) {
		
		this.battleQuestionManager = battleQuestionManager;
		this.eventManager = new EventManager();
		this.battleDataRoomManager = battleDataRoomManager;
		
	}

	@Override
	public EventManager getEventManager() {
		
		return eventManager;
	}

	@Override
	public List<BattleRoomMemberVo> getBattleMembers(Integer... statuses){
		return battleDataRoomManager.getBattleMembers(statuses);
	}


	@Override
	public List<BattlePaperQuestionVo> selectQuestions(){
		
		return battleQuestionManager.selectQuestions();
	}

	@Override
	public void nextStage(){
		battleQuestionManager.nextStage();
	}

	@Override
	public BattleStageVo currentStage(){
		return battleQuestionManager.currentStage();
	}

	@Override
	public void nextQuestion(){
		battleQuestionManager.nextQuestion();
	}

	@Override
	public BattlePaperQuestionVo currentQuestion(){
		return battleQuestionManager.currentQuestion();
	}

	@Override
	public int stageCount(){
		return battleQuestionManager.stageCount();
	}

	@Override
	public void clearMembers(){
		battleDataRoomManager.clear();
		
	}
}
