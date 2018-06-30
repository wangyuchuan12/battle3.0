package com.battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuestionDistributionStageDao;
import com.battle.domain.BattleQuestionDistributionStage;

@Service
public class BattleQuestionDistributionStageService {

	@Autowired
	private BattleQuestionDistributionStageDao battleQuestionDistributionStageDao;

	public List<BattleQuestionDistributionStage> findAllByDistributionIdOrderByStageIndexAsc(String distributionId) {
		
		return battleQuestionDistributionStageDao.findAllByDistributionIdOrderByStageIndexAsc(distributionId);
	}
}
