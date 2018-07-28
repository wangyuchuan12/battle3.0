package com.battle.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.PersonalSpaceDao;

@Service
public class PersonalSpaceService {

	@Autowired
	private PersonalSpaceDao personalSpaceDao;
}
