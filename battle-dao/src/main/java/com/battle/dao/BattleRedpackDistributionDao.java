package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRedpackDistribution;

public interface BattleRedpackDistributionDao extends CrudRepository<BattleRedpackDistribution, String>{

	List<BattleRedpackDistribution> findAllByRedpackIdAndStatusOrderByReceiveAtDesc(String redPackId, Integer status);

}
