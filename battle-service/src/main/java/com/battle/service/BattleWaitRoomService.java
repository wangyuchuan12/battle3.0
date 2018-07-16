package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleWaitRoomDao;
import com.battle.domain.BattleWaitRoom;

@Service
public class BattleWaitRoomService {

	@Autowired
	private BattleWaitRoomDao battleWaitRoomDao;

	public void add(BattleWaitRoom battleWaitRoom) {
		
		battleWaitRoom.setId(UUID.randomUUID().toString());
		battleWaitRoom.setUpdateAt(new DateTime());
		battleWaitRoom.setCreateAt(new DateTime());
		
		battleWaitRoomDao.save(battleWaitRoom);
	}

	public BattleWaitRoom findOne(String id) {
		
		return battleWaitRoomDao.findOne(id);
	}

	public List<BattleWaitRoom> findAllByOwnerIdAndSearchKeyAndStatus(String userId, String searchKey,Integer status) {
		
		return battleWaitRoomDao.findAllByOwnerIdAndSearchKeyAndStatus(userId,searchKey,status);
	}

	public void update(BattleWaitRoom battleWaitRoom) {
	
		battleWaitRoom.setUpdateAt(new DateTime());
		battleWaitRoomDao.save(battleWaitRoom);
		
	}

	public Page<BattleWaitRoom> findAllByStatusAndSearchKeyAndIsPublicAndIsFull(Integer status, String searchKey, int isPublic,int isFull,Pageable pageable) {
		
		return battleWaitRoomDao.findAllByStatusAndSearchKeyAndIsPublicAndIsFull(status,searchKey,isPublic,isFull,pageable);
	}
}
