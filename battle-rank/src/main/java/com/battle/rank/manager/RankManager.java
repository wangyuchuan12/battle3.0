package com.battle.rank.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleQuestion;
import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankQuestion;
import com.battle.domain.BattleRankSubject;
import com.battle.domain.BattleSubject;
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
	public void addBySubjectId(String rankId,String periosId,String battleSubjectId,int num){
	
		BattleRankSubject battleRankSubject = battleRankSubjectService.findOneByBattleSubjectId(battleSubjectId);
		BattleSubject battleSubject = battleSubjectService.findOne(battleSubjectId);
		if(battleRankSubject==null){
			battleRankSubject = new BattleRankSubject();
			battleRankSubject.setBattleSubjectId(battleSubject.getId());
			battleRankSubject.setImgUrl(battleSubject.getImgUrl());
			battleRankSubject.setIsDel(0);
			battleRankSubject.setName(battleSubject.getName());
			battleRankSubjectService.add(battleRankSubject);
		}
		
		Pageable pageable = new PageRequest(0, num);
		List<BattleQuestion> battleQuestions = battleQuestionService.findAllByBattleIdAndPeriodIdAndSubjectIdIsDelRandom(battleSubject.getBattleId(), periosId, battleSubjectId, pageable);
		for(BattleQuestion battleQuestion:battleQuestions){
			BattleRankQuestion battleRankQuestion = new BattleRankQuestion();
			battleRankQuestion.setAnswer(battleQuestion.getAnswer());
			battleRankQuestion.setBattleId(battleQuestion.getBattleId());
			battleRankQuestion.setBattlePeriodId(periosId);
			battleRankQuestion.setBattleSubjectId(battleSubject.getId());
			battleRankQuestion.setImgUrl(battleQuestion.getImgUrl());
			battleRankQuestion.setIsDel(0);
			battleRankQuestion.setName(battleQuestion.getName());
			battleRankQuestion.setOptions(battleQuestion.getOptions());
			battleRankQuestion.setPeriodStageId(battleQuestion.getPeriodStageId());
			battleRankQuestion.setQuestion(battleQuestion.getQuestion());
			battleRankQuestion.setQuestionId(battleQuestion.getQuestionId());
			battleRankQuestion.setRightAnswer(battleQuestion.getRightAnswer());
			battleRankQuestion.setSeq(battleQuestion.getSeq());
			battleRankQuestion.setType(battleQuestion.getType());
			battleRankQuestionService.add(battleRankQuestion);
		}
	}
}
