package com.battle.socket;

import org.springframework.context.ApplicationEvent;

import com.battle.domain.UserStatus;

public class DownEvent extends ApplicationEvent{

	public DownEvent(UserStatus userStatus) {
		super(userStatus);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
