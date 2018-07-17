package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleSearchRoomReward;

public interface BattleSearchRoomRewardDao extends CrudRepository<BattleSearchRoomReward, String>{

	List<BattleSearchRoomReward> findAllBySearchKey(String searchKey);

}
