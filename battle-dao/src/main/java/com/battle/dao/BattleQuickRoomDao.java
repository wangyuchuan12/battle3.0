package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleQuickRoom;

public interface BattleQuickRoomDao extends CrudRepository<BattleQuickRoom, String>{

	List<BattleQuickRoom> findAllByStatusAndIsDel(Integer status, Integer isDel,Pageable pageable);

}
