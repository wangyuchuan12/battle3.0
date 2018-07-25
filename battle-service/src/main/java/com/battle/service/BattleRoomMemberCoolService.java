package com.battle.service;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRoomMemberCoolDao;
import com.battle.domain.BattleRoomCoolMember;

@Service
public class BattleRoomMemberCoolService {

	@Autowired
	private BattleRoomMemberCoolDao battleRoomMemberCoolDao;

	public BattleRoomCoolMember findOneByRoomIdAndUserId(String roomId, String userId) {
		
		return battleRoomMemberCoolDao.findOneByRoomIdAndUserId(roomId,userId);
	}

	public void update(BattleRoomCoolMember battleMemberLoveCool) {
		
		battleMemberLoveCool.setUpdateAt(new DateTime());
		battleRoomMemberCoolDao.save(battleMemberLoveCool);
		
	}

	public void add(BattleRoomCoolMember battleRoomCoolMember) {
		
		battleRoomCoolMember.setId(UUID.randomUUID().toString());
		battleRoomCoolMember.setUpdateAt(new DateTime());
		battleRoomCoolMember.setCreateAt(new DateTime());
		battleRoomMemberCoolDao.save(battleRoomCoolMember);
		
	}
}
