package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleQuestionDistributionQuestion;

public interface BattleQuestionDistributionQuestionDao extends CrudRepository<BattleQuestionDistributionQuestion, String>{

	List<BattleQuestionDistributionQuestion> findAllByDistributionIdAndIsDel(String distributionId, int isDel);

	List<BattleQuestionDistributionQuestion> findAllByDistributionIdAndDistributionStageIdAndIsDel(
			String distributionId, String distributionStageId, int isDel);

}
