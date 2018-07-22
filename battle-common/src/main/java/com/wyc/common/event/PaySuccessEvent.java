package com.wyc.common.event;

import org.springframework.context.ApplicationEvent;

import com.wyc.common.domain.PaySuccess;

public class PaySuccessEvent extends ApplicationEvent{

	public PaySuccessEvent(PaySuccess paySuccess) {
		super(paySuccess);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public PaySuccess getSource() {
		return (PaySuccess)super.getSource();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
