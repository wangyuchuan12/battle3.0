package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRedpackTask;

public interface BattleRedpackTaskDao extends CrudRepository<BattleRedpackTask, String>{

	List<BattleRedpackTask> findAllByRedpackIdOrderByIndexAsc(String redpackId);

	List<BattleRedpackTask> findAllByRankId(String rankId);

}
