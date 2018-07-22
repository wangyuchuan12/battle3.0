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

import com.battle.domain.BattleQuestionDistribution;
import com.battle.domain.BattleQuestionDistributionQuestion;
import com.battle.domain.BattleQuestionDistributionStage;
import com.battle.domain.BattleQuestionDistributionSubject;
import com.battle.domain.Question;
import com.battle.domain.QuestionOption;
import com.battle.executer.BattleQuestionManager;
import com.battle.executer.vo.BattlePaperOptionVo;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.service.BattleQuestionDistributionQuestionService;
import com.battle.service.BattleQuestionDistributionService;
import com.battle.service.BattleQuestionDistributionStageService;
import com.battle.service.BattleQuestionDistributionSubjectService;
import com.battle.service.QuestionOptionService;
import com.battle.service.QuestionService;

public class DistributionQuestionManager implements BattleQuestionManager{

	private BattlePaperVo battlePaper;
	
	@Autowired
	private BattleQuestionDistributionStageService battleQuestionDistributionStageService;
	
	@Autowired
	private BattleQuestionDistributionSubjectService battleQuestionDistributionSubjectService;
	
	@Autowired
	private BattleQuestionDistributionQuestionService battleQuestionDistributionQuestionService;
	
	@Autowired
	private BattleQuestionDistributionService battleQuestionDistributionService;
	
	@Autowired
	private QuestionOptionService questionOptionService;
	
