package com.battle.socket;
import com.alibaba.druid.support.json.JSONUtils;
import com.battle.domain.UserStatus;
import com.battle.exception.SendMessageException;
import com.battle.socket.service.UserStatusManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author xiaojf 2017/3/2 9:55.
 */
@Service
public class SocketHandler extends TextWebSocketHandler implements ApplicationEventPublisherAware {
    @Autowired
    private AutowireCapableBeanFactory factory;
    
    @Autowired
    private ScheduledExecutorService executorService;
    
    @Autowired
    private WebSocketManager webSocketManager;
    
    @Autowired
    private UserStatusManager userStatusManager;
    
    private ApplicationEventPublisher applicationEventPublisher;
    
    
    final static Logger logger = LoggerFactory.getLogger(SocketHandler.class);
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        session.sendMessage(new TextMessage(session.getPrincipal().getName()+",你是第" + (sessionMap.size()) + "位访客")); //p2p

        Object parse = JSONUtils.parse(message.getPayload());

        System.out.println(".......message:"+parse);
        
        
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
    	
    	Map<String, Object> attributes = session.getAttributes();
    	final Object token = attributes.get("token");
    	
    	webSocketManager.onLine(token.toString(), session);
		super.afterConnectionEstablished(session);
    	
    }

    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    	Map<String, Object> attributes = session.getAttributes();
    	Object token = attributes.get("token");
		/*
    	onlineListener.downLine(userId.toString());
		*/
		webSocketManager.downLine(token.toString());
        super.afterConnectionClosed(session, status);
    }

    /**
     * 发送消息
     * @author xiaojf 2017/3/2 11:43
     */
    public void sendMessage(String token,String message) throws SendMessageException {
        sendMessage(Arrays.asList(token),Arrays.asList(message));
    }

    /**
     * 发送消息
     * @author xiaojf 2017/3/2 11:43
     */
    public void sendMessage(Collection<String> acceptorList,String message) throws SendMessageException{
        sendMessage(acceptorList,Arrays.asList(message));
       
    }
    

    /**
     * 发送消息，p2p 群发都支持
     * @author xiaojf 2017/3/2 11:43
     */
    public void sendMessage(Collection<String> acceptorList, Collection<String> msgList) throws SendMessageException {
        if (acceptorList != null && msgList != null) {
            for (String acceptor : acceptorList) {
            	boolean isOpen = webSocketManager.isOpen(acceptor);
            	if(isOpen){
	                WebSocketSession session = webSocketManager.get(acceptor);
	             
	                if (session != null) {
	                    for (String msg : msgList) {
	                    	try{
	                    		 System.out.println("..........sendMessage:"+msg);
	                    		session.sendMessage(new TextMessage(msg.getBytes()));
	                    	}catch(Exception e){
	                    	
	                    		logger.error("信息发生错误 {}",e);
	                    		
	                    		
	                    		UserStatus userStatus = userStatusManager.getUserStatus(acceptor);
	                    		
	                    		applicationEventPublisher.publishEvent(new DownEvent(userStatus));
	                    		throw new SendMessageException();
	              
	                    	}
	                    }
	                }
            	}else{
            		UserStatus userStatus = userStatusManager.getUserStatus(acceptor);
            		if(userStatus!=null){
            			userStatusManager.downLine(userStatus.getToken());
            		}
            		logger.error("连接已经关闭");
            		throw new SendMessageException();
            	}
            }
        }
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		
		this.applicationEventPublisher = applicationEventPublisher;
		
	}
}