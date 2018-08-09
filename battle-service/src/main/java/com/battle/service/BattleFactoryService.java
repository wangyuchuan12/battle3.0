package com.battle.service;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleFactoryDao;
import com.battle.domain.BattleFactory;

@Service
public class BattleFactoryService {

	@Autowired
	private BattleFactoryDao battleFactoryDao;

	public BattleFactory findOneByUserId(String userId) {
		
		return battleFactoryDao.findOneByUserId(userId);
	}

	public void add(BattleFactory battleFactory) {
		battleFactory.setUpdateAt(new DateTime());
		battleFactory.setCreateAt(new DateTime());
		
		battleFactory.setId(UUID.randomUUID().toString());
		battleFactoryDao.save(battleFactory);
		
	}
}
