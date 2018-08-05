package com.battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRankGoodDao;
import com.battle.domain.BattleRankGood;

@Service
public class BattleRankGoodService {

	@Autowired
	private BattleRankGoodDao battleRankGoodDao;

	public List<BattleRankGood> findAllByRankId(String rankId) {
		
		return battleRankGoodDao.findAllByRankId(rankId);
	}
}
