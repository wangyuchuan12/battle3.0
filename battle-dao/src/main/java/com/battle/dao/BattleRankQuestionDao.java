package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleRankQuestion;

public interface BattleRankQuestionDao extends CrudRepository<BattleRankQuestion, String>{

	@Query(value="from com.battle.domain.BattleRankQuestion brq where "
			+ "brq.battleSubjectId in(:subjectIds) order by rand() ")
	List<BattleRankQuestion> findAllByBattleSubjectIdIn(@Param("subjectIds")List<String> subjectIds, Pageable pageable);


}
