package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRedpackStorageRecordDao;
import com.battle.domain.BattleRedpackStorageRecord;

@Service
public class BattleRedpackStorageRecordService {

	@Autowired
	private BattleRedpackStorageRecordDao battleRedpackStorageRecordDao;
	public List<BattleRedpackStorageRecord> findAllByRedPackAndUserId(String redPackId, String userId) {
		
		return battleRedpackStorageRecordDao.findAllByRedPackIdAndUserId(redPackId,userId);
	}
	public void add(BattleRedpackStorageRecord battleRedpackStorageRecord) {
		
		battleRedpackStorageRecord.setId(UUID.randomUUID().toString());
		battleRedpackStorageRecord.setUpdateAt(new DateTime());
		battleRedpackStorageRecord.setCreateAt(new DateTime());
		
		battleRedpackStorageRecordDao.save(battleRedpackStorageRecord);
		
	}

}
