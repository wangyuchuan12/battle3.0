package com.battle.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.battle.domain.UserStatus;
import com.battle.socket.service.UserStatusManager;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.wx.domain.UserInfo;

@Service
public class WebSocketManager {
	
	@Autowired
	private UserStatusManager userStatusManager;
	
	@Autowired
	private WxUserInfoService userInfoService;
	
	final static Logger logger = LoggerFactory.getLogger(WebSocketManager.class);
	
	public boolean isOpen(String token){
		WebSocketSession webSocketSession = userStatusManager.getWebSocketSession(token);
		if(webSocketSession==null){
			logger.debug("webSocketSession is null");
			return false;
		}
		
		logger.debug("webSocketSession is not null,weSocketSession:"+webSocketSession.getId());
		boolean b = webSocketSession.isOpen();
		if(!b){
			userStatusManager.downLine(token);
		}
		return b;
	}
	
	public void onLine(String token,WebSocketSession webSocketSession){
		userStatusManager.downLine(token);
		UserInfo userInfo = userInfoService.findByToken(token);
		userStatusManager.onLine(userInfo, webSocketSession);
		
		logger.debug("onLine,webSocketSession:"+webSocketSession.getId());
		
		logger.debug("webSocketSession.isOpen:"+webSocketSession.isOpen());
	}
	
	public WebSocketSession get(String token){
		WebSocketSession webSocketSession = userStatusManager.getWebSocketSession(token);
		return webSocketSession;
	}
	
	public UserStatus getUserStatus(String token){
		return userStatusManager.getUserStatus(token);
	}
	
	public void downLine(String token){
		userStatusManager.downLine(token);
	}
	
}
