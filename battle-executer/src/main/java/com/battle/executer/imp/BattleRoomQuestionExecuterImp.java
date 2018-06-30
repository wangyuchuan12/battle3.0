package com.battle.executer.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.domain.Question;
import com.battle.domain.QuestionOption;
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.vo.BattlePaperOptionVo;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.QuestionAnswerResultVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.battle.service.QuestionOptionService;
import com.battle.service.QuestionService;
import com.battle.socket.MessageVo;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

public class BattleRoomQuestionExecuterImp implements BattleRoomQuestionExecuter{
	
	private BattleRoomDataManager battleRoomDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	@Autowired
	private QuestionOptionService questionOptionService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WxUserInfoService userInfoService;

	public void init(ExecuterStore executerStore){
		this.battleRoomDataManager = executerStore.getBattleRoomDataManager();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
	}
	
	@Override
	public void startQuestions(){
		List<BattleStageVo> battleStageVos = battleRoomDataManager.getBattlePaper().getBattleStages();
		Integer stageIndex = battleRoomDataManager.getBattlePaper().getStageIndex();
		BattleStageVo battleStageVo = null;
		
		for(BattleStageVo thisBattleStageVo:battleStageVos){
			if(thisBattleStageVo.getStageIndex().intValue()==stageIndex.intValue()){
				battleStageVo = thisBattleStageVo;
			}
		}
		
		Map<String,List<BattlePaperQuestionVo>> battlePaperQuestionMap = battleStageVo.getBattlePaperQuestions();
		
		
		System.out.println("...........battlePaperQuestionMap:"+battlePaperQuestionMap);
		List<BattlePaperSubjectVo> battlePaperSubjects = battleStageVo.getBattlePaperSubjects();
		
		List<BattlePaperSubjectVo> selectPaperSubjects = new ArrayList<>();
		
		for(BattlePaperSubjectVo battlePaperSubjectVo:battlePaperSubjects){
			if(battlePaperSubjectVo.getIsSelect().intValue()==1){
				selectPaperSubjects.add(battlePaperSubjectVo);
			}
		}
		
		System.out.println("........................111");
		
		List<BattlePaperQuestionVo> selectQuestions = new ArrayList<>();
		
		Integer questionCount = battleStageVo.getQuestionCount();
		
		Collections.shuffle(selectPaperSubjects);
		
		System.out.println("........................222");
		for(BattlePaperSubjectVo battlePaperSubjectVo:selectPaperSubjects){
			if(selectQuestions.size()<questionCount){
				List<BattlePaperQuestionVo> subjectBattlePaperQuestions = battlePaperQuestionMap.get(battlePaperSubjectVo.getId());
				if(subjectBattlePaperQuestions.size()>0){
					Collections.shuffle(subjectBattlePaperQuestions);
					selectQuestions.add(subjectBattlePaperQuestions.get(0));
				}
			}
		}
		
		System.out.println("........................333");
		
		if(selectQuestions.size()<questionCount){
			Collections.shuffle(selectPaperSubjects);
			for(BattlePaperSubjectVo battlePaperSubjectVo:selectPaperSubjects){
				if(selectQuestions.size()<questionCount){
					List<BattlePaperQuestionVo> subjectBattlePaperQuestions = battlePaperQuestionMap.get(battlePaperSubjectVo.getId());
					if(subjectBattlePaperQuestions.size()>0){
						Collections.shuffle(subjectBattlePaperQuestions);
						selectQuestions.add(subjectBattlePaperQuestions.get(0));
					}
				}
			}
		}
		
		System.out.println("........................444");
		
		for(BattlePaperQuestionVo battlePaperQuestionVo:selectQuestions){
			
			System.out.println("........................555");
			if(battlePaperQuestionVo.getType().intValue()==Question.SELECT_TYPE){
				List<QuestionOption> questionOptions = questionOptionService.findAllByQuestionId(battlePaperQuestionVo.getQuestionId());
				List<BattlePaperOptionVo> battlePaperOptionVos = battlePaperQuestionVo.getOptions();
				
				System.out.println("...........questionOptions:"+questionOptions);
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
				
				System.out.println("........................666");
				Question question = questionService.findOne(battlePaperQuestionVo.getQuestionId());
				System.out.println("........................888");
				if(question!=null){
					System.out.println("........................999");
					String fillWords = question.getFillWords();
					System.out.println("........................000");
					battlePaperQuestionVo.setFillWords(fillWords);
					System.out.println("........................1111");
				}
			}
		}
		
		System.out.println("........................777");
		
		System.out.println("...........startQuestions.selectQuestions:"+selectQuestions);
		battleStageVo.setSelectBattlePaperQuestions(selectQuestions);
		startQuestion();
	}
	
