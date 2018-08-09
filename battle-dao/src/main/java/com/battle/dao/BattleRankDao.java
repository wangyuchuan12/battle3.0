package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRank;

public interface BattleRankDao extends CrudRepository<BattleRank, String>{

	List<BattleRank> findAllByIsDefault(int isDefault);

	List<BattleRank> findAllByOwnerUserId(String ownerUserId);

	List<BattleRank> findAllByFactoryId(String factoryId);

}
