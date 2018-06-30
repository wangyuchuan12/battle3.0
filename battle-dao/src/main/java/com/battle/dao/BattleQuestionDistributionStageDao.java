package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleQuestionDistributionStage;

public interface BattleQuestionDistributionStageDao extends CrudRepository<BattleQuestionDistributionStage, String>{

	List<BattleQuestionDistributionStage> findAllByDistributionIdOrderByStageIndexAsc(String distributionId);

}