	@Override
	public void startQuestion(){
		
		System.out.println("...........startQuestion");
		List<BattleStageVo> battleStageVos = battleRoomDataManager.getBattlePaper().getBattleStages();
		Integer stageIndex = battleRoomDataManager.getBattlePaper().getStageIndex();
		BattleStageVo battleStageVo = null;
		for(BattleStageVo thisBattleStageVo:battleStageVos){
			if(thisBattleStageVo.getStageIndex().intValue()==stageIndex.intValue()){
				battleStageVo = thisBattleStageVo;
			}
		}
		
		
		List<BattlePaperQuestionVo> battlePaperQuestions = battleStageVo.getSelectBattlePaperQuestions();
		Integer questionIndex = battleStageVo.getQuestionIndex();
		if(questionIndex<battlePaperQuestions.size()){
			BattlePaperQuestionVo battlePaperQuestion = battlePaperQuestions.get(questionIndex);
			battleRoomPublish.publishShowQuestion(battlePaperQuestion);
			
			final BattleStageVo inBattleStageVo = battleStageVo;
			scheduledExecutorService.schedule(new Runnable() {
				
				@Override
				public void run() {
					inBattleStageVo.setQuestionIndex(inBattleStageVo.getQuestionIndex()+1);
					startQuestion();
					
				}
			}, battlePaperQuestion.getTimeLong(), TimeUnit.SECONDS);
		}else{
			EventManager eventManager = battleRoomDataManager.getEventManager();
			eventManager.publishEvent(Event.SUBMIT_RESULT, null);
		}
	}
	
	@Override
	public synchronized void answerQuestion(QuestionAnswerVo questionAnswer) {
		
		BattleStageVo battleStageVo = battleRoomDataManager.getBattlePaper().currentStage();
		
		BattleRoomVo battleRoom = battleRoomDataManager.getBattleRoom();
		BattlePaperQuestionVo battlePaperQuestionVo = battleStageVo.currentQuestion();
		
		List<QuestionAnswerVo> questionAnswerVos = battlePaperQuestionVo.getQuestionAnswerVos();

		questionAnswerVos.add(questionAnswer);

		QuestionAnswerResultVo questionAnswerResultVo = new QuestionAnswerResultVo();		
		questionAnswerResultVo.setAnswer(questionAnswer.getMyAnswer());
		questionAnswerResultVo.setUserImg(questionAnswer.getUserImg());
		questionAnswerResultVo.setUserName(questionAnswer.getUserName());
		questionAnswerResultVo.setOptionId(questionAnswer.getOptionId());
		questionAnswerResultVo.setType(questionAnswer.getType());
		
		Integer isRight = 0;
		
		if(CommonUtil.isNotEmpty(questionAnswer.getMyAnswer())){
			if(questionAnswer.getMyAnswer().equals(battlePaperQuestionVo.getRightAnswer())){
				isRight = 1;
			}
		}
		questionAnswerResultVo.setIsRight(isRight);
		battleRoomPublish.publishDoAnswer(questionAnswerResultVo);
		BattleRoomMemberVo battleRoomMember = battleRoomDataManager.getBattleMemberByUserId(questionAnswer.getUserId());
		BattleRewardVo battleRewardVo = new BattleRewardVo();
		battleRewardVo.setId(UUID.randomUUID().toString());
		battleRewardVo.setImgUrl(questionAnswer.getUserImg());
		battleRewardVo.setNickname(battleRewardVo.getNickname());
		battleRewardVo.setUserId(questionAnswer.getUserId());
		if(isRight.intValue()==1){
			Integer cnRightCount = battleRoomMember.getCnRightCount();
			cnRightCount++;
			battleRoomMember.setCnRightCount(cnRightCount);
			if(cnRightCount.intValue()==1){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean());
			}else if(cnRightCount.intValue()==2){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean2());
			}else if(cnRightCount.intValue()==3){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean3());
			}else if(cnRightCount.intValue()==4){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean4());
			}else if(cnRightCount.intValue()==5){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean5());
			}else if(cnRightCount.intValue()==6){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean6());
			}else if(cnRightCount.intValue()==7){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean7());
			}else if(cnRightCount.intValue()==8){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean8());
			}else if(cnRightCount.intValue()==9){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean9());
			}else if(cnRightCount.intValue()>=10){
				battleRewardVo.setRewardBean(battleRoom.getRewardBean10());
			}
			
			battleRewardVo.setStatus(BattleRewardVo.ADD_STATUS);
			
			battleRewardVo.setCnRightCount(cnRightCount);
			
			UserInfo userInfo = userInfoService.findOne(questionAnswer.getUserId());
			Account account = accountService.fineOneSync(userInfo.getAccountId());
			Long wisdomCount = account.getWisdomCount();
			
			wisdomCount=wisdomCount+battleRewardVo.getRewardBean();
			account.setWisdomCount(wisdomCount);
			accountService.update(account);
		}else{
			battleRoomMember.setCnRightCount(0);
			battleRewardVo.setStatus(BattleRewardVo.SUB_STATUS);
			battleRewardVo.setSubBean(battleRoom.getSubBean());
			UserInfo userInfo = userInfoService.findOne(questionAnswer.getUserId());
			Account account = accountService.fineOneSync(userInfo.getAccountId());
			Long wisdomCount = account.getWisdomCount();
			wisdomCount = wisdomCount - battleRewardVo.getSubBean();
			if(wisdomCount<0){
				wisdomCount = 0L;
			}
			account.setWisdomCount(wisdomCount);
			accountService.update(account);
		}
		
		battleRoomPublish.publishReward(battleRewardVo);
		
	}

	@Override
	public void roomEnd() {
		scheduledExecutorService = null;
	}

}
