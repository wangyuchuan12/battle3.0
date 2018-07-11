package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleWaitRoomGroup;

public interface BattleWaitRoomGroupDao extends CrudRepository<BattleWaitRoomGroup, String>{

	List<BattleWaitRoomGroup> findAllByIsDefault(int isDefault);

}
