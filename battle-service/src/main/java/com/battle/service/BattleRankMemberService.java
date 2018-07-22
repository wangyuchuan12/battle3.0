package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRankMemberDao;
import com.battle.domain.BattleRankMember;

@Service
public class BattleRankMemberService {

	@Autowired
	private BattleRankMemberDao battleRankMemberDao;

	public BattleRankMember findOneByRankIdAndUserId(String rankId, String userId) {
		
		return battleRankMemberDao.findOneByRankIdAndUserId(rankId,userId);
	}

	public void add(BattleRankMember battleRankMember) {
		
		battleRankMember.setId(UUID.randomUUID().toString());
		battleRankMember.setUpdateAt(new DateTime());
		battleRankMember.setCreateAt(new DateTime());
		
		
		battleRankMemberDao.save(battleRankMember);
		
	}

	public void update(BattleRankMember battleRankMember) {
		battleRankMember.setUpdateAt(new DateTime());
		battleRankMemberDao.save(battleRankMember);
	}

	public Page<BattleRankMember> findAllByRankId(String rankId,Pageable pageable) {
		
		return battleRankMemberDao.findAllByRankId(rankId,pageable);
	}
	
	public Long rank(String rankId,Integer process){
		return battleRankMemberDao.rank(rankId, process);
	}
}
