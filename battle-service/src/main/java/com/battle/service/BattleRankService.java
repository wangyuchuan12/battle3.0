package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRankDao;
import com.battle.domain.BattleRank;

@Service
public class BattleRankService {

	@Autowired
	private BattleRankDao battleRankDao;

	public List<BattleRank> findAllByIsDefault(int isDefault) {
		
		return battleRankDao.findAllByIsDefault(isDefault);
		
	}

	public BattleRank findOne(String rankId) {
		
		return battleRankDao.findOne(rankId);
	}

	public void update(BattleRank battleRank) {
		
		battleRank.setUpdateAt(new DateTime());
		
		battleRankDao.save(battleRank);
		
	}

	public void add(BattleRank battleRank) {
		
		battleRank.setId(UUID.randomUUID().toString());
		battleRank.setCreateAt(new DateTime());
		battleRank.setUpdateAt(new DateTime());
		
		battleRankDao.save(battleRank);
		
	}

	public List<BattleRank> findAllByOwnerUserId(String ownerUserId) {
		
		return battleRankDao.findAllByOwnerUserId(ownerUserId);
	}

	public List<BattleRank> findAllByFactoryId(String factoryId) {
		
		return battleRankDao.findAllByFactoryId(factoryId);
	}

	public List<BattleRank> findAllByStatusAndIsPublic(Integer status, int isPublic, Pageable pageable) {
		
		return battleRankDao.findAllByStatusAndIsPublic(status,isPublic,pageable);
	}

	public List<BattleRank> findAllByFactoryIdIn(List<String> factoryIds) {
		
		return battleRankDao.findAllByFactoryIdIn(factoryIds);
	}
}
