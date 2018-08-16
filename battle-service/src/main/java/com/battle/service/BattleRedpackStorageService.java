package com.battle.service;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRedpackStorageDao;
import com.battle.domain.BattleRedpackStorage;

@Service
public class BattleRedpackStorageService {

	@Autowired
	private BattleRedpackStorageDao battleRedpackStorageDao;
	public BattleRedpackStorage findOneByUserId(String userId) {
		
		return battleRedpackStorageDao.findOneByUserId(userId);
	}
	public void add(BattleRedpackStorage battleRedpackStorage) {
		
		battleRedpackStorage.setId(UUID.randomUUID().toString());
		battleRedpackStorage.setUpdateAt(new DateTime());
		battleRedpackStorage.setCreateAt(new DateTime());
		
		battleRedpackStorageDao.save(battleRedpackStorage);
		
	}
	public void update(BattleRedpackStorage battleRedpackStorage) {
		
		battleRedpackStorage.setUpdateAt(new DateTime());
		
		battleRedpackStorageDao.save(battleRedpackStorage);
		
	}

}
