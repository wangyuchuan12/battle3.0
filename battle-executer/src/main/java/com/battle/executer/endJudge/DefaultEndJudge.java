package com.battle.executer.endJudge;

import java.util.List;

import com.battle.executer.BattleDataManager;
import com.battle.executer.EndJudge;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.EndJudgeException;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleStageVo;

public class DefaultEndJudge implements EndJudge{

	private BattleDataManager battleDataManager;
	@Override
	public boolean isEnd() throws BattleDataManagerException, EndJudgeException {
		
		try{
			BattleStageVo battleStageVo = battleDataManager.currentStage();
			if(battleStageVo!=null){
				List<BattleRoomMemberVo> battleRoomMemberVos = battleDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN);
				int liveNum = 0;
				for(BattleRoomMemberVo battleRoomMember:battleRoomMemberVos){
					if(battleRoomMember.getRemainLove()>0){
						liveNum++;
					}
				}
				if(liveNum>1){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		}catch(BattleDataManagerException e){
			throw e;
		}catch(Exception e){
			throw new EndJudgeException();
		}
	}

	@Override
	public void init(BattleDataManager battleDataManager) {
		
		this.battleDataManager = battleDataManager;
		
	}

}
