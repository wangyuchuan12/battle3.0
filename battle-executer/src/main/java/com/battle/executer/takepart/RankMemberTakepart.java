package com.battle.executer.takepart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.domain.BattleRoomCoolMember;
import com.battle.exception.SendMessageException;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomMemberTakepart;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;
import com.battle.service.other.BattleRoomCoolHandle;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.wx.domain.UserInfo;

public class RankMemberTakepart implements BattleRoomMemberTakepart{

	private BattleDataManager battleDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private BattleRankMemberService battleRankMemberService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;
	
	private ScheduledExecuter scheduledExecuter;
	@Override
	public BattleRoomMemberVo takepart(UserInfo userInfo){
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
			battleRoomMemberVo.setPreClear(0);
			battleRoomMemberVo.setShareNum(0);
			battleRoomMemberVo.setIsOut(0);
			battleRoomMemberVo.setBeanNum(account.getWisdomCount().intValue());
			
			
			BattleRank battleRank = battleRankService.findOne(battleDataManager.getRankId());
			if(battleRank!=null){
				BattleRankMember battleRankMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(), userInfo.getId());
				if(battleRankMember!=null){
					battleRoomMemberVo.setProcess(battleRankMember.getProcess());
					battleRoomMemberVo.setShareNum(0);
					battleRoomMemberVo.setIsOut(0);
				}
			}
			
				
			battleRoomMembers.add(battleRoomMemberVo);
		}else{
			Account account = accountService.fineOne(userInfo.getAccountId());
			battleRoomMemberVo.setBeanNum(account.getWisdomCount().intValue());
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
			battleRoomMemberVo.setShareNum(0);
			battleRoomMemberVo.setIsOut(0);
		}
		
		BattleRoomCoolMemberVo battleRoomCoolMemberVo = battleRoomCoolHandle.getCoolMember(battleRoom.getId(), userInfo.getId());
		
		if(battleRoomCoolMemberVo==null){
			battleRoomCoolMemberVo = battleRoomCoolHandle.createBattleRoomCoolMember(battleRoom.getId(), 
					userInfo.getId(), 
					battleRoomMemberVo.getRemainLove(),
					battleRoomMemberVo.getLimitLove());
		}else{
			battleRoomCoolMemberVo = battleRoomCoolHandle.filterAndSaveCoolMember(battleRoomCoolMemberVo);
			battleRoomMemberVo.setRemainLove(battleRoomCoolMemberVo.getLoveCount());
		}
		
		if(battleRoomCoolMemberVo!=null){
			battleRoomMemberVo.setRemainLove(battleRoomCoolMemberVo.getLoveCount());
			battleRoomMemberVo.setLimitLove(battleRoomCoolMemberVo.getLoveLimit());
		}
		
		battleRoomMemberVo.setBattleRoomCoolMemberVo(battleRoomCoolMemberVo);
		
		
		final BattleRoomMemberVo thisMember = battleRoomMemberVo;
		scheduledExecuter.schedule(new Runnable() {
			@Override
			public void run() {
				try{
					if(thisMember.getRemainLove().intValue()<=0){
						
						EventManager eventManager = battleDataManager.getEventManager();
						Map<String, Object> data = new HashMap<>();
						data.put("type", BattleRoomPublish.LOVE_DIE_TYPE);
						data.put("member", thisMember);
						eventManager.publishEvent(Event.PUBLISH_DIE, data);
					}
					battleRoomPublish.publishTakepart(thisMember);
					battleRoomPublish.publishLoveCool(thisMember);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 2000);
		
		return battleRoomMemberVo;
	}

	@Override
	public void init(ExecuterStore executerStore) {
		
		this.battleDataManager = executerStore.getBattleDataManager();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
		this.scheduledExecuter = executerStore.getScheduledExecuter();
		
	}

}
