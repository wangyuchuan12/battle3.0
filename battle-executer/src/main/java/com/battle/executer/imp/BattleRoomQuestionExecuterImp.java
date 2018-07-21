package com.battle.executer.imp;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.ScheduledExecuter;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattleUserRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.QuestionAnswerResultVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

public class BattleRoomQuestionExecuterImp implements BattleRoomQuestionExecuter{
	
	private BattleDataManager battleRoomDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	

	private ScheduledExecuter scheduledExecuter;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WxUserInfoService userInfoService;

	public void init(ExecuterStore executerStore){
		this.battleRoomDataManager = executerStore.getBattleDataManager();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
		this.scheduledExecuter = executerStore.getScheduledExecuter();
	}
	
	@Override
	public void startQuestions(){
		battleRoomDataManager.selectQuestions();
		startQuestion();
	}
	
	@Override
	public void startQuestion(){
		
		System.out.println(".........startQuestion");
		try{
			BattlePaperQuestionVo battlePaperQuestion = battleRoomDataManager.currentQuestion();
			if(battlePaperQuestion!=null){
				System.out.println(".........startQuestion1");
				battleRoomPublish.publishShowQuestion(battlePaperQuestion);
				scheduledExecuter.schedule(new Runnable() {
					@Override
					public void run() {
						startQuestion();
						battleRoomDataManager.nextQuestion();
						
					}
				}, battlePaperQuestion.getTimeLong()+1);
			}else{
				System.out.println(".........startQuestion2");
				EventManager eventManager = battleRoomDataManager.getEventManager();
				eventManager.publishEvent(Event.SUBMIT_RESULT, null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public synchronized void answerQuestion(QuestionAnswerVo questionAnswer) {
		
		try{
			BattleRoomVo battleRoom = battleRoomDataManager.getBattleRoom();
			BattlePaperQuestionVo battlePaperQuestionVo = battleRoomDataManager.currentQuestion();
			
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
			BattleUserRewardVo battleRewardVo = new BattleUserRewardVo();
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
				
				battleRewardVo.setStatus(BattleUserRewardVo.ADD_STATUS);
				
				battleRewardVo.setCnRightCount(cnRightCount);
				
				UserInfo userInfo = userInfoService.findOne(questionAnswer.getUserId());
				Account account = accountService.fineOneSync(userInfo.getAccountId());
				Long wisdomCount = account.getWisdomCount();
				
				wisdomCount=wisdomCount+battleRewardVo.getRewardBean();
				account.setWisdomCount(wisdomCount);
				accountService.update(account);
			}else{
				battleRoomMember.setCnRightCount(0);
				battleRewardVo.setStatus(BattleUserRewardVo.SUB_STATUS);
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void roomEnd() {
		
	}

}
