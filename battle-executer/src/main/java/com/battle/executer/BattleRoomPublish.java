package com.battle.executer;

import com.battle.exception.SendMessageException;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattleUserRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.QuestionAnswerResultVo;

public interface BattleRoomPublish {

	public static final Integer BEAN_DIE_TYPE = 0;
	public static final Integer LOVE_DIE_TYPE = 1;
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

	public void publishDie(BattleRoomMemberVo battleRoomMember,Integer type);

	public void publishMembers();
	
	public void publishTakepart(BattleRoomMemberVo battleRoomMember);
	
	public void publishMyInfo(BattleRoomMemberVo battleRoomMember);
	
	public void publishLoveCool(BattleRoomMemberVo battleRoomMember);
	
	public void publishGoods();
	
}
