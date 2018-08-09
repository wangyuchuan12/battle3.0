package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleRankSubject;

public interface BattleRankSubjectDao extends CrudRepository<BattleRankSubject, String>{

	@Query(value="from com.battle.domain.BattleRankSubject brs where "
			+ "brs.isDel=:isDel order by rand() ")
	List<BattleRankSubject> findByRandom(@Param("isDel")Integer isDel,Pageable pageable);

	BattleRankSubject findOneByBattleSubjectIdAndIsDel(String battleSubjectId,int isDel);

	Page<BattleRankSubject> findAllByRankIdAndIsDel(String rankId, int isDel, Pageable pageable);

}
