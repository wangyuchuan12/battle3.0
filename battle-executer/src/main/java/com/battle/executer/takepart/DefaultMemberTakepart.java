package com.battle.executer.takepart;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomMemberTakepart;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.ExecuterStore;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.wx.domain.UserInfo;

public class DefaultMemberTakepart implements BattleRoomMemberTakepart{

	private BattleDataManager battleDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	@Autowired
	private AccountService accountService;
	@Override
	public BattleRoomMemberVo takepart(UserInfo userInfo) throws BattleDataManagerException, BattleDataRoomManagerException {
		List<BattleRoomMemberVo> battleRoomMembers =  battleDataManager.getBattleMembers();
		BattleRoomVo battleRoom = battleDataManager.getBattleRoom();
		BattleRoomMemberVo battleRoomMemberVo = null;
		for(BattleRoomMemberVo thisMember:battleRoomMembers){
			if(thisMember.getUserId().equals(userInfo.getId())){
				battleRoomMemberVo = thisMember;
			}
		}
		if(battleRoomMemberVo==null){
			Account account = accountService.fineOne(userInfo.getAccountId());
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
			battleRoomMemberVo.setBeanNum(account.getWisdomCount().intValue());
			battleRoomMemberVo.setPreClear(0);
			battleRoomMemberVo.setIsOut(0);
			battleRoomMembers.add(battleRoomMemberVo);
		}else{
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
			battleRoomMemberVo.setIsOut(0);
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
