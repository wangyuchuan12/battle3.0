package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleQuestionDistributionQuestion;

public interface BattleQuestionDistributionQuestionDao extends CrudRepository<BattleQuestionDistributionQuestion, String>{

	List<BattleQuestionDistributionQuestion> findAllByDistributionIdAndIsDel(String distributionId, int isDel);

	@Query(value="from com.battle.domain.BattleQuestionDistributionQuestion bq where "
			+ "bq.distributionId=:distributionId and bq.distributionStageId=:distributionStageId and isDel=:isDel order by rand()")
	List<BattleQuestionDistributionQuestion> findAllByDistributionIdAndDistributionStageIdAndIsDel(
			@Param("distributionId")String distributionId, @Param("distributionStageId")String distributionStageId, @Param("isDel")int isDel,Pageable pageable);

	@Query(value="select count(*) from com.battle.domain.BattleQuestionDistributionQuestion bq where bq.questionId=:questionId and bq.distributionId=:distributionId")
	Integer countByQuestionIdAndDistributionId(@Param("questionId")String questionId,@Param("distributionId") String distributionId);

}
