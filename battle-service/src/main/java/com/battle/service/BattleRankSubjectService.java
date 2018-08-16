package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRankSubjectDao;
import com.battle.domain.BattleRankSubject;

@Service
public class BattleRankSubjectService {

	@Autowired
	private BattleRankSubjectDao battleRankSubjectDao;

	public List<BattleRankSubject> findByRankIdAndIsDelRandom(String rankId,Integer isDel,Pageable pageable) {
		
		return battleRankSubjectDao.findByRankIdAndIsDelRandom(rankId,isDel,pageable);
		
	}

	public BattleRankSubject findOneByBattleSubjectIdAndIsDel(String battleSubjectId,int isDel) {
		
		return battleRankSubjectDao.findOneByBattleSubjectIdAndIsDel(battleSubjectId,isDel);
	}

	public void add(BattleRankSubject battleRankSubject) {
		
		battleRankSubject.setId(UUID.randomUUID().toString());
		battleRankSubject.setUpdateAt(new DateTime());
		battleRankSubject.setCreateAt(new DateTime());
		
		battleRankSubjectDao.save(battleRankSubject);
	}

	public Page<BattleRankSubject> findAllByRankIdAndIsDel(String rankId, int isDel, Pageable pageable) {
		
		return battleRankSubjectDao.findAllByRankIdAndIsDel(rankId,isDel,pageable);
	}

	public void update(BattleRankSubject battleRankSubject) {
		
		battleRankSubject.setUpdateAt(new DateTime());
		
		battleRankSubjectDao.save(battleRankSubject);
		
	}
}
