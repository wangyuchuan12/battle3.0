package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuestionDistributionDao;
import com.battle.domain.BattleQuestionDistribution;

@Service
public class BattleQuestionDistributionService {

	@Autowired
	private BattleQuestionDistributionDao battleQuestionDistributionDao;

	public void add(BattleQuestionDistribution battleQuestionDistribution) {
		
		battleQuestionDistribution.setId(UUID.randomUUID().toString());
		battleQuestionDistribution.setUpdateAt(new DateTime());
		battleQuestionDistribution.setCreateAt(new DateTime());
		
		battleQuestionDistributionDao.save(battleQuestionDistribution);
		
	}

	public BattleQuestionDistribution findOne(String id) {
		
		return battleQuestionDistributionDao.findOne(id);
		
	}

	public List<BattleQuestionDistribution> findAllByBattleIdAndPeriodIdAndStatusByRandom(String battleId, String periodId,Integer status,
			Pageable pageable) {
		
		return battleQuestionDistributionDao.findAllByBattleIdAndPeriodIdAndStatusByRandom(battleId,periodId,status,pageable);
	}

	public void update(BattleQuestionDistribution battleQuestionDistribution) {
		
		battleQuestionDistribution.setUpdateAt(new DateTime());
		
		battleQuestionDistributionDao.save(battleQuestionDistribution);
		
	}

	public List<BattleQuestionDistribution> findAllByGroupId(String groupId) {
		
		return battleQuestionDistributionDao.findAllByGroupId(groupId);
		
	}
}
