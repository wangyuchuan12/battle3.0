package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleWaitRoomNum;

public interface BattleWaitRoomNumDao extends CrudRepository<BattleWaitRoomNum, String>{

	List<BattleWaitRoomNum> findAllByIsDefault(int isDefault);

	BattleWaitRoomNum findOneBySearchKey(String searchKey);

}
