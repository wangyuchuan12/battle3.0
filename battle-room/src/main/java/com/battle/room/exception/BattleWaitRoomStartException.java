package com.battle.room.exception;

public class BattleWaitRoomStartException extends Exception{

	
	
	public static int MEMBER_STATUS_ERROR_TYPE = 0;
	
	public static int ROOM_STATUS_ERROR_TYPE = 1;
	
	
	
	public static int ROOM_NUM_ERROR_TYPE = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer type;
	
	public BattleWaitRoomStartException(Integer type) {
		
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

	
}
