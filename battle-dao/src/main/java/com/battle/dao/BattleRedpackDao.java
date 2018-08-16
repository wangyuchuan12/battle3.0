package com.battle.dao;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRedpack;

public interface BattleRedpackDao extends CrudRepository<BattleRedpack, String>{

	List<BattleRedpack> findAllByStatus(Integer status);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	BattleRedpack findOne(String id);

}
