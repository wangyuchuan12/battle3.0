package com.battle.socket.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.battle.domain.BattleWaitUser;
import com.battle.exception.SendMessageException;
import com.battle.service.BattleWaitUserService;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleWaitSocketService {

	@Autowired
	private  MessageHandler messageHandler;

	@Autowired
	private BattleWaitUserService battleWaitUserService;

	final static Logger logger = LoggerFactory.getLogger(BattleWaitSocketService.class);
	public void waitPublish(final BattleWaitUser user)throws Exception{
		
		List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitId(user.getWaitId());
		List<String> userIds = new ArrayList<>();
		for(BattleWaitUser battleWaitUser:battleWaitUsers){
			String userId = battleWaitUser.getUserId();
			if(!userId.equals(user.getUserId())){
				userIds.add(userId);
			}
		}
		
		final MessageVo messageVo = new MessageVo();
		messageVo.setData(user);
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setUserIds(userIds);
		messageVo.setCode(MessageVo.WAIT_STATUS_CODE);
		try {
			messageHandler.sendMessage(messageVo);
		} catch (SendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
