package com.wyc.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.battle.domain.UserStatus;
import com.battle.executer.BattleRoomConnector;
import com.battle.socket.DownEvent;



@Service
public class DownListener implements ApplicationListener<DownEvent>{

	@Autowired
	private BattleRoomConnector battleRoomConnector;
	@Override
	public void onApplicationEvent(DownEvent event) {
		UserStatus userStatus = (UserStatus)event.getSource();
		String roomId = userStatus.getRoomId();
		battleRoomConnector.signOut(roomId, userStatus.getUserId());
	}

	

}
