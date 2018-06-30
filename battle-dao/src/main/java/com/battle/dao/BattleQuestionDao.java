package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleQuestion;

public interface BattleQuestionDao extends CrudRepository<BattleQuestion, String>{

	@Query("from com.battle.domain.BattleQuestion bq where bq.battleId=:battleId and bq.periodStageId=:periodStageId order by rand()")
	List<BattleQuestion> findAllByBattleIdAndPeriodStageIdRandom(@Param("battleId")String battleId,@Param("periodStageId")String periodStageId,Pageable pageable);

	@Query("from com.battle.domain.BattleQuestion bq where bq.battleId=:battleId and bq.subjectId=:subjectId order by rand()")
	List<BattleQuestion> findAllByBattleIdAndSubjectIdRandom(@Param("battleId")String battleId,@Param("subjectId") String subjectId,Pageable pageable);
	
	List<BattleQuestion> findAllByIdIn(List<String> ids);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleQuestion> findAllByPeriodStageIdAndSubjectIdAndIsDelOrderBySeqAsc(String stageId, String subjectId,Integer isDel);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleQuestion> findAllByPeriodStageIdAndIsDelOrderBySeqAsc(String stageId,Integer isDel);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleQuestion> findAllByBattleIdAndPeriodStageIdAndSubjectIdInAndIsDel(String battleId, String stageId,
			String[] subjectIds,Integer isDel);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleQuestion> findAllByBattleIdAndPeriodIdAndIsDel(String battleId, String periodId, int isDel);
	
	
	@Query("select count(bq) as num,bq.periodStageId as stageId,bq.subjectId as subjectId "
			+ "from com.battle.domain.BattleQuestion bq where  "
			+ "bq.periodStageId in(:stageIds) and bq.subjectId in(:subjectIds) group by bq.periodStageId,bq.subjectId")
	List<Object[]> getQuestionNumByStageIdsAndSubjectIds(@Param("stageIds") List<String> stageIds, @Param("subjectIds") List<String> subjectIds);

	
	@Query("from com.battle.domain.BattleQuestion bq where bq.battleId=:battleId and bq.periodId=:periodId order by rand()")
	List<BattleQuestion> findAllByBattleIdAndPeriodIdRandom(@Param("battleId")String battleId, @Param("periodId")String periodId, Pageable pageable);

	@Query("from com.battle.domain.BattleQuestion bq where bq.battleId=:battleId and bq.periodId=:periodId and bq.subjectId=:subjectId order by rand()")
	List<BattleQuestion> findAllByBattleIdAndPeriodIdAndSubjectIdIsDelRandom(@Param("battleId")String battleId,
			@Param("periodId")String periodId,@Param("subjectId")String subjectId,
			Pageable pageable);

}
