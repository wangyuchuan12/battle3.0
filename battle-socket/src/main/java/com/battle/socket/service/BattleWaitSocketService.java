package com.battle.socket.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.battle.domain.BattleWaitUser;
import com.battle.service.BattleWaitUserService;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleWaitSocketService {

	@Autowired
	private  MessageHandler messageHandler;

	@Autowired
	private BattleWaitUserService battleWaitUserService;
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;

	final static Logger logger = LoggerFactory.getLogger(BattleWaitSocketService.class);
	public void waitPublish(final BattleWaitUser user)throws Exception{
		
		List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitId(user.getWaitId());
		List<String> userIds = new ArrayList<>();
		for(BattleWaitUser battleWaitUser:battleWaitUsers){
			String userId = battleWaitUser.getUserId();
			userIds.add(userId);
		}
		
		System.out.println("............userIds:"+userIds);
		final MessageVo messageVo = new MessageVo();
		messageVo.setData(user);
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setUserIds(userIds);
		messageVo.setCode(MessageVo.WAIT_STATUS_CODE);
		try {
			messageHandler.sendMessage(messageVo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
