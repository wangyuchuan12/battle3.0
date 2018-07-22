package com.battle.rank.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.executer.BattleRestEvent;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;

@Service
public class BattleRestEventListener  implements ApplicationListener<BattleRestEvent>{

	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private BattleRankMemberService battleRankMemberService;
	@Override
	public void onApplicationEvent(BattleRestEvent event) {
		List<BattleRoomMemberVo> battleRoomMemberVos = event.getSource();
		
		List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
		
		BattleRank battleRank = null;
		
		if(battleRanks.size()>0){
			battleRank = battleRanks.get(0);
		}
		
		if(battleRank!=null){
			for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
				BattleRankMember battleRankMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(),battleRoomMemberVo.getUserId());
				if(battleRankMember==null){
					battleRankMember = new BattleRankMember();
					battleRankMember.setCoverUrl(battleRoomMemberVo.getImgUrl());
					battleRankMember.setHeadImg(battleRoomMemberVo.getImgUrl());
					battleRankMember.setLoveCount(battleRoomMemberVo.getRemainLove());
					battleRankMember.setLoveLimit(battleRoomMemberVo.getLimitLove());
					battleRankMember.setProcess(battleRoomMemberVo.getProcess());
					battleRankMember.setRankId(battleRank.getId());
					battleRankMember.setUserId(battleRoomMemberVo.getUserId());
					battleRankMember.setNickname(battleRoomMemberVo.getNickname());
					battleRankMemberService.add(battleRankMember);
				}else{
					battleRankMember.setCoverUrl(battleRoomMemberVo.getImgUrl());
					battleRankMember.setHeadImg(battleRoomMemberVo.getImgUrl());
					battleRankMember.setLoveCount(battleRoomMemberVo.getRemainLove());
					battleRankMember.setLoveLimit(battleRoomMemberVo.getLimitLove());
					battleRankMember.setProcess(battleRoomMemberVo.getProcess());
					battleRankMember.setNickname(battleRoomMemberVo.getNickname());
					battleRankMemberService.update(battleRankMember);
				}
			}
		}
	}

}
