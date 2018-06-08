package com.battle.service.dbExecuter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wyc.common.session.DbServiceExecuter;

@Service
public class DbServiceExecuterImp implements DbServiceExecuter{

	
	@Override
	public void update(List<Object> objs) {
		
	}

	@Override
	public <T> T findOne(Class<T> clazz, String id) {
		
		return null;
	}

}
