package com.wyc.listener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class InitListener implements ApplicationListener<ContextRefreshedEvent>{

	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
	}

	
}
