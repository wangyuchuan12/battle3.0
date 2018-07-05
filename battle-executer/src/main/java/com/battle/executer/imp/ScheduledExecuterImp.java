package com.battle.executer.imp;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.ScheduledExecuter;


public class ScheduledExecuterImp implements ScheduledExecuter{
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;
	
	@Override
	public void schedule(Runnable runnable,Integer delay) {

		scheduledExecutorService.schedule(runnable, delay,TimeUnit.SECONDS);
		
	}

	@Override
	public void shutdown() {
		//scheduledExecutorService.shutdownNow();
	}

}
