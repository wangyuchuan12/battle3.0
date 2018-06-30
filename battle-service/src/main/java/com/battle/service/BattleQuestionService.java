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

	public List<BattleQuestion> findAllByBattleIdAndPeriodStageIdRandom(String battleId, String periodStageId,Pageable pageable) {
		return battleQuestionDao.findAllByBattleIdAndPeriodStageIdRandom(battleId,periodStageId,pageable);
	}
	
	public List<BattleQuestion> findAllByBattleIdAndPeriodIdAndSubjectIdIsDelRandom(String battleId, String periodId,String subjectId,Pageable pageable) {
		return battleQuestionDao.findAllByBattleIdAndPeriodIdAndSubjectIdIsDelRandom(battleId,periodId,subjectId,pageable);
	}

	public List<BattleQuestion> findAllByIdIn(List<String> ids) {
		
		return battleQuestionDao.findAllByIdIn(ids);
	}

	public List<BattleQuestion> findAllByPeriodStageIdAndSubjectIdAndIsDelOrderBySeqAsc(String stageId,
			String subjectId,Integer isDel) {
		return battleQuestionDao.findAllByPeriodStageIdAndSubjectIdAndIsDelOrderBySeqAsc(stageId,subjectId,isDel);
	}

	public List<BattleQuestion> findAllByPeriodStageIdAndIsDelOrderBySeqAsc(String stageId,Integer isDel) {
		return battleQuestionDao.findAllByPeriodStageIdAndIsDelOrderBySeqAsc(stageId,isDel);
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

	public List<BattleQuestion> findAllByBattleIdAndPeriodStageIdAndSubjectIdInAndIsDel(String battleId, String stageId,
			String[] subjectIds,Integer isDel) {
		return battleQuestionDao.findAllByBattleIdAndPeriodStageIdAndSubjectIdInAndIsDel(battleId,stageId,subjectIds,isDel);
	}

	public List<BattleQuestion> findAllByBattleIdAndPeriodIdAndIsDel(String battleId, String periodId, int isDel) {
		return battleQuestionDao.findAllByBattleIdAndPeriodIdAndIsDel(battleId,periodId,isDel);
	}

	public List<Object[]> getQuestionNumByStageIdsAndSubjectIds(List<String> stageIds,
			List<String> subjectIds) {
		
		return battleQuestionDao.getQuestionNumByStageIdsAndSubjectIds(stageIds,subjectIds);
	}

	public List<BattleQuestion> findAllByBattleIdAndSubjectIdRandom(String battleId, String subjectId,
			Pageable pageable) {
		
		return battleQuestionDao.findAllByBattleIdAndSubjectIdRandom(battleId,subjectId,pageable);
	}
	
	public List<BattleQuestion> findAllByBattleIdAndPeriodIdRandom(String battleId,String periodId,
			Pageable pageable) {
		
		return battleQuestionDao.findAllByBattleIdAndPeriodIdRandom(battleId,periodId,pageable);
	}
}
