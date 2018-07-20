package com.battle.room.executer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.battle.exception.SendMessageException;
import com.battle.room.vo.BattleWaitRoomMemberVo;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

public class BattleWaitRoomPublish {

	private BattleWaitRoomDataManager battleWaitRoomDataManager;
	
	@Autowired
	private MessageHandler messageHandler;
	public BattleWaitRoomPublish(BattleWaitRoomDataManager battleWaitRoomDataManager){
		
		this.battleWaitRoomDataManager = battleWaitRoomDataManager;
		
	}
	public void waitRoomMemberPublish(String userId){
		
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
		
		
		System.out.println("...........battleWaitRoomMember.isOwner:"+battleWaitRoomMember.getIsOwner());
		
		List<BattleWaitRoomMemberVo> battleWaitRoomMemberVos = battleWaitRoomDataManager.getMembers();
		List<String> userIds = new ArrayList<>();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMemberVo:battleWaitRoomMemberVos){
			userIds.add(battleWaitRoomMemberVo.getUserId());
		}
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.WAIT_ROOM_MEMBER_STATUS_CODE);
		messageVo.setData(battleWaitRoomMember);
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		
		try {
			messageHandler.sendMessage(messageVo);
		} catch (SendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void changeOwnerPublish(String userId) {
		
		List<String> userIds = new ArrayList<>();
		userIds.add(userId);
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.CHANGE_OWNER);
		messageVo.setData(null);
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		try {
			messageHandler.sendMessage(messageVo);
		} catch (SendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void kickOutPublish(String userId) {
		
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.FORCE_KICK_OUT);
		Map<String, Object> data = new HashMap<>();
		data.put("content", battleWaitRoomMember.getEndContent());
		messageVo.setData(data);
		userIds.add(battleWaitRoomMember.getUserId());
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		
		try {
			messageHandler.sendMessage(messageVo);
		} catch (SendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
