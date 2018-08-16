package com.battle.dao;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRedpackStorage;

public interface BattleRedpackStorageDao extends CrudRepository<BattleRedpackStorage, String>{

	BattleRedpackStorage findOneByUserId(String userId);

}
