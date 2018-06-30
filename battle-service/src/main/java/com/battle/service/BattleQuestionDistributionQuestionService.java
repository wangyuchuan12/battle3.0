package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuestionDistributionQuestionDao;
import com.battle.domain.BattleQuestionDistributionQuestion;

@Service
public class BattleQuestionDistributionQuestionService {

	@Autowired
	private BattleQuestionDistributionQuestionDao battleQuestionDistributionQuestionDao;

	public List<BattleQuestionDistributionQuestion> findAllByDistributionIdAndIsDel(String distributionId, int isDel) {
		
		return battleQuestionDistributionQuestionDao.findAllByDistributionIdAndIsDel(distributionId,isDel);
	}

	public void update(BattleQuestionDistributionQuestion battleQuestionDistributionQuestion) {
		
		battleQuestionDistributionQuestion.setUpdateAt(new DateTime());
		
		battleQuestionDistributionQuestionDao.save(battleQuestionDistributionQuestion);
		
	}

	public void add(BattleQuestionDistributionQuestion battleQuestionDistributionQuestion) {
		
		battleQuestionDistributionQuestion.setId(UUID.randomUUID().toString());
		
		battleQuestionDistributionQuestionDao.save(battleQuestionDistributionQuestion);
		
	}

	public List<BattleQuestionDistributionQuestion> findAllByDistributionIdAndDistributionStageIdAndIsDel(String distributionId,
			String distributionStageId, int isDel) {
		
		return battleQuestionDistributionQuestionDao.findAllByDistributionIdAndDistributionStageIdAndIsDel(distributionId,distributionStageId,isDel);
	}
}
