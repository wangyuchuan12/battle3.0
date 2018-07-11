package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleWaitRoom;

public interface BattleWaitRoomDao extends CrudRepository<BattleWaitRoom, String>{

	List<BattleWaitRoom> findAllByOwnerIdAndStatus(String userId, Integer status);

}
