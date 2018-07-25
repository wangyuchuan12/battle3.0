package com.battle.executer;

import java.util.List;
import java.util.Map;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;

public interface BattleDataRoomManager {
	
	public void init(List<UserParam> userParams,Integer type ,Map<String, Object> data);
	
	public List<BattleRoomMemberVo> getBattleMembers();
	
	public List<BattleRoomMemberVo> getBattleMembers(Integer ...statuses);
	
	public BattleRoomMemberVo getBattleMemberByUserId(String userId);
	
	public BattleRoomVo getBattleRoom();
	
	public void clear();
	
}
