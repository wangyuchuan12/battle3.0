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
	public void publishRoomEnd()throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishMemberStatus(String userId)throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishShowSubjects(BattleStageVo battleStage) throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishShowQuestion(BattlePaperQuestionVo battlePaperQuestion) throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishDoAnswer(QuestionAnswerResultVo questionAnswerResultVo) throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishShowSubjectStatus(BattlePaperSubjectVo battlePaperSubjectVo) throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishRest() throws PublishException, BattleDataManagerException, BattleQuestionManagerException, BattleDataRoomManagerException, SendMessageException;

	void init(ExecuterStore executerStore) throws PublishException;

	public void publishRoomStart() throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, BattleQuestionManagerException, SendMessageException;
	
	public void publishReward(BattleUserRewardVo battleReward) throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;

	public void publishDie(BattleRoomMemberVo battleRoomMember,Integer type) throws PublishException, SendMessageException;

	public void publishMembers() throws PublishException, BattleDataManagerException, BattleDataRoomManagerException, SendMessageException;
	
	public void publishTakepart(BattleRoomMemberVo battleRoomMember);
	
	public void publishMyInfo(BattleRoomMemberVo battleRoomMember) throws SendMessageException, PublishException;
	
	public void publishLoveCool(BattleRoomMemberVo battleRoomMember) throws SendMessageException, PublishException;
	
}
