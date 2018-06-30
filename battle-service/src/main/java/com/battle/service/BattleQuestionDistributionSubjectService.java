package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuestionDistributionSubjectDao;
import com.battle.domain.BattleQuestionDistributionSubject;

@Service
public class BattleQuestionDistributionSubjectService {

	@Autowired
	private BattleQuestionDistributionSubjectDao battleQuestionDistributionSubjectDao;

	public List<BattleQuestionDistributionSubject> findAllByDistributionIdAndIsDel(String distributionId,Integer isDel) {
		
		return battleQuestionDistributionSubjectDao.findAllByDistributionIdAndIsDel(distributionId,isDel);
	}

	public void update(BattleQuestionDistributionSubject battleQuestionDistributionSubject) {
		
		battleQuestionDistributionSubject.setUpdateAt(new DateTime());
		
		battleQuestionDistributionSubjectDao.save(battleQuestionDistributionSubject);
		
	}

	public void add(BattleQuestionDistributionSubject battleQuestionDistributionSubject) {
		
		battleQuestionDistributionSubject.setId(UUID.randomUUID().toString());
		
		battleQuestionDistributionSubject.setCreateAt(new DateTime());
		battleQuestionDistributionSubject.setUpdateAt(new DateTime());
		
		battleQuestionDistributionSubjectDao.save(battleQuestionDistributionSubject);
		
	}

	public List<BattleQuestionDistributionSubject> findAllByDistributionIdAndDistributionStageIdAndIsDel(String distributionId,
			String distributionStageId, int isDel) {
		
		return battleQuestionDistributionSubjectDao.findAllByDistributionIdAndDistributionStageIdAndIsDel(distributionId,distributionStageId,isDel);
	}
}
