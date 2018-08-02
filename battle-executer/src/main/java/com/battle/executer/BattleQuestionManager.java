package com.battle.executer;

import java.util.List;
import java.util.Map;

import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleStageVo;

public interface BattleQuestionManager {

	public void init(BattleDataManager battleDataManager);
	
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex);
	
	public BattleStageVo currentStage();
	
	public List<BattlePaperQuestionVo> selectQuestions();
	
	public void nextQuestion();
	
	public BattlePaperQuestionVo currentQuestion();
	
	public void nextStage();
	
	public int stageCount();
	
	public BattlePaperVo getBattlePaper();
}
