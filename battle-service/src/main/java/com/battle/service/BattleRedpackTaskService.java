package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRedpackTaskDao;
import com.battle.domain.BattleRedpackTask;

@Service
public class BattleRedpackTaskService {

	@Autowired
	private BattleRedpackTaskDao battleRedpackTaskDao;
	public List<BattleRedpackTask> findAllByRedpackIdOrderByIndexAsc(String redpackId) {
		
		return battleRedpackTaskDao.findAllByRedpackIdOrderByIndexAsc(redpackId);
	}
	public void add(BattleRedpackTask battleRedpackTask) {
		
		battleRedpackTask.setUpdateAt(new DateTime());
		battleRedpackTask.setCreateAt(new DateTime());
		battleRedpackTask.setId(UUID.randomUUID().toString());
		
		battleRedpackTaskDao.save(battleRedpackTask);
		
	}
	public List<BattleRedpackTask> findAllByRankId(String rankId) {
		
		return battleRedpackTaskDao.findAllByRankId(rankId);
	}

}
