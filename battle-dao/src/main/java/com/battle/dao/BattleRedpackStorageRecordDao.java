package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRedpackStorageRecord;

public interface BattleRedpackStorageRecordDao extends CrudRepository<BattleRedpackStorageRecord, String>{

	List<BattleRedpackStorageRecord> findAllByRedPackIdAndUserId(String redPackId, String userId);

}
