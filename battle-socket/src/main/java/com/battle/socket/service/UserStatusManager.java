package com.battle.socket.service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.battle.domain.UserStatus;
import com.battle.service.UserStatusService;
import com.battle.socket.WebSocketManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

import net.sf.ehcache.store.chm.ConcurrentHashMap;

@Service
public class UserStatusManager {
	
	class UserStatusData{
		private UserInfo userInfo;
		private UserStatus userStatus;
		private WebSocketSession webSocketSession;
		public UserInfo getUserInfo() {
			return userInfo;
		}
		public void setUserInfo(UserInfo userInfo) {
			this.userInfo = userInfo;
		}
		public UserStatus getUserStatus() {
			return userStatus;
		}
		public void setUserStatus(UserStatus userStatus) {
			this.userStatus = userStatus;
		}
		public WebSocketSession getWebSocketSession() {
			return webSocketSession;
		}
		public void setWebSocketSession(WebSocketSession webSocketSession) {
			this.webSocketSession = webSocketSession;
		}
	}

	private final Map<String,UserStatusData> sessionMap = new ConcurrentHashMap<String, UserStatusData>();
	
	@Autowired
	private UserStatusService userStatusService;
	
	final static Logger logger = LoggerFactory.getLogger(WebSocketManager.class);

	
	public synchronized void onLine(UserInfo userInfo,WebSocketSession webSocketSession){
		
		System.out.println("........................onLine,token:"+userInfo.getToken());
		UserStatusData userStatusData = sessionMap.get(userInfo.getToken());
		if(CommonUtil.isEmpty(userStatusData)){
			UserStatus userStatus = userStatusService.findOne(userInfo.getStatusId());
			userStatus.setIsLine(1);
			userStatusData = new UserStatusData();
			userStatusData.setUserInfo(userInfo);
			userStatusData.setUserStatus(userStatus);
			userStatusData.setWebSocketSession(webSocketSession);
			sessionMap.put(userInfo.getToken(), userStatusData);
			userStatusService.update(userStatus);
		}else{
			UserStatus userStatus = userStatusData.getUserStatus();
			userStatus.setIsLine(1);
			userStatusService.update(userStatus);
		}
	}
	
	public synchronized void downLine(String token){
		UserStatusData userStatusData = sessionMap.get(token);
		logger.debug("downline");
		if(userStatusData==null){
			return;
		}
		UserStatus userStatus = userStatusData.getUserStatus();
		WebSocketSession webSocketSession = userStatusData.getWebSocketSession();
		
		if(webSocketSession.isOpen()){
			try {
				logger.debug("close websocketSession");
				webSocketSession.close();
			} catch (Exception e) {
				logger.error("close websocketSession fail");
			}
		}
		userStatus.setIsLine(0);
		userStatusService.update(userStatus);
		sessionMap.remove(token);
	}
	
	public WebSocketSession getWebSocketSession(String token){
		UserStatusData userStatusData = sessionMap.get(token);
		if(userStatusData==null){
			return null;
		}
		return userStatusData.getWebSocketSession();
	}
	
}
