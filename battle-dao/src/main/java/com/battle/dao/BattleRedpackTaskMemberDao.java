package com.battle.dao;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRedpackTaskMember;

public interface BattleRedpackTaskMemberDao extends CrudRepository<BattleRedpackTaskMember, String>{

	BattleRedpackTaskMember findOneByTaskIdAndUserId(String taskId, String userId);

}
