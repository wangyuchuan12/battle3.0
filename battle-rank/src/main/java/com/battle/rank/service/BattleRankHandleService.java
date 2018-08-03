package com.battle.rank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.wx.domain.UserInfo;

@Service
public class BattleRankHandleService {

	@Autowired
	private BattleRankMemberService battleRankMemberService;
	
	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private WxUserInfoService userInfoService;
	
	
	public BattleRankMember takepart(String rankId,String userId){
		
		
		BattleRank battleRank = battleRankService.findOne(rankId);
		BattleRankMember battleRankMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(), userId);
		
		if(battleRankMember==null){
			
			battleRankMember = new BattleRankMember();
			UserInfo userInfo = userInfoService.findOne(userId);
			
			battleRankMember.setCoverUrl(userInfo.getHeadimgurl());
			battleRankMember.setHeadImg(userInfo.getHeadimgurl());
			battleRankMember.setNickname(userInfo.getNickname());
			battleRankMember.setProcess(0);
			battleRankMember.setRankId(battleRank.getId());
			battleRankMember.setUserId(userId);
			
			battleRankMemberService.add(battleRankMember);
		}
		return battleRankMember;
		
	}
}
