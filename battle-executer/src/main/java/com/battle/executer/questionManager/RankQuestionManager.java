package com.battle.executer.questionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankQuestion;
import com.battle.domain.BattleRankSubject;
import com.battle.domain.Question;
import com.battle.domain.QuestionOption;
import com.battle.executer.BattleQuestionManager;
import com.battle.executer.vo.BattlePaperOptionVo;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.service.BattleRankQuestionService;
import com.battle.service.BattleRankService;
import com.battle.service.BattleRankSubjectService;
import com.battle.service.QuestionOptionService;
import com.battle.service.QuestionService;

public class RankQuestionManager implements BattleQuestionManager{

	private BattlePaperVo battlePaper;
	
	@Autowired
	private QuestionOptionService questionOptionService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private BattleRankSubjectService battleRankSubjectService;
	
	@Autowired
	private BattleRankQuestionService battleRankQuestionService;
	

	private String roomId;
	@Override
	public void init(Map<String, Object> data) {
		roomId = (String)data.get("roomId");
		battlePaper = new BattlePaperVo();
		battlePaper.setBattlePaperQuestions(new ArrayList<BattlePaperQuestionVo>());
		battlePaper.setBattleStages(new ArrayList<BattleStageVo>());
		battlePaper.setId(UUID.randomUUID().toString());
		battlePaper.setStageIndex(0);
		nextStage();
	}

	@Override
	public List<BattlePaperSubjectVo> getPaperSubjects(Integer stageIndex) {
		List<BattleStageVo> battleStageVos = battlePaper.getBattleStages();
		for(BattleStageVo battleStageVo:battleStageVos){
			if(battleStageVo.getStageIndex().intValue()==stageIndex.intValue()){
				return battleStageVo.getBattlePaperSubjects();
			}
		}
		return null;
	}

	@Override
	public BattleStageVo currentStage() {
		return battlePaper.currentStage();
	}

	@Override
	public List<BattlePaperQuestionVo> selectQuestions() {
		BattleStageVo battleStageVo = currentStage();
		List<BattlePaperSubjectVo> battlePaperSubjects = battleStageVo.getBattlePaperSubjects();
		
		List<String> subjectIds = new ArrayList<>();
		
		for(BattlePaperSubjectVo battlePaperSubjectVo:battlePaperSubjects){
			if(battlePaperSubjectVo.getIsSelect().intValue()==1){
				subjectIds.add(battlePaperSubjectVo.getBattleSubjectId());
			}
		}
		
		
		List<BattlePaperQuestionVo> selectQuestions = new ArrayList<>();

		if(subjectIds.size()==0){
			battleStageVo.setSelectBattlePaperQuestions(selectQuestions);
			battleStageVo.setQuestionCount(0);
			return selectQuestions;
		}
		
		Collections.shuffle(subjectIds);
		
		Pageable pageable = new PageRequest(0, battleStageVo.getQuestionCount());
		
		
		List<BattleRankQuestion> battleRankQuestions = battleRankQuestionService.findAllByBattleSubjectIdIn(subjectIds,pageable);

		for(int i=0;i<battleRankQuestions.size();i++){
			BattleRankQuestion battleRankQuestion = battleRankQuestions.get(i);
			BattlePaperQuestionVo battlePaperQuestionVo = new BattlePaperQuestionVo();
			battlePaperQuestionVo.setAnswer(battleRankQuestion.getAnswer());
			battlePaperQuestionVo.setBattleId(battleRankQuestion.getBattleId());
			battlePaperQuestionVo.setBattlePeriodId(battleRankQuestion.getBattlePeriodId());
			battlePaperQuestionVo.setBattleSubjectId(battleRankQuestion.getBattleSubjectId());
			battlePaperQuestionVo.setFillWords(battleRankQuestion.getFillWords());
			battlePaperQuestionVo.setImgUrl(battleRankQuestion.getImgUrl());
			battlePaperQuestionVo.setName(battleRankQuestion.getName());
			battlePaperQuestionVo.setPeriodStageId(battleRankQuestion.getPeriodStageId());
			battlePaperQuestionVo.setQuestion(battleRankQuestion.getQuestion());
			battlePaperQuestionVo.setQuestionId(battleRankQuestion.getQuestionId());
			battlePaperQuestionVo.setQuestionIndex(i);
			battlePaperQuestionVo.setRightAnswer(battleRankQuestion.getRightAnswer());
			battlePaperQuestionVo.setRoomId(roomId);
			battlePaperQuestionVo.setSeq(0);
			battlePaperQuestionVo.setTimeLong(battleStageVo.getTimeLong());
			battlePaperQuestionVo.setType(battleRankQuestion.getType());
			selectQuestions.add(battlePaperQuestionVo);
		}
		
		
		for(BattlePaperQuestionVo battlePaperQuestionVo:selectQuestions){
			if(battlePaperQuestionVo.getType().intValue()==Question.SELECT_TYPE){
				List<QuestionOption> questionOptions = questionOptionService.findAllByQuestionId(battlePaperQuestionVo.getQuestionId());
				List<BattlePaperOptionVo> battlePaperOptionVos = battlePaperQuestionVo.getOptions();
				for(QuestionOption questionOption:questionOptions){
					BattlePaperOptionVo battlePaperOptionVo = new BattlePaperOptionVo();
					battlePaperOptionVo.setContent(questionOption.getContent());
					battlePaperOptionVo.setId(questionOption.getId());
					
					if(questionOption.getContent().equals(battlePaperQuestionVo.getRightAnswer())){
						battlePaperOptionVo.setIsRight(1);
					}else{
						battlePaperOptionVo.setIsRight(0);
					}
					
					battlePaperOptionVos.add(battlePaperOptionVo);
				}
				
			}else if(battlePaperQuestionVo.getType().intValue()==Question.FILL_TYPE){
				Question question = questionService.findOne(battlePaperQuestionVo.getQuestionId());
				if(question!=null){
					String fillWords = question.getFillWords();
					battlePaperQuestionVo.setFillWords(fillWords);
				}
			}

		}
		battleStageVo.setSelectBattlePaperQuestions(selectQuestions);
		
		battleStageVo.setQuestionCount(selectQuestions.size());
		
		return selectQuestions;
	}

