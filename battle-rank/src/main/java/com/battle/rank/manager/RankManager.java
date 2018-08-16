package com.battle.rank.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.domain.BattlePeriod;
import com.battle.domain.BattleQuestion;
import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankQuestion;
import com.battle.domain.BattleRankSubject;
import com.battle.domain.BattleSubject;
import com.battle.service.BattlePeriodService;
import com.battle.service.BattleQuestionService;
import com.battle.service.BattleRankQuestionService;
import com.battle.service.BattleRankService;
import com.battle.service.BattleRankSubjectService;
import com.battle.service.BattleSubjectService;

@Service
public class RankManager {
	
	@Autowired
	private BattleRankSubjectService battleRankSubjectService;
	
	@Autowired
	private BattleSubjectService battleSubjectService;
	
	@Autowired
	private BattleQuestionService battleQuestionService;
	
	@Autowired
	private BattleRankQuestionService battleRankQuestionService;
	
	@Autowired
	private BattlePeriodService battlePeriodService;
	
	@Autowired
	private BattleRankService battleRankService;
	
	public void addByPeriodId(String rankId,String periodId){
		BattlePeriod battlePeriod = battlePeriodService.findOne(periodId);
		Pageable pageable = new PageRequest(0,100);
		List<BattleSubject> battleSubjects = battleSubjectService.findAllByBattleIdAndIsDel(battlePeriod.getBattleId(), 0, pageable);
		
		for(BattleSubject battleSubject:battleSubjects){
			addBySubjectId(rankId, periodId, battleSubject.getId(), 500);
		}
	}
	
	
	
	public void addBySubjectId(String rankId,String periosId,String battleSubjectId,int num){
	
		
		BattleRank battleRank = battleRankService.findOne(rankId);
		
		if(battleRank==null){
			throw new RuntimeException();
		}
		BattleRankSubject battleRankSubject = battleRankSubjectService.findOneByBattleSubjectIdAndIsDel(battleSubjectId,0);
		BattleSubject battleSubject = battleSubjectService.findOne(battleSubjectId);
		if(battleRankSubject!=null){
			battleRankSubject.setIsDel(1);
			battleRankSubjectService.update(battleRankSubject);
		}
		
		
		battleRankSubject = new BattleRankSubject();
		battleRankSubject.setBattleSubjectId(battleSubject.getId());
		battleRankSubject.setImgUrl(battleSubject.getImgUrl());
		battleRankSubject.setIsDel(0);
		battleRankSubject.setName(battleSubject.getName());
		battleRankSubject.setRankId(rankId);
		battleRankSubjectService.add(battleRankSubject);
		
		List<String> subjectIds = new ArrayList<>();
		subjectIds.add(battleSubject.getId());
		
		Pageable pageable = new PageRequest(0, 10000);
		List<BattleRankQuestion> battleRankQuestions = battleRankQuestionService.findAllByBattleSubjectIdInAndIsDel(subjectIds, 0,pageable);
		
		for(BattleRankQuestion battleRankQuestion:battleRankQuestions){
			battleRankQuestion.setIsDel(1);
			battleRankQuestionService.update(battleRankQuestion);
		}
		
		pageable = new PageRequest(0, num);
		List<BattleQuestion> battleQuestions = battleQuestionService.findAllByBattleIdAndPeriodIdAndSubjectIdAndIsDelRandom(battleSubject.getBattleId(), periosId, battleSubjectId, 0,pageable);
		
		System.out.println("******************:"+battleQuestions);
		for(BattleQuestion battleQuestion:battleQuestions){
			BattleRankQuestion battleRankQuestion = new BattleRankQuestion();
			battleRankQuestion.setAnswer(battleQuestion.getAnswer());
			battleRankQuestion.setBattleId(battleQuestion.getBattleId());
			battleRankQuestion.setBattlePeriodId(periosId);
			battleRankQuestion.setBattleSubjectId(battleQuestion.getSubjectId());
			battleRankQuestion.setImgUrl(battleQuestion.getImgUrl());
			battleRankQuestion.setIsDel(0);
			battleRankQuestion.setName(battleQuestion.getName());
			battleRankQuestion.setOptions(battleQuestion.getOptions());
			battleRankQuestion.setQuestion(battleQuestion.getQuestion());
			battleRankQuestion.setQuestionId(battleQuestion.getQuestionId());
			battleRankQuestion.setRightAnswer(battleQuestion.getAnswer());
			battleRankQuestion.setSeq(battleQuestion.getSeq());
			battleRankQuestion.setType(battleQuestion.getType());
			battleRankQuestion.setRankId(rankId);
			battleRankQuestionService.add(battleRankQuestion);
		}
	}
}
