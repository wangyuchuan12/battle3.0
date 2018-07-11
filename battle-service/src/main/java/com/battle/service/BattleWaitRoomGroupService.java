package com.battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleWaitRoomGroupDao;
import com.battle.domain.BattleWaitRoomGroup;

@Service
public class BattleWaitRoomGroupService {

	@Autowired
	private BattleWaitRoomGroupDao battleWaitRoomGroupDao;

	public List<BattleWaitRoomGroup> findAllByIsDefault(int isDefault) {
		
		return battleWaitRoomGroupDao.findAllByIsDefault(isDefault);
	}
}
