package com.battle.rank.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.executer.BattleRestEvent;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;
import com.battle.service.other.BattleRoomCoolHandle;
import com.wyc.common.util.CommonUtil;

@Service
public class BattleRestEventListener  implements ApplicationListener<BattleRestEvent>{

	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private BattleRankMemberService battleRankMemberService;
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;
	@Override
	public void onApplicationEvent(BattleRestEvent event) {
		
		BattleRoomVo battleRoomVo = event.getSource();
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomVo.getMembers();
		battleRoomMemberVos = new ArrayList<>(battleRoomMemberVos);
		BattleRank battleRank = null;
		if(CommonUtil.isNotEmpty(battleRoomVo.getRankId())){
			battleRank = battleRankService.findOne(battleRoomVo.getRankId());
		}
		if(battleRank!=null){
			for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
				
				try{
					BattleRankMember battleRankMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(),battleRoomMemberVo.getUserId());
					if(battleRankMember==null){
						battleRankMember = new BattleRankMember();
						battleRankMember.setCoverUrl(battleRoomMemberVo.getImgUrl());
						battleRankMember.setHeadImg(battleRoomMemberVo.getImgUrl());
						battleRankMember.setProcess(battleRoomMemberVo.getProcess());
						battleRankMember.setRankId(battleRank.getId());
						battleRankMember.setUserId(battleRoomMemberVo.getUserId());
						battleRankMember.setNickname(battleRoomMemberVo.getNickname());
						
						
						battleRankMemberService.add(battleRankMember);
					}else{
						battleRankMember.setCoverUrl(battleRoomMemberVo.getImgUrl());
						battleRankMember.setHeadImg(battleRoomMemberVo.getImgUrl());
						battleRankMember.setProcess(battleRoomMemberVo.getProcess());
						battleRankMember.setNickname(battleRoomMemberVo.getNickname());
						battleRankMemberService.update(battleRankMember);
					}
					
					BattleRoomCoolMemberVo battleRoomCoolMember = battleRoomMemberVo.getBattleRoomCoolMemberVo();
					battleRoomCoolMember.setLoveCount(battleRoomMemberVo.getRemainLove());
					battleRoomCoolMember = battleRoomCoolHandle.filterAndSaveCoolMember(battleRoomCoolMember);
				}catch(Exception e){
					e.printStackTrace();
				}

				
			}
		}
	}

}
