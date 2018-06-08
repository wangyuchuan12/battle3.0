package com.battle.socket;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageHandler {

	@Autowired
	private AutowireCapableBeanFactory factory;
	public  void sendMessage(MessageVo messageVo) throws IOException{
		
		MessageSender messageSender = new MessageSender();
		
		factory.autowireBean(messageSender);
		
		messageSender.sendMessage(messageVo);
		
		
	}
}