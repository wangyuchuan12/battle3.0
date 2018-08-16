package com.battle.service;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRedpackTaskMemberDao;
import com.battle.domain.BattleRedpackTaskMember;

@Service
public class BattleRedpackTaskMemberService {

	@Autowired
	private BattleRedpackTaskMemberDao battleRedpackTaskMemberDao;
	
	public BattleRedpackTaskMember findOneByTaskIdAndUserId(String taskId, String userId) {
		
		return battleRedpackTaskMemberDao.findOneByTaskIdAndUserId(taskId,userId);
	}

	public void add(BattleRedpackTaskMember battleRedpackTaskMember) {
		
		battleRedpackTaskMember.setId(UUID.randomUUID().toString());
		battleRedpackTaskMember.setUpdateAt(new DateTime());
		battleRedpackTaskMember.setCreateAt(new DateTime());
		
		battleRedpackTaskMemberDao.save(battleRedpackTaskMember);
		
	}

	public void update(BattleRedpackTaskMember battleRedpackTaskMember) {
		
		battleRedpackTaskMember.setUpdateAt(new DateTime());
		
		battleRedpackTaskMemberDao.save(battleRedpackTaskMember);
		
	}

}
