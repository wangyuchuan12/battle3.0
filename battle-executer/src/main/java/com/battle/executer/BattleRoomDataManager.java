package com.battle.executer;

import java.util.List;
import java.util.Map;

import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;

public interface BattleRoomDataManager {

	public BattleRoomVo getBattleRoom();
	
	public BattlePaperVo getBattlePaper();
	
	public List<BattleRoomMemberVo> getBattleMembers();
	
	public BattleRoomMemberVo getBattleMemberByUserId(String userId);
	
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex);
	
	public EventManager getEventManager();
	
	public void init(String battleId,String periodId,List<String> userIds,Integer type,Map<String, Object> data);
}
