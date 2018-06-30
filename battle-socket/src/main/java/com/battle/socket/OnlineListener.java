package com.battle.socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OnlineListener {


	final static Logger logger = LoggerFactory.getLogger(OnlineListener.class);
	public synchronized void onLine(final String id){
	
	}
	
	
	public synchronized void downLine(final String id){

		
	}
}
