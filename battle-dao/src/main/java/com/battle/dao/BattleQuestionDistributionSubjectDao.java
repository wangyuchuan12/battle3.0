package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleQuestionDistributionSubject;

public interface BattleQuestionDistributionSubjectDao extends CrudRepository<BattleQuestionDistributionSubject, String>{

	List<BattleQuestionDistributionSubject> findAllByDistributionIdAndIsDel(String distributionId,Integer isDel);

	List<BattleQuestionDistributionSubject> findAllByDistributionIdAndDistributionStageIdAndIsDel(String distributionId,
			String distributionStageId, int isDel);

}
