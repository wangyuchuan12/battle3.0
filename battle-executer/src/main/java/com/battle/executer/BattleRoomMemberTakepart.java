package com.battle.executer;

import com.battle.exception.SendMessageException;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.BattleRoomExecuterException;
import com.battle.executer.exception.BattleRoomMemberTakepartException;
import com.battle.executer.exception.BattleRoomQuestionExecuterException;
import com.battle.executer.exception.BattleRoomStageExceptionException;
import com.battle.executer.exception.EndJudgeException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.wyc.common.wx.domain.UserInfo;

public interface BattleRoomMemberTakepart {

	public BattleRoomMemberVo takepart(UserInfo userInfo);
	
	public void init(ExecuterStore executerStore);
}
