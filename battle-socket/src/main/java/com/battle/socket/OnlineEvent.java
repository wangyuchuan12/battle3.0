package com.battle.socket;

import org.springframework.context.ApplicationEvent;

import com.battle.domain.UserStatus;

public class OnlineEvent extends ApplicationEvent{

	public OnlineEvent(UserStatus userStatus) {
		super(userStatus);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
