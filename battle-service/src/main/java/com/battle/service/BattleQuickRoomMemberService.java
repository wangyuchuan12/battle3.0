package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuickRoomMemberDao;
import com.battle.domain.BattleQuickRoomMember;

@Service
public class BattleQuickRoomMemberService {

	@Autowired
	private BattleQuickRoomMemberDao battleQuickRoomMemberDao;

	public BattleQuickRoomMember findOneByQuickRoomIdAndUserId(String quickRoomId, String userId) {
		
		return battleQuickRoomMemberDao.findOneByQuickRoomIdAndUserId(quickRoomId,userId);
	}

	public void update(BattleQuickRoomMember battleQuickRoomMember) {
		battleQuickRoomMember.setUpdateAt(new DateTime());
		battleQuickRoomMemberDao.save(battleQuickRoomMember);
	}

	public List<BattleQuickRoomMember> findAllByQuickRoomId(String roomId,Pageable pageable) {
		
		return battleQuickRoomMemberDao.findAllByQuickRoomId(roomId,pageable);
		
	}

	public void add(BattleQuickRoomMember battleQuickRoomMember) {
		
		battleQuickRoomMember.setId(UUID.randomUUID().toString());
		battleQuickRoomMember.setUpdateAt(new DateTime());
		battleQuickRoomMember.setCreateAt(new DateTime());
		
		battleQuickRoomMemberDao.save(battleQuickRoomMember);
		
	}
}
