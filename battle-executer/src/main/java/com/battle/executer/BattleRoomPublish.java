package com.battle.executer;

import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattleUserRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.QuestionAnswerResultVo;

public interface BattleRoomPublish {
	
	public void publishRoomEnd();
	
	public void publishMemberStatus(String userId);
	
	public void publishShowSubjects(BattleStageVo battleStage);
	
	public void publishShowQuestion(BattlePaperQuestionVo battlePaperQuestion);
	
	public void publishDoAnswer(QuestionAnswerResultVo questionAnswerResultVo);
	
	public void publishShowSubjectStatus(BattlePaperSubjectVo battlePaperSubjectVo);
	
	public void publishRest();

	void init(ExecuterStore executerStore);

	public void publishRoomStart();
	
	public void publishReward(BattleUserRewardVo battleReward);

	public void publishDie(BattleRoomMemberVo battleRoomMember);

	public void publishMembers();
	
}
