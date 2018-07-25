package com.battle.dao;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRoomCoolMember;

public interface BattleRoomMemberCoolDao extends CrudRepository<BattleRoomCoolMember, String>{

	BattleRoomCoolMember findOneByRoomIdAndUserId(String roomId, String userId);

}
