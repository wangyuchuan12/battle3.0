package com.battle.executer;

import com.battle.executer.vo.BattleRoomMemberVo;
import com.wyc.common.wx.domain.UserInfo;

public interface BattleRoomMemberTakepart {

	public BattleRoomMemberVo takepart(UserInfo userInfo);
	
	public void init(ExecuterStore executerStore);
}
