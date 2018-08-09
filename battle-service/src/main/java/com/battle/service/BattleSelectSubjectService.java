package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleSelectSubjectDao;
import com.battle.domain.BattleSelectSubject;

@Service
public class BattleSelectSubjectService {

	@Autowired
	private BattleSelectSubjectDao battleSelectSubjectDao;

	public List<BattleSelectSubject> findAllByIsDel(int isDel, Pageable pageable) {
		
		return battleSelectSubjectDao.findAllByIsDel(isDel,pageable);
	}

	public BattleSelectSubject findOne(String id) {
		
		return battleSelectSubjectDao.findOne(id);
	}

	public void add(BattleSelectSubject battleSelectSubject) {
		
		battleSelectSubject.setId(UUID.randomUUID().toString());
		battleSelectSubject.setUpdateAt(new DateTime());
		battleSelectSubject.setCreateAt(new DateTime());
		battleSelectSubjectDao.save(battleSelectSubject);
		
	}

	public BattleSelectSubject findOneBySubjectId(String subjectId) {
		// TODO Auto-generated method stub
		return battleSelectSubjectDao.findOneBySubjectId(subjectId);
	}

	public void update(BattleSelectSubject battleSelectSubject) {
		
		battleSelectSubject.setUpdateAt(new DateTime());
		
		battleSelectSubjectDao.save(battleSelectSubject);
		
	}

	public List<BattleSelectSubject> findAllByFactoryIdAndIsDel(String factoryId, int isDel, Pageable pageable) {
		
		return battleSelectSubjectDao.findAllByFactoryIdAndIsDel(factoryId,isDel,pageable);
	}
}
