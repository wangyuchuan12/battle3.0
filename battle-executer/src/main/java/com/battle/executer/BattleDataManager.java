package com.battle.executer;

import java.util.List;
import java.util.Map;

import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;

public interface BattleDataManager {

	public BattleRoomVo getBattleRoom();
	
	public BattlePaperVo getBattlePaper();
	
	public List<BattleRoomMemberVo> getBattleMembers();
	
	public List<BattleRoomMemberVo> getBattleMembers(Integer ...statuses);
	
	public BattleRoomMemberVo getBattleMemberByUserId(String userId);
	
	public EventManager getEventManager();
	
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex);
	
	public BattleStageVo currentStage();
	
	public List<BattlePaperQuestionVo> selectQuestions();
	
	public void nextQuestion();
	
	public BattlePaperQuestionVo currentQuestion();
	
	public int stageCount();
	
	public void nextStage();
	
	public void clearMembers();
	
	public void init(BattleQuestionManager battleQuestionManager,BattleDataRoomManager battleDataRoomManager);
}
