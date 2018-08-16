package com.battle.service;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleQuestionDao;
import com.battle.domain.BattleQuestion;

@Service
public class BattleQuestionService {

	@Autowired
	private BattleQuestionDao battleQuestionDao;

	
	public List<BattleQuestion> findAllByBattleIdAndPeriodIdAndSubjectIdAndIsDelRandom(String battleId, String periodId,String subjectId,int isDel,Pageable pageable) {
		return battleQuestionDao.findAllByBattleIdAndPeriodIdAndSubjectIdAndIsDelRandom(battleId,periodId,subjectId,isDel,pageable);
	}

	public List<BattleQuestion> findAllByIdIn(List<String> ids) {
		
		return battleQuestionDao.findAllByIdIn(ids);
	}

	public void add(BattleQuestion battleQuestion) {
		
		battleQuestion.setId(UUID.randomUUID().toString());
		battleQuestion.setCreateAt(new DateTime());
		battleQuestion.setUpdateAt(new DateTime());
		battleQuestionDao.save(battleQuestion);
	}

	public void update(BattleQuestion battleQuestion) {
		
		battleQuestion.setUpdateAt(new DateTime());
		battleQuestionDao.save(battleQuestion);
		
	}

	public BattleQuestion findOne(String id) {
		return battleQuestionDao.findOne(id);
	}


	public List<BattleQuestion> findAllByBattleIdAndPeriodIdAndIsDel(String battleId, String periodId, int isDel) {
		return battleQuestionDao.findAllByBattleIdAndPeriodIdAndIsDel(battleId,periodId,isDel);
	}
	
	public List<BattleQuestion> findAllByBattleIdAndSubjectIdRandom(String battleId, String subjectId,
			Pageable pageable) {
		
		return battleQuestionDao.findAllByBattleIdAndSubjectIdRandom(battleId,subjectId,pageable);
	}
	
	public List<BattleQuestion> findAllByBattleIdAndPeriodIdRandom(String battleId,String periodId,
			Pageable pageable) {
		
		return battleQuestionDao.findAllByBattleIdAndPeriodIdRandom(battleId,periodId,pageable);
	}

	public List<BattleQuestion> findAllByBattleIdAndSubjectIdInAndIsDel(String battleId, String[] subjectIds, int isDel) {
		
		return battleQuestionDao.findAllByBattleIdAndSubjectIdInAndIsDel(battleId,subjectIds,isDel);
	}

	public List<Object[]> getQuestionNumBySubjectIds(List<String> subjectIds) {
		
		return battleQuestionDao.getQuestionNumBySubjectIds(subjectIds);
		
	}
}
