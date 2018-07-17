package com.battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleSearchRoomRewardDao;
import com.battle.domain.BattleSearchRoomReward;

@Service
public class BattleSearchRoomRewardService {

	@Autowired
	private BattleSearchRoomRewardDao battleSearchRoomRewardDao;

	public List<BattleSearchRoomReward> findAllBySearchKey(String searchKey) {
		
		return battleSearchRoomRewardDao.findAllBySearchKey(searchKey);
	}
}
