package com.battle.executer;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.battle.executer.vo.BattleRoomMemberVo;

public class BattleRestEvent extends ApplicationEvent{

	public BattleRestEvent(List<BattleRoomMemberVo> battleRoomMemberVos) {
		super(battleRoomMemberVos);
	}
	
	@Override
	public List<BattleRoomMemberVo> getSource() {
		// TODO Auto-generated method stub
		return (List<BattleRoomMemberVo>)super.getSource();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
