package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRankSubjectDao;
import com.battle.domain.BattleRankSubject;

@Service
public class BattleRankSubjectService {

	@Autowired
	private BattleRankSubjectDao battleRankSubjectDao;

	public List<BattleRankSubject> findByRandom(Pageable pageable) {
		
		return battleRankSubjectDao.findByRandom(0,pageable);
		
	}

	public BattleRankSubject findOneByBattleSubjectId(String battleSubjectId) {
		
		return battleRankSubjectDao.findOneByBattleSubjectId(battleSubjectId);
	}

	public void add(BattleRankSubject battleRankSubject) {
		
		battleRankSubject.setId(UUID.randomUUID().toString());
		battleRankSubject.setUpdateAt(new DateTime());
		battleRankSubject.setCreateAt(new DateTime());
		
		battleRankSubjectDao.save(battleRankSubject);
	}
}
