package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleSubject;

public interface BattleSubjectDao extends CrudRepository<BattleSubject, String>{

	//@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleSubject> findAllByBattleIdAndIsDelOrderBySeqAsc(String battleId,Integer isDel);

	//@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleSubject> findAllByIdIn(String[] subjectIds);

	//@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	@Query("select id from com.battle.domain.BattleSubject bs where bs.battleId=:battleId")
	List<String> getIdsByBattleId(@Param("battleId")String battleId);

	//@Query("from com.battle.domain.BattleSubject bs where bs.battleId=:battleId and bs.isDel=:isDel order by rand()")
	List<BattleSubject> findAllByBattleIdAndIsDel(@Param("battleId")String battleId,@Param("isDel") int isDel, Pageable pageable);
	
	@Cacheable(value="userCache")
	BattleSubject findOne(String id);

	//@Query("from com.battle.domain.BattleSubject bs where bs.isDel=:isDel order by rand()")
	List<BattleSubject> findAllByIsDel(@Param("isDel") Integer isDel,Pageable pageable);

}
