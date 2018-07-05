package com.battle.executer;

public interface ScheduledExecuter {
	public void schedule(Runnable runnable,Integer delay);
	
	public void shutdown();
}
