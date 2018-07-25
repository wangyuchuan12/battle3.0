package com.battle.executer;

import java.util.List;

import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;

public interface BattleDataManager {

	public BattleRoomVo getBattleRoom() throws BattleDataManagerException, BattleDataRoomManagerException;
	
	public BattlePaperVo getBattlePaper() throws BattleDataManagerException, BattleQuestionManagerException;
	
	public List<BattleRoomMemberVo> getBattleMembers() throws BattleDataManagerException, BattleDataRoomManagerException;
	
	public List<BattleRoomMemberVo> getBattleMembers(Integer ...statuses) throws BattleDataManagerException, BattleDataRoomManagerException;
	
	public BattleRoomMemberVo getBattleMemberByUserId(String userId) throws BattleDataManagerException, BattleDataRoomManagerException;
	
	public EventManager getEventManager() throws BattleDataManagerException;
	
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex) throws BattleDataManagerException, BattleQuestionManagerException;
	
	public BattleStageVo currentStage() throws BattleDataManagerException, BattleQuestionManagerException;
	
	public List<BattlePaperQuestionVo> selectQuestions() throws BattleDataManagerException, BattleQuestionManagerException;
	
	public void nextQuestion() throws BattleDataManagerException, BattleQuestionManagerException;
	
	public BattlePaperQuestionVo currentQuestion() throws BattleDataManagerException, BattleQuestionManagerException;
	
	public int stageCount() throws BattleQuestionManagerException;
	
	public void nextStage() throws BattleQuestionManagerException;
	
	public void clearMembers() throws BattleDataRoomManagerException;
	
	public void init(BattleQuestionManager battleQuestionManager,BattleDataRoomManager battleDataRoomManager);
}
