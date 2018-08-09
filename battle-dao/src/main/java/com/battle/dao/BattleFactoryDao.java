package com.battle.dao;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleFactory;

public interface BattleFactoryDao extends CrudRepository<BattleFactory, String>{

	BattleFactory findOneByUserId(String userId);

}
