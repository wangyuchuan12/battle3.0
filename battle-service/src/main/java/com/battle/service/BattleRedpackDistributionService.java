package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRedpackDistributionDao;
import com.battle.domain.BattleRedpackDistribution;

@Service
public class BattleRedpackDistributionService {
	
	@Autowired
	private BattleRedpackDistributionDao battleRedpackDistributionDao;

	public void add(BattleRedpackDistribution battleRedpackDistribution) {
		
		battleRedpackDistribution.setId(UUID.randomUUID().toString());
		battleRedpackDistribution.setUpdateAt(new DateTime());
		battleRedpackDistribution.setCreateAt(new DateTime());
		
		battleRedpackDistributionDao.save(battleRedpackDistribution);
		
	}

	public List<BattleRedpackDistribution> findAllByRedpackIdAndStatusOrderByReceiveAtDesc(String redPackId,
			Integer status) {
		
		return battleRedpackDistributionDao.findAllByRedpackIdAndStatusOrderByReceiveAtDesc(redPackId,status);
	}

	public void update(BattleRedpackDistribution battleRedpackDistribution) {
		
		battleRedpackDistribution.setUpdateAt(new DateTime());
		battleRedpackDistributionDao.save(battleRedpackDistribution);
		
	}

}
