package com.battle.executer;

import org.springframework.context.ApplicationEvent;
import com.battle.executer.vo.BattleRoomVo;

public class BattleRestEvent extends ApplicationEvent{
	
	public BattleRestEvent(BattleRoomVo battleRoomVo) {
		super(battleRoomVo);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public BattleRoomVo getSource() {
		return (BattleRoomVo)super.getSource();
	}



	
	
}
