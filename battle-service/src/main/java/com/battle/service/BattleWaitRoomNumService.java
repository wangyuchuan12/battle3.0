package com.battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleWaitRoomNumDao;
import com.battle.domain.BattleWaitRoomNum;

@Service
public class BattleWaitRoomNumService {

	@Autowired
	private BattleWaitRoomNumDao battleWaitRoomNumDao;

	public List<BattleWaitRoomNum> findAllByIsDefault(int isDefault) {
		
		return battleWaitRoomNumDao.findAllByIsDefault(isDefault);
	}
}
