package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRedpackDao;
import com.battle.domain.BattleRedpack;

@Service
public class BattleRedpackService {

	@Autowired
	private BattleRedpackDao battleRedpackDao;
	public void add(BattleRedpack battleRedpack) {
		
		battleRedpack.setId(UUID.randomUUID().toString());
		battleRedpack.setUpdateAt(new DateTime());
		battleRedpack.setCreateAt(new DateTime());
		
		battleRedpackDao.save(battleRedpack);
	}
	public List<BattleRedpack> findAllByStatus(Integer status) {
		
		return battleRedpackDao.findAllByStatus(status);
	}
	public BattleRedpack findOne(String id) {
		
		return battleRedpackDao.findOne(id);
	}
	public void update(BattleRedpack battleRedpack) {
		
		battleRedpack.setUpdateAt(new DateTime());
		
		battleRedpackDao.save(battleRedpack);
		
	}

}
