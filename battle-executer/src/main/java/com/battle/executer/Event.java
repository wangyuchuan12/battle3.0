package com.battle.executer;

import java.util.ArrayList;
import java.util.List;

public  class Event {
	
	public static final String REST_END_CODE = "rest_end_code";
	
	public static final String START_ROOM_CODE = "start_room_code";
	
	public static final String ROOM_END_CODE = "room_end";
	
	public static final String START_QUESTIONS = "start_questions";
	
	public static final String SUBMIT_RESULTS = "submit_results";
	
	public static final String SUBMIT_RESULT = "submit_result";
	
	public static final String PUBLISH_DIE = "publish_die";
	
	public static final String PUBLISH_GOODS = "publish_goods";
	
	public static final String MY_INFO = "my_info";

	private String code;
	
	private Object data;
	
	private Integer timeLong;
	
	private List<EventCallback> eventCallbacks = new ArrayList<>();
	
	

	public List<EventCallback> getEventCallbacks() {
		return eventCallbacks;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(Integer timeLong) {
		this.timeLong = timeLong;
	}
}
