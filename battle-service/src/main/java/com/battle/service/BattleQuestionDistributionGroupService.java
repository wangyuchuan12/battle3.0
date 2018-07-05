package com.battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuestionDistributionGroupDao;
import com.battle.domain.BattleQuestionDistributionGroup;

@Service
public class BattleQuestionDistributionGroupService {

	@Autowired
	private BattleQuestionDistributionGroupDao battleQuestionDistributionGroupDao;

	public BattleQuestionDistributionGroup findOne(String groupId) {
		
		return battleQuestionDistributionGroupDao.findOne(groupId);
		
	}
}
