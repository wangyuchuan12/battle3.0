package com.battle.executer.takepart;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomMemberTakepart;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.ExecuterStore;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.wyc.common.wx.domain.UserInfo;

public class DefaultMemberTakepart implements BattleRoomMemberTakepart{

	private BattleDataManager battleDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	@Override
	public BattleRoomMemberVo takepart(UserInfo userInfo) {
		List<BattleRoomMemberVo> battleRoomMembers =  battleDataManager.getBattleMembers();
		BattleRoomVo battleRoom = battleDataManager.getBattleRoom();
		BattleRoomMemberVo battleRoomMemberVo = null;
		for(BattleRoomMemberVo thisMember:battleRoomMembers){
			if(thisMember.getUserId().equals(userInfo.getId())){
				battleRoomMemberVo = thisMember;
			}
		}
		if(battleRoomMemberVo==null){
			battleRoomMemberVo = new BattleRoomMemberVo();
			battleRoomMemberVo.setAccountId(userInfo.getAccountId());
			battleRoomMemberVo.setBattleId(battleRoom.getBattleId());
			battleRoomMemberVo.setCnRightCount(0);
			battleRoomMemberVo.setEndCotent("");
			battleRoomMemberVo.setId(UUID.randomUUID().toString());
			battleRoomMemberVo.setImgUrl(userInfo.getHeadimgurl());
			battleRoomMemberVo.setIsEnd(0);
			battleRoomMemberVo.setIsPass(0);
			battleRoomMemberVo.setRemainLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setLimitLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setNickname(userInfo.getNickname());
			battleRoomMemberVo.setPeriodId(battleRoom.getPeriodId());
			battleRoomMemberVo.setProcess(0);
			battleRoomMemberVo.setRangeGogal(battleRoom.getRangeGogal());
			battleRoomMemberVo.setRank(100);
			battleRoomMemberVo.setRewardBean(0);
			battleRoomMemberVo.setRewardLove(0);
			battleRoomMemberVo.setRoomId(battleRoom.getId());
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
			battleRoomMemberVo.setUserId(userInfo.getId());
			battleRoomMemberVo.setToken(userInfo.getToken());
			battleRoomMembers.add(battleRoomMemberVo);
		}else{
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
		}
		
		battleRoomPublish.publishTakepart(battleRoomMemberVo);
		return battleRoomMemberVo;
	}

	@Override
	public void init(ExecuterStore executerStore) {
		
		this.battleDataManager = executerStore.getBattleDataManager();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
	}

}
