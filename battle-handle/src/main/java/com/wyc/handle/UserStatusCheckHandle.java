package com.wyc.handle;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.UserStatus;
import com.battle.service.UserStatusService;
import com.battle.socket.WebSocketManager;
import com.wyc.common.wx.domain.UserInfo;

@Service
public class UserStatusCheckHandle {
	
	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
	private WebSocketManager webSocketManager;

	public UserStatus userStatus(UserInfo userInfo){
		UserStatus userStatus = userStatusService.findOneByUserId(userInfo.getId());
		Map<String, Object> data = new HashMap<>();
		if(webSocketManager.isOpen(userInfo.getToken())){
			int isLine = userStatus.getIsLine();
			data.put("isLine", 1);
			if(isLine==0){
				userStatus.setIsLine(1);
				userStatusService.update(userStatus);
			}
		}else{
			int isLine = userStatus.getIsLine();
			data.put("isLine", 0);
			if(isLine==1){
				userStatus.setIsLine(0);
				userStatusService.update(userStatus);
			}
		}
		
		return userStatus;
	}
}
