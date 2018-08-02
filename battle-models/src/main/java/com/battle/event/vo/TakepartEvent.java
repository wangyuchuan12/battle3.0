package com.battle.event.vo;

import org.springframework.context.ApplicationEvent;

public class TakepartEvent extends ApplicationEvent{

	public TakepartEvent(TakepartVo takepartVo) {
		super(takepartVo);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public TakepartVo getSource() {
		// TODO Auto-generated method stub
		return (TakepartVo)super.getSource();
	}

}
