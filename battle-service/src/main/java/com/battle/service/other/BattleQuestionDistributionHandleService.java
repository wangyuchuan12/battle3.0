package com.battle.service.other;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleQuestion;
import com.battle.domain.BattleQuestionDistribution;
import com.battle.domain.BattleQuestionDistributionQuestion;
import com.battle.domain.BattleQuestionDistributionStage;
import com.battle.domain.BattleQuestionDistributionSubject;
import com.battle.domain.BattleSubject;
import com.battle.service.BattleQuestionDistributionQuestionService;
import com.battle.service.BattleQuestionDistributionService;
import com.battle.service.BattleQuestionDistributionStageService;
import com.battle.service.BattleQuestionDistributionSubjectService;
import com.battle.service.BattleQuestionService;
import com.battle.service.BattleSubjectService;
import com.wyc.common.util.CommonUtil;

@Service
public class BattleQuestionDistributionHandleService {

	@Autowired
	private SimpleCacheManager simpleCacheManager;
	
	@Autowired
	private BattleQuestionDistributionService battleQuestionDistributionService;
	
	@Autowired
	private BattleQuestionDistributionStageService battleQuestionDistributionStageService;
	
	@Autowired
	private BattleQuestionDistributionSubjectService battleQuestionDistributionSubjectService;
	
	@Autowired
	private BattleQuestionDistributionQuestionService battleQuestionDistributionQuestionService;
	
	@Autowired
	private BattleQuestionService battleQuestionService;
	
	@Autowired
	private BattleSubjectService battleSubjectService;
	
	/**
	 * 
	 * @param battleId
	 * @param periodId
	 * @param stageData 第一个参数是关卡，第二个参数是主题id，第三个参数是每个主题有几道题
	 */
	public void flushDistribution(BattleQuestionDistribution battleQuestionDistribution){
		
		List<BattleQuestionDistributionStage> battleQuestionDistributionStages = battleQuestionDistributionStageService.findAllByDistributionIdOrderByStageIndexAsc(battleQuestionDistribution.getId());
		List<BattleQuestionDistributionSubject> battleQuestionDistributionSubjects = battleQuestionDistributionSubjectService.findAllByDistributionIdAndIsDel(battleQuestionDistribution.getId(),0);
		List<BattleQuestionDistributionQuestion> battleQuestionDistributionQuestions = battleQuestionDistributionQuestionService.findAllByDistributionIdAndIsDel(battleQuestionDistribution.getId(),0);
		
		List<BattleSubject> battleSubjects = battleSubjectService.findAllByBattleIdAndIsDelOrderBySeqAsc(battleQuestionDistribution.getBattleId(),0);
		
		for(BattleQuestionDistributionSubject battleQuestionDistributionSubject:battleQuestionDistributionSubjects){
			battleQuestionDistributionSubject.setIsDel(1);
			battleQuestionDistributionSubjectService.update(battleQuestionDistributionSubject);
		}
		
		for(BattleQuestionDistributionQuestion battleQuestionDistributionQuestion:battleQuestionDistributionQuestions){
			battleQuestionDistributionQuestion.setIsDel(1);
			battleQuestionDistributionQuestionService.update(battleQuestionDistributionQuestion);
		}
		
		for(BattleQuestionDistributionStage battleQuestionDistributionStage:battleQuestionDistributionStages){
			Integer averageQuestionCount = battleQuestionDistributionStage.getAverageQuestionCount();
			Integer subjectCount = battleQuestionDistributionStage.getSubjectCount();
			Collections.shuffle(battleSubjects);
			
			List<BattleSubject> subSubjects = battleSubjects.subList(0, subjectCount);
			
			Pageable pageable2 = new PageRequest(0, averageQuestionCount);
			int index = 0;
			for(BattleSubject battleSubject:subSubjects){
				index++;
				BattleQuestionDistributionSubject battleQuestionDistributionSubject = new BattleQuestionDistributionSubject();
				battleQuestionDistributionSubject.setDistributionId(battleQuestionDistribution.getId());
				battleQuestionDistributionSubject.setDistributionStageId(battleQuestionDistributionStage.getId());
				battleQuestionDistributionSubject.setImgUrl(battleSubject.getImgUrl());
				battleQuestionDistributionSubject.setIndex(index);
				battleQuestionDistributionSubject.setIsDel(0);
				battleQuestionDistributionSubject.setName(battleSubject.getName());
				battleQuestionDistributionSubject.setStageIndex(battleQuestionDistributionStage.getStageIndex());
				battleQuestionDistributionSubject.setBattleSubjectId(battleSubject.getId());
				battleQuestionDistributionSubjectService.add(battleQuestionDistributionSubject);
				List<BattleQuestion> battleQuestions = battleQuestionService.
						findAllByBattleIdAndPeriodIdAndSubjectIdIsDelRandom(battleQuestionDistribution.getBattleId(),battleQuestionDistribution.getPeriodId(),battleSubject.getId(),pageable2);
			
				for(BattleQuestion battleQuestion:battleQuestions){
					BattleQuestionDistributionQuestion battleQuestionDistributionQuestion = new BattleQuestionDistributionQuestion();
					battleQuestionDistributionQuestion.setAnswer(battleQuestion.getAnswer());
					battleQuestionDistributionQuestion.setBattleId(battleQuestion.getBattleId());
					battleQuestionDistributionQuestion.setBattlePeriodId(battleQuestion.getPeriodId());
					battleQuestionDistributionQuestion.setBattleSubjectId(battleQuestion.getSubjectId());
					battleQuestionDistributionQuestion.setDistributionId(battleQuestionDistribution.getId());
					battleQuestionDistributionQuestion.setDistributionStageId(battleQuestionDistributionStage.getId());
					battleQuestionDistributionQuestion.setDistributionSubjectId(battleQuestionDistributionSubject.getId());
					battleQuestionDistributionQuestion.setImgUrl(battleQuestion.getImgUrl());
					battleQuestionDistributionQuestion.setIsDel(0);
					battleQuestionDistributionQuestion.setName(battleQuestion.getName());
					battleQuestionDistributionQuestion.setOptions(battleQuestion.getOptions());
					battleQuestionDistributionQuestion.setPeriodStageId(battleQuestionDistributionStage.getId());
					battleQuestionDistributionQuestion.setQuestion(battleQuestion.getQuestion());
					battleQuestionDistributionQuestion.setQuestionId(battleQuestion.getQuestionId());
					battleQuestionDistributionQuestion.setRightAnswer(battleQuestion.getRightAnswer());
					battleQuestionDistributionQuestion.setSeq(battleQuestion.getSeq());
					battleQuestionDistributionQuestion.setType(battleQuestion.getType());
					
					if(CommonUtil.isEmpty(battleQuestionDistributionQuestion.getRightAnswer())){
						battleQuestionDistributionQuestion.setRightAnswer(battleQuestionDistributionQuestion.getAnswer());
					}
					
					if(CommonUtil.isEmpty(battleQuestionDistributionQuestion.getAnswer())){
						battleQuestionDistributionQuestion.setAnswer(battleQuestionDistributionQuestion.getRightAnswer());
					}
					
					battleQuestionDistributionQuestionService.add(battleQuestionDistributionQuestion);
				}
			}
			
		}
		
		battleQuestionDistribution.setStatus(BattleQuestionDistribution.STATUS_IN);
		
		battleQuestionDistributionService.update(battleQuestionDistribution);
	}
	
	
	public void saveToCache(BattleQuestionDistribution battleQuestionDistribution){
		
	}

	public BattleQuestionDistribution findOne(String id) {
		
		return battleQuestionDistributionService.findOne(id);
		
	}
}
