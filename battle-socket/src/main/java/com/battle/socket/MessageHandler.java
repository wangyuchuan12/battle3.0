package com.battle.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.battle.exception.SendMessageException;

@Service
public class MessageHandler {

	@Autowired
	private AutowireCapableBeanFactory factory;
	public  void sendMessage(MessageVo messageVo) throws SendMessageException{
		
		MessageSender messageSender = new MessageSender();
		
		factory.autowireBean(messageSender);
		
		messageSender.sendMessage(messageVo);
		
		
	}
}