	@Override
	public void nextQuestion() {
		BattleStageVo battleStageVo = currentStage();
		int questionIndex = battleStageVo.getQuestionIndex();
		battleStageVo.setQuestionIndex(questionIndex+1);
		
	}

	@Override
	public BattlePaperQuestionVo currentQuestion() {
		BattleStageVo battleStageVo = currentStage();
		return battleStageVo.currentQuestion();
	}

	@Override
	public void nextStage() {
		try{
			List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
			BattleRank battleRank = null;
			if(battleRanks.size()>0){
				battleRank = battleRanks.get(0);
			}else{
				throw new RuntimeException("battleRank无记录");
			}
			
			Pageable pageable = new PageRequest(0, 9);
			List<BattleRankSubject> battleRankSubjects = battleRankSubjectService.findByRandom(pageable);
			BattleStageVo battleStageVo = currentStage();
			battlePaper.getBattleStages().remove(battleStageVo);
			int stageIndex = 0;
			
			if(battleStageVo==null){
				
			}else{
				stageIndex = battleStageVo.getStageIndex();
			}
			
			Map<String, List<BattlePaperQuestionVo>> battlePaperQuestionMap = new HashMap<>();
			List<BattlePaperSubjectVo> battlePaperSubjects = new ArrayList<>();
			
			for(int i=0;i<battleRankSubjects.size();i++){
				BattleRankSubject battleRankSubject = battleRankSubjects.get(i);
				BattlePaperSubjectVo battlePaperSubject = new BattlePaperSubjectVo();
				battlePaperSubject.setId(UUID.randomUUID().toString());
				battlePaperSubject.setImgUrl(battleRankSubject.getImgUrl());
				battlePaperSubject.setIndex(i);
				battlePaperSubject.setIsSelect(0);
				battlePaperSubject.setName(battleRankSubject.getName());
				battlePaperSubject.setStageIndex(stageIndex+1);
				battlePaperSubject.setBattleSubjectId(battleRankSubject.getBattleSubjectId());
				battlePaperSubjects.add(battlePaperSubject);
				
			}
			
			int questionCount = battleRank.getQuestionCount();
			int subjectCount = battleRank.getSubjectCount();
			int timeLong = battleRank.getTimeLong();
			battleStageVo = new BattleStageVo();
			battleStageVo.setBattlePaperQuestions(battlePaperQuestionMap);
			battleStageVo.setBattlePaperSubjects(battlePaperSubjects);
			battleStageVo.setId(UUID.randomUUID().toString());
			battleStageVo.setQuestionCount(questionCount);
			battleStageVo.setQuestionIndex(0);
			battleStageVo.setSelectBattlePaperQuestions(new ArrayList<BattlePaperQuestionVo>());
			battleStageVo.setStageIndex(stageIndex+1);
			battleStageVo.setStatus(BattleStageVo.STATUS_FREE);
			battleStageVo.setSubjectCount(subjectCount);
			battleStageVo.setTimeLong(timeLong);
			battlePaper.setStageIndex(battleStageVo.getStageIndex());
			List<BattleStageVo> battleStageVos = battlePaper.getBattleStages();
			battleStageVos.add(battleStageVo);
			
			System.out.println("................battleStageVos:"+battleStageVos);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public int stageCount() {
		// TODO Auto-generated method stub
		return 1000000000;
	}

	@Override
	public BattlePaperVo getBattlePaper() {
		return battlePaper;
	}

}
