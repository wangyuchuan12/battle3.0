package com.battle.executer;

import java.util.List;

import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.QuestionAnswerResultVo;

public interface BattleRoomPublish {
	
	public void publishRoomEnd();
	
	public void publishRoomStart();
	
	public void publishMemberStatus(String userId);
	
	public void publishShowSubjects(List<BattlePaperSubjectVo> battlePaperSubjectVos);
	
	public void publishShowQuestion(List<BattlePaperQuestionVo> battlePaperQuestionVos);
	
	public void publishDoAnswer(QuestionAnswerResultVo questionAnswerResultVo);
	
	public void publishDoSelectSubject(BattlePaperSubjectVo battlePaperSubjectVo);

	void init(BattleRoomDataManager battleRoomDataManager);
	
}
