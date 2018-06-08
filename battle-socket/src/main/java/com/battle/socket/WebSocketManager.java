package com.battle.socket;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import net.sf.ehcache.store.chm.ConcurrentHashMap;

@Service
public class WebSocketManager {
	private final Map<String,WebSocketSession> sessionMap = new ConcurrentHashMap<String, WebSocketSession>();
	
	public boolean isOpen(String token){
		WebSocketSession webSocketSession = sessionMap.get(token);
		if(webSocketSession==null){
			return false;
		}
		
		return webSocketSession.isOpen();
	}
	
	public void put(String token,WebSocketSession webSocketSession){
		
		
		System.out.println("............put socket");
		sessionMap.put(token, webSocketSession);
	}
	
	public WebSocketSession get(String token){
		
		System.out.println("............get socket");
		return sessionMap.get(token);
	}
	
	public void remove(String token){
		
		
		System.out.println("............remove socket");
		sessionMap.remove(token);
	}
	
}
