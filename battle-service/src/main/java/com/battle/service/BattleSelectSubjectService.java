package com.battle.service;

import java.util.List;

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
}
