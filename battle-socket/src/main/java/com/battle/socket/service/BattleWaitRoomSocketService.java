package com.battle.socket.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleWaitRoomMember;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleWaitRoomSocketService {
	@Autowired
	private  MessageHandler messageHandler;
	
	public void waitRoomMemberPublish(BattleWaitRoomMember battleWaitRoomMember,List<String> userIds) throws IOException{
		
		
		System.out.println(".............waitRoomMemberPublish1");
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.WAIT_ROOM_MEMBER_STATUS_CODE);
		messageVo.setData(battleWaitRoomMember);
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		
		messageHandler.sendMessage(messageVo);
	}
}
