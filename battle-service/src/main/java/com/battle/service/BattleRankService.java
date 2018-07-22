package com.battle.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
}
