package com.battle.socket.service;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.battle.domain.UserStatus;
import com.battle.service.UserStatusService;
import com.battle.socket.DownEvent;
import com.battle.socket.OnlineEvent;
import com.battle.socket.WebSocketManager;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

import net.sf.ehcache.store.chm.ConcurrentHashMap;

@Service
@Scope("singleton")
public class UserStatusManager implements ApplicationEventPublisherAware{
	
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

	private  Map<String,UserStatusData> sessionMap = new ConcurrentHashMap<String, UserStatusData>();
	
	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
	private WxUserInfoService userInfoService;
	
	private ApplicationEventPublisher applicationEventPublisher;
	
	final static Logger logger = LoggerFactory.getLogger(WebSocketManager.class);

	
	public synchronized void onLine(UserInfo userInfo,WebSocketSession webSocketSession){
		
		try{
			UserStatusData userStatusData = sessionMap.get(userInfo.getToken());
			if(CommonUtil.isEmpty(userStatusData)){
				UserStatus userStatus = null;
				if(CommonUtil.isNotEmpty(userInfo.getStatusId())){
					userStatus = userStatusService.findOne(userInfo.getStatusId());
				}
				
				if(userStatus!=null){
					userStatus.setIsLine(1);
					userStatusData = new UserStatusData();
					userStatusData.setUserInfo(userInfo);
					userStatusData.setUserStatus(userStatus);
					userStatusData.setWebSocketSession(webSocketSession);
					sessionMap.put(userInfo.getToken(), userStatusData);
					userStatusService.update(userStatus);
				}else{
					
					System.out.println("............hahahah");
					userStatus = new UserStatus();
					userStatus.setIsLine(1);
					userStatus.setDownLineCount(0);
					userStatus.setOnLineAt(new DateTime());
					userStatus.setOnLineCount(1);
					userStatus.setRoomId("");
					userStatus.setStatus(UserStatus.FREE_STATUS);
					userStatus.setToken(userInfo.getToken());
					userStatus.setUserId(userInfo.getId());
					userStatusService.add(userStatus);
					
					userStatusData = new UserStatusData();
					userStatusData.setUserInfo(userInfo);
					userStatusData.setUserStatus(userStatus);
					userStatusData.setWebSocketSession(webSocketSession);
					sessionMap.put(userInfo.getToken(), userStatusData);
					
					userInfo.setStatusId(userStatus.getId());
					userInfoService.update(userInfo);
				}
			}else{
				UserStatus userStatus = userStatusData.getUserStatus();
				
				if(userStatus!=null){
					userStatus.setIsLine(1);
					userStatusService.update(userStatus);
				}else{
					userStatus = new UserStatus();
					userStatus.setIsLine(1);
					userStatus.setDownLineCount(0);
					userStatus.setOnLineAt(new DateTime());
					userStatus.setOnLineCount(1);
					userStatus.setRoomId("");
					userStatus.setStatus(UserStatus.FREE_STATUS);
					userStatus.setToken(userInfo.getToken());
					userStatus.setUserId(userInfo.getId());
					userStatusService.add(userStatus);
					
					userStatusData = new UserStatusData();
					userStatusData.setUserInfo(userInfo);
					userStatusData.setUserStatus(userStatus);
					userStatusData.setWebSocketSession(webSocketSession);
					sessionMap.put(userInfo.getToken(), userStatusData);
					
					userInfo.setStatusId(userStatus.getId());
					userInfoService.update(userInfo);
				}
			}
			
			applicationEventPublisher.publishEvent(new OnlineEvent(userStatusData.getUserStatus()));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public synchronized UserStatus doDownLine(String token){
		UserStatusData userStatusData = sessionMap.get(token);
		logger.debug("downline");
		if(userStatusData==null){
			return null;
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
		
		return userStatus;
	}
	
	public synchronized void downLine(String token){
		
		UserStatus userStatus = this.doDownLine(token);
		if(userStatus!=null){
			applicationEventPublisher.publishEvent(new DownEvent(userStatus));
		}
		
	}
	
	public WebSocketSession getWebSocketSession(String token){
		UserStatusData userStatusData = sessionMap.get(token);
		
		System.out.println(".........token:"+token+",userStatusData:"+userStatusData+",sessionMap:"+sessionMap+",this:"+this);
		if(userStatusData==null){
			return null;
		}
		return userStatusData.getWebSocketSession();
	}
	
	public UserStatus getUserStatus(String token){
		UserStatusData userStatusData = sessionMap.get(token);
		if(userStatusData==null){
			return null;
		}
		return userStatusData.getUserStatus();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		
		this.applicationEventPublisher = applicationEventPublisher;
		
	}
	
}
