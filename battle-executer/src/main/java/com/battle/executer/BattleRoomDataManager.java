package com.battle.executer;

import java.util.List;

import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;

public interface BattleRoomDataManager {
	
	public List<BattlePaperQuestionVo> getPaperQuestions(Integer stageIndex);
	
	public BattleRoomVo getBattleRoom();
	
	public BattlePaperVo getBattlePaper();
	
	public List<BattleRoomMemberVo> getBattleMembers();
	
	public BattleRoomMemberVo getBattleMember(String id);
	
	public BattleRoomMemberVo getBattleMemberByUserId(String userId);
	
	public List<BattlePaperSubjectVo> getPaperSubjects();
	
	public BattlePaperQuestionVo getPaperQuestion(String id);
}
