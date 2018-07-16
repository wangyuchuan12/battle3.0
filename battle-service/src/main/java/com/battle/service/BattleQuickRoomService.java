package com.battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuickRoomDao;
import com.battle.domain.BattleQuickRoom;

@Service
public class BattleQuickRoomService {

	@Autowired
	private BattleQuickRoomDao battleQuickRoomDao;

	public List<BattleQuickRoom> findAllByStatusAndIsDel(Integer status,Integer isDel,Pageable pageable) {
		
		return battleQuickRoomDao.findAllByStatusAndIsDel(status,isDel,pageable);
	}

	public BattleQuickRoom findOne(String id) {
		
		return battleQuickRoomDao.findOne(id);
	}
}
