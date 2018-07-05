package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleQuestionDistribution;

public interface BattleQuestionDistributionDao extends CrudRepository<BattleQuestionDistribution, String>{

	
	@Query("from com.battle.domain.BattleQuestionDistribution bqd where bqd.battleId=:battleId and bqd.periodId=:periodId and bqd.status=:status order by rand()")
	List<BattleQuestionDistribution> findAllByBattleIdAndPeriodIdAndStatusByRandom(@Param("battleId")String battleId,
			@Param("periodId") String periodId,
			@Param("status") Integer status,
			Pageable pageable);

	List<BattleQuestionDistribution> findAllByGroupId(String groupId);

}
