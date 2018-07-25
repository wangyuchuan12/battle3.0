package com.battle.executer;

import java.util.List;
import java.util.Map;

import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleStageVo;

public interface BattleQuestionManager {

	public void init(Map<String, Object> data)throws BattleQuestionManagerException;
	
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex) throws BattleQuestionManagerException;
	
	public BattleStageVo currentStage() throws BattleQuestionManagerException;
	
	public List<BattlePaperQuestionVo> selectQuestions() throws BattleQuestionManagerException;
	
	public void nextQuestion() throws BattleQuestionManagerException;
	
	public BattlePaperQuestionVo currentQuestion() throws BattleQuestionManagerException;
	
	public void nextStage() throws BattleQuestionManagerException;
	
	public int stageCount() throws BattleQuestionManagerException;
	
	public BattlePaperVo getBattlePaper() throws BattleQuestionManagerException;
}
