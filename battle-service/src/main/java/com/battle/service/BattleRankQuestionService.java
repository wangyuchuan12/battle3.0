package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleRankQuestionDao;
import com.battle.domain.BattleRankQuestion;

@Service
public class BattleRankQuestionService {

	@Autowired
	private BattleRankQuestionDao battleRankQuestionDao;

	public List<BattleRankQuestion> findAllByBattleSubjectIdInAndIsDel(List<String> subjectIds,int isDel, Pageable pageable) {
		
		return battleRankQuestionDao.findAllByBattleSubjectIdInAndIsDel(subjectIds,isDel,pageable);
	}

	public void add(BattleRankQuestion battleRankQuestion) {
		
		battleRankQuestion.setId(UUID.randomUUID().toString());
		battleRankQuestion.setUpdateAt(new DateTime());
		battleRankQuestion.setCreateAt(new DateTime());
		
		battleRankQuestionDao.save(battleRankQuestion);
		
	}

	public void update(BattleRankQuestion battleRankQuestion) {
		
		battleRankQuestion.setUpdateAt(new DateTime());
		
		battleRankQuestionDao.save(battleRankQuestion);
		
	}
}
