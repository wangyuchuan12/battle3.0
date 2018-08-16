package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleFactory;

public interface BattleFactoryDao extends CrudRepository<BattleFactory, String>{

	List<BattleFactory> findAllByUserId(String userId);

}