	@Autowired
	private QuestionService questionService;
	@Override
	public void init(Map<String, Object> data) {
		List<String> userIds = (List<String>)data.get("userIds");
		String roomId = (String)data.get("roomId");
		String groupId = (String)data.get("groupId");

		List<BattleQuestionDistribution> battleQuestionDistributions = battleQuestionDistributionService.findAllByGroupId(groupId);
		
		BattleQuestionDistribution battleQuestionDistribution;
		
		if(battleQuestionDistributions.size()>0){
			Collections.shuffle(battleQuestionDistributions);
			battleQuestionDistribution = battleQuestionDistributions.get(0);
		}else{
			throw new RuntimeException("battleQuestionDistribution 为空");
		}
		
		battlePaper = new BattlePaperVo();
		battlePaper.setId(UUID.randomUUID().toString());
		battlePaper.setStageIndex(1);
		
		List<BattleStageVo> battleStageVos = new ArrayList<>();
		
		
		battlePaper.setBattleStages(battleStageVos);
		
		List<BattleQuestionDistributionStage> battleQuestionDistributionStages = battleQuestionDistributionStageService.findAllByDistributionIdOrderByStageIndexAsc(battleQuestionDistribution.getId());
		
		
		for(int i = 0;i<battleQuestionDistributionStages.size();i++){
			BattleQuestionDistributionStage battleQuestionDistributionStage = battleQuestionDistributionStages.get(i);
			List<BattleQuestionDistributionSubject> battleQuestionDistributionSubjects = battleQuestionDistributionSubjectService.findAllByDistributionIdAndDistributionStageIdAndIsDel(battleQuestionDistribution.getId(),battleQuestionDistributionStage.getId(),0);
			
			Pageable pageable = new PageRequest(0, 100);
			List<BattleQuestionDistributionQuestion> battleQuestionDistributionQuestions = battleQuestionDistributionQuestionService.findAllByDistributionIdAndDistributionStageIdAndIsDel(battleQuestionDistribution.getId(),battleQuestionDistributionStage.getId(),0,pageable);
			Map<String, List<BattlePaperQuestionVo>> battlePaperQuestionMap = new HashMap<>();
			for(BattleQuestionDistributionQuestion battleQuestionDistributionQuestion:battleQuestionDistributionQuestions){
				List<BattlePaperQuestionVo> battlePaperQuestions = battlePaperQuestionMap.get(battleQuestionDistributionQuestion.getBattleSubjectId());
				if(battlePaperQuestions==null){
					battlePaperQuestions = new ArrayList<>();
					battlePaperQuestionMap.put(battleQuestionDistributionQuestion.getBattleSubjectId(), battlePaperQuestions);
				}
				
				BattlePaperQuestionVo battlePaperQuestionVo = new BattlePaperQuestionVo();
				battlePaperQuestionVo.setAnswer(battleQuestionDistributionQuestion.getAnswer());
				battlePaperQuestionVo.setBattlePeriodId(battleQuestionDistributionQuestion.getBattlePeriodId());
				battlePaperQuestionVo.setBattleSubjectId(battleQuestionDistributionQuestion.getBattleSubjectId());
				battlePaperQuestionVo.setImgUrl(battleQuestionDistributionQuestion.getImgUrl());
				battlePaperQuestionVo.setName(battleQuestionDistributionQuestion.getName());
				battlePaperQuestionVo.setPeriodStageId(battleQuestionDistributionQuestion.getPeriodStageId());
				battlePaperQuestionVo.setQuestion(battleQuestionDistributionQuestion.getQuestion());
				battlePaperQuestionVo.setQuestionId(battleQuestionDistributionQuestion.getQuestionId());
				battlePaperQuestionVo.setRightAnswer(battleQuestionDistributionQuestion.getRightAnswer());
				battlePaperQuestionVo.setSeq(battleQuestionDistributionQuestion.getSeq());
				battlePaperQuestionVo.setType(battleQuestionDistributionQuestion.getType());
				battlePaperQuestionVo.setQuestionIndex(i+1);
				battlePaperQuestionVo.setRoomId(roomId);
				battlePaperQuestionVo.setTimeLong(10);
				battlePaperQuestions.add(battlePaperQuestionVo);
			}
			
			List<BattlePaperSubjectVo> battlePaperSubjects = new ArrayList<>();
			
			for(BattleQuestionDistributionSubject battleQuestionDistributionSubject:battleQuestionDistributionSubjects){
				BattlePaperSubjectVo battlePaperSubjectVo = new BattlePaperSubjectVo();
				battlePaperSubjectVo.setId(battleQuestionDistributionSubject.getBattleSubjectId());
				battlePaperSubjectVo.setDistributionId(battleQuestionDistributionSubject.getDistributionId());
				battlePaperSubjectVo.setDistributionStageId(battleQuestionDistributionSubject.getDistributionStageId());
				battlePaperSubjectVo.setImgUrl(battleQuestionDistributionSubject.getImgUrl());
				battlePaperSubjectVo.setIndex(battleQuestionDistributionSubject.getIndex());
				battlePaperSubjectVo.setName(battleQuestionDistributionSubject.getName());
				battlePaperSubjectVo.setStageIndex(battleQuestionDistributionSubject.getStageIndex());
				battlePaperSubjectVo.setIsSelect(0);
				
				List<BattlePaperQuestionVo> battlePaperQuestionVos = battlePaperQuestionMap.get(battlePaperSubjectVo.getId());
				
				if(battlePaperQuestionVos!=null&&battlePaperQuestionVos.size()>0){
					battlePaperSubjects.add(battlePaperSubjectVo);
				}
			}
			
			Integer userNum = userIds.size();
			Integer subjectNum = battlePaperSubjects.size();
			
			BattleStageVo battleStage = new BattleStageVo();
			if(userNum>subjectNum){
				battleStage.setSubjectCount(subjectNum);
			}else{
				battleStage.setSubjectCount(userNum);
			}
			
			battleStage.setQuestionCount(battleQuestionDistributionStage.getQuestionCount());
			battleStage.setStageIndex(battleQuestionDistributionStage.getStageIndex());
			battleStage.setBattlePaperQuestions(battlePaperQuestionMap);
			battleStage.setBattlePaperSubjects(battlePaperSubjects);
			battleStage.setTimeLong(10);
			battleStage.setQuestionIndex(0);
			battleStageVos.add(battleStage);
		}
		
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
		Map<String,List<BattlePaperQuestionVo>> battlePaperQuestionMap = battleStageVo.getBattlePaperQuestions();
		
		List<BattlePaperSubjectVo> battlePaperSubjects = battleStageVo.getBattlePaperSubjects();
		
		List<BattlePaperSubjectVo> selectPaperSubjects = new ArrayList<>();
		
		for(BattlePaperSubjectVo battlePaperSubjectVo:battlePaperSubjects){
			if(battlePaperSubjectVo.getIsSelect().intValue()==1){
				selectPaperSubjects.add(battlePaperSubjectVo);
			}
		}
		
		
		List<BattlePaperQuestionVo> selectQuestions = new ArrayList<>();
		
		Integer questionCount = battleStageVo.getQuestionCount();
		
		Collections.shuffle(selectPaperSubjects);
		
		List<String> selectQuestionIds = new ArrayList<>();
		
		for(BattlePaperSubjectVo battlePaperSubjectVo:selectPaperSubjects){
			if(selectQuestions.size()<questionCount){
				List<BattlePaperQuestionVo> subjectBattlePaperQuestions = battlePaperQuestionMap.get(battlePaperSubjectVo.getId());
				if(subjectBattlePaperQuestions.size()>0){
					Collections.shuffle(subjectBattlePaperQuestions);
					selectQuestions.add(subjectBattlePaperQuestions.get(0));
					selectQuestionIds.add(subjectBattlePaperQuestions.get(0).getQuestionId());
				}
			}
		}
		
		
		
		if(selectQuestions.size()<questionCount){
			Collections.shuffle(selectPaperSubjects);
			for(BattlePaperSubjectVo battlePaperSubjectVo:selectPaperSubjects){
				if(selectQuestions.size()<questionCount){
					List<BattlePaperQuestionVo> subjectBattlePaperQuestions = battlePaperQuestionMap.get(battlePaperSubjectVo.getId());
					if(subjectBattlePaperQuestions.size()>0){
						Collections.shuffle(subjectBattlePaperQuestions);
						if(!selectQuestionIds.contains(subjectBattlePaperQuestions.get(0).getQuestionId())){
							selectQuestions.add(subjectBattlePaperQuestions.get(0));
						}
					}
				}
			}
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
		
		int currentStageIndex = battlePaper.getStageIndex();
		battlePaper.setStageIndex(currentStageIndex+1);
		
	}

	@Override
	public int stageCount() {

		return battlePaper.getBattleStages().size();
	}

	@Override
	public BattlePaperVo getBattlePaper() {

		return battlePaper;
	}

}
