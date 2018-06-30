package com.battle.executer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventManager {
	private List<Event> events = new ArrayList<>();
	public void addEvent(Event eventL){
		for(Event event:events){
			if(event.getCode().equals(eventL.getCode())){
				return;
			}
		}
		events.add(eventL);
	}
	
	public void addCallback(String code,EventCallback callback){
		Event eventL = null;
		for(Event event:events){
			if(event.getCode().equals(code)){
				eventL = event;
				break;
			}
		}
		
		if(eventL==null){
			throw new RuntimeException("找不到code 为"+code+"的事件");
		}
		
		List<EventCallback> eventCallbacks = eventL.getEventCallbacks();
		eventCallbacks.add(callback);
	}
	
	public void publishEvent(String code,Map<String, Object> data){
		Event eventL = null;
		for(Event event:events){
			if(event.getCode().equals(code)){
				eventL = event;
				break;
			}
		}
		
		if(eventL==null){
			throw new RuntimeException("找不到code 为"+code+"的事件");
		}
		
		List<EventCallback> eventCallbacks = eventL.getEventCallbacks();
		
		for(EventCallback eventCallback:eventCallbacks){
			eventCallback.callback(data);
		}
	}
}
