package com.battle.socket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.WAIT_ROOM_MEMBER_STATUS_CODE);
		messageVo.setData(battleWaitRoomMember);
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		
		messageHandler.sendMessage(messageVo);
	}
	
	public void kickOutPublish(BattleWaitRoomMember battleWaitRoomMember)throws Exception{
		
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.FORCE_KICK_OUT);
		Map<String, Object> data = new HashMap<>();
		data.put("content", battleWaitRoomMember.getEndContent());
		messageVo.setData(data);
		userIds.add(battleWaitRoomMember.getUserId());
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		
		messageHandler.sendMessage(messageVo);
	}
}
