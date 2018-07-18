package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleWaitRoomMember;

public interface BattleWaitRoomMemberDao extends CrudRepository<BattleWaitRoomMember, String>{

	List<BattleWaitRoomMember> findAllByRoomId(String roomId);

	BattleWaitRoomMember findOneByRoomIdAndUserId(String roomId, String userId);

	List<BattleWaitRoomMember> findAllByUserIdAndStatus(String userId, Integer status);

	List<BattleWaitRoomMember> findAllByRoomIdAndIsOwner(String roomId, int isOwner);

}
