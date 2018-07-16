package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleQuickRoomMember;

public interface BattleQuickRoomMemberDao extends CrudRepository<BattleQuickRoomMember, String>{

	BattleQuickRoomMember findOneByQuickRoomIdAndUserId(String quickRoomId, String userId);

	 List<BattleQuickRoomMember> findAllByQuickRoomId(String roomId,Pageable pageable);

}
