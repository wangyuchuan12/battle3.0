package com.wyc.handle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.service.BattleWaitRoomMemberService;
import com.battle.service.BattleWaitRoomService;
import com.battle.socket.service.BattleWaitRoomSocketService;

@Service
public class BattleWaitRoomHandle {

	@Autowired
	private BattleWaitRoomSocketService battleWaitRoomSocketService;
	
	@Autowired
	private BattleWaitRoomMemberService battleWaitRoomMemberService;
	
	@Autowired
	private BattleWaitRoomService battleWaitRoomService;
	
	public BattleWaitRoomMember switchOwner(BattleWaitRoomMember battleWaitRoomMember,BattleWaitRoomMember battleWaitRoomMember2,List<BattleWaitRoomMember> battleWaitRoomMembers)throws Exception{
		battleWaitRoomMember.setIsOwner(0);
		battleWaitRoomMember2.setIsOwner(1);
		battleWaitRoomMember2.setStatus(BattleWaitRoomMember.READY_STATUS);
		
		BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(battleWaitRoomMember.getRoomId());
		battleWaitRoom.setOwnerId(battleWaitRoomMember2.getId());
		
		battleWaitRoomService.update(battleWaitRoom);
		
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember member:battleWaitRoomMembers){
			userIds.add(member.getUserId());
		}
		battleWaitRoomMemberService.update(battleWaitRoomMember2);
		battleWaitRoomMemberService.update(battleWaitRoomMember);
		
		System.out.print("..................77");
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember2, userIds);
		
		return battleWaitRoomMember2;
	}
}
