package com.battle.executer;

import java.util.List;
import java.util.Map;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;

public interface BattleDataRoomManager {
	
	public void init(List<UserParam> userParams,Integer type ,Map<String, Object> data) throws BattleDataRoomManagerException;
	
	public List<BattleRoomMemberVo> getBattleMembers() throws BattleDataRoomManagerException;
	
	public List<BattleRoomMemberVo> getBattleMembers(Integer ...statuses) throws BattleDataRoomManagerException;
	
	public BattleRoomMemberVo getBattleMemberByUserId(String userId) throws BattleDataRoomManagerException;
	
	public BattleRoomVo getBattleRoom() throws BattleDataRoomManagerException;
	
	public void clear() throws BattleDataRoomManagerException;
	
}
