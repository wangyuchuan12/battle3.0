package com.battle.dao;

import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleAccountResult;

public interface BattleAccountResultDao extends CrudRepository<BattleAccountResult, String>{

	
	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleAccountResult findOneByUserId(String userId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	Page<BattleAccountResult> findAll(Pageable pageable);

}
