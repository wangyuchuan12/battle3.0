package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleWaitRoomMemberDao;
import com.battle.domain.BattleWaitRoomMember;

@Service
public class BattleWaitRoomMemberService {

	@Autowired
	private BattleWaitRoomMemberDao battleWaitRoomMemberDao;

	public void add(BattleWaitRoomMember battleWaitRoomMember) {
		
		battleWaitRoomMember.setId(UUID.randomUUID().toString());
		battleWaitRoomMember.setCreateAt(new DateTime());
		battleWaitRoomMember.setUpdateAt(new DateTime());
		
		battleWaitRoomMemberDao.save(battleWaitRoomMember);
		
	}

	public List<BattleWaitRoomMember> findAllByRoomId(String roomId) {
		
		return battleWaitRoomMemberDao.findAllByRoomId(roomId);
		
	}

	public BattleWaitRoomMember findOneByRoomIdAndUserId(String roomId, String userId) {
		
		return battleWaitRoomMemberDao.findOneByRoomIdAndUserId(roomId,userId);
		
	}

	public void update(BattleWaitRoomMember battleWaitRoomMember) {
		
		battleWaitRoomMember.setUpdateAt(new DateTime());
		battleWaitRoomMemberDao.save(battleWaitRoomMember);
		
	}

	public List<BattleWaitRoomMember> findAllByUserIdAndStatus(String userId, Integer status) {
		
		return battleWaitRoomMemberDao.findAllByUserIdAndStatus(userId,status);
		
	}

	public BattleWaitRoomMember findOne(String id) {
		
		return battleWaitRoomMemberDao.findOne(id);
	}

	public List<BattleWaitRoomMember> findAllByIsOwner(int isOwner) {
		
		return battleWaitRoomMemberDao.findAllByIsOwner(isOwner);
	}
}
