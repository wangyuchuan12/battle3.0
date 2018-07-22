package com.battle.dao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleRankMember;

public interface BattleRankMemberDao extends CrudRepository<BattleRankMember, String>{

	BattleRankMember findOneByRankIdAndUserId(String rankId, String userId);

	Page<BattleRankMember> findAllByRankId(String rankId,Pageable pageable);
	
	@Query(value="select count(*)+1 from com.battle.domain.BattleRankMember brm where brm.rankId=:rankId and brm.process>:process ")
	Long rank(@Param("rankId")String rankId,@Param("process")Integer process);
}
