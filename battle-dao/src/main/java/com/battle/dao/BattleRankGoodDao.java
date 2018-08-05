package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRankGood;

public interface BattleRankGoodDao extends CrudRepository<BattleRankGood, String>{

	List<BattleRankGood> findAllByRankId(String rankId);

}
