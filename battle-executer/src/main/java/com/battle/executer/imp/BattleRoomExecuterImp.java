package com.battle.executer.imp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.battle.domain.UserStatus;
import com.battle.event.vo.TakepartEvent;
import com.battle.event.vo.TakepartVo;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRestEvent;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomMemberTakepart;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomRewardRecord;
import com.battle.executer.vo.BattleRoomShareRewardVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.battle.service.UserStatusService;
import com.battle.service.other.BattleRoomCoolHandle;
import com.wyc.ApplicationContextProvider;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

public class BattleRoomExecuterImp implements BattleRoomExecuter{

	private EventManager eventManager;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	
	private BattleDataManager battleDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	@Autowired
	private UserStatusService userStatusService;
	
	private BattleRoomMemberTakepart battleRoomMemberTakepart;
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;


	@Override
	public void answerQuestion(QuestionAnswerVo questionAnswer){
		battleRoomQuestionExecuter.answerQuestion(questionAnswer);
	}

	@Override
	public void signOut(String userId){
		
		List<BattleRoomMemberVo> battleRoomMembers = battleDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			if(battleRoomMember.getUserId().equals(userId)){
				battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_OUT);
				battleRoomMember.setIsOut(1);
			}
		}
	}

	@Override
	public void init(EventManager eventManager,ExecuterStore executerStore){
		
		this.eventManager = eventManager;
		this.battleDataManager = executerStore.getBattleDataManager();
		this.battleRoomQuestionExecuter = executerStore.getBattleQuestionExecuter();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
		this.battleRoomMemberTakepart = executerStore.getBattleRoomMemberTakepart();
		this.startRoom();
	}

	@Override
	public void subjectReady(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDouble(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doNotDouble(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startRoom(){
		battleRoomPublish.publishRoomStart();
		eventManager.publishEvent(Event.START_ROOM_CODE, null);
		
		List<BattleRoomMemberVo> battleRoomMembers = battleDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		
		BattleRoomVo battleRoom = battleDataManager.getBattleRoom();
		
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			UserStatus userStatus = userStatusService.findOneByUserId(battleRoomMember.getUserId());
			userStatus.setRoomId(battleRoom.getId());
			userStatus.setStatus(UserStatus.IN_STATUS);
			userStatusService.update(userStatus);
		}
	
	}

	@Override
	public void endRoom(){

		BattleRoomVo battleroom = battleDataManager.getBattleRoom();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus transactionStatus = platformTransactionManager.getTransaction(def);

		List<BattleRoomMemberVo> battleRoomMembers = battleDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		
		
		List<BattleRoomRewardRecord> battleRoomRewardRecords = battleDataManager.getBattleRoom().getBattleRoomRewardRecords();
		Map<Integer, BattleRoomRewardRecord> battleRoomRewardRecordMap = new HashMap<>();
		for(BattleRoomRewardRecord battleRoomRewardRecord:battleRoomRewardRecords){
			battleRoomRewardRecordMap.put(battleRoomRewardRecord.getRank(), battleRoomRewardRecord);
		}
		
		
		Collections.sort(battleRoomMembers, new Comparator<BattleRoomMemberVo>() {

			@Override
			public int compare(BattleRoomMemberVo o1, BattleRoomMemberVo o2) {
				Integer process1 = o1.getProcess();
				Integer process2 = o2.getProcess();
				if(process1>process2){
					return -1;
				}else if(process1==process2){
					return 0;
				}else{
					return 1;
				}
			}
		});
		
		for(int i=0;i<battleRoomMembers.size();i++){
			BattleRoomMemberVo battleRoomMember = battleRoomMembers.get(i);
			battleRoomMember.setIsEnd(1);
			battleRoomMember.setEndCotent("比赛已经结束");
			BattleRoomRewardRecord battleRoomRewardRecord = battleRoomRewardRecordMap.get(i+1);
			if(battleRoomRewardRecord!=null){
				battleRoomMember.setRewardBean(battleRoomRewardRecord.getRewardBean());
				battleRoomMember.setRewardLove(battleRoomRewardRecord.getRewardLove());
				battleRoomMember.setRank(battleRoomRewardRecord.getRank());
				
				try{
					Account account = accountService.fineOneSync(battleRoomMember.getAccountId());
					Long wisdomCount = account.getWisdomCount();
					Integer loveCount = account.getLoveLife();
					wisdomCount = wisdomCount+battleRoomMember.getRewardBean();
					loveCount = loveCount + battleRoomMember.getRewardLove();
					account.setWisdomCount(wisdomCount);
					account.setLoveLife(loveCount);
					accountService.update(account);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				battleRoomMember.setRewardBean(0);
				battleRoomMember.setRewardLove(0);
				battleRoomMember.setRank(i+1);
			}
			
			Map<String, Object> data = battleroom.getData();
			Integer passNum = null;
			if(data!=null){
				passNum = (Integer)data.get("passNum");
			}
			if(passNum!=null&&passNum>0){
				if(battleRoomMember.getRank()<=passNum){
					battleRoomMember.setIsPass(1);
				}else{
					battleRoomMember.setIsPass(0);
				}
			}else{
				if(battleRoomMember.getRemainLove().intValue()>0){
					battleRoomMember.setIsPass(1);
				}else{
					battleRoomMember.setIsPass(0);
				}
			}
		}
		battleRoomPublish.publishRoomEnd();
		
		platformTransactionManager.commit(transactionStatus);
	}

	@Override
	public synchronized void subjectSelect(String subjectId, String userId){
		BattlePaperVo battlePaperVo = battleDataManager.getBattlePaper();
		
		BattleRoomVo battleRoomVo = battleDataManager.getBattleRoom();
		
		Integer stageIndex = battlePaperVo.getStageIndex();
		
		List<BattleStageVo> battleStages = battlePaperVo.getBattleStages();
		
		BattleStageVo battleStage = null;
		
		BattleRoomMemberVo battleRoomMember = null;
		
		for(BattleStageVo thisBattleStage:battleStages){
			if(thisBattleStage.getStageIndex().intValue()==stageIndex.intValue()){
				battleStage = thisBattleStage;
			}
		}
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomVo.getMembers();
		
		for(BattleRoomMemberVo thisBattleRoomMember:battleRoomMemberVos){
			
			if(thisBattleRoomMember.getUserId().equals(userId)){
				battleRoomMember = thisBattleRoomMember;
			}
		}
		
		BattlePaperSubjectVo battlePaperSubject = null;
		
		List<BattlePaperSubjectVo> battlePaperSubjects = battleStage.getBattlePaperSubjects();
		
		
		for(BattlePaperSubjectVo thisBattlePaperSubject: battlePaperSubjects){
		
			if(thisBattlePaperSubject.getId().equals(subjectId)){
				battlePaperSubject = thisBattlePaperSubject;
			}
		}
		
		if(battlePaperSubject==null||battlePaperSubject.getIsSelect()==1){
			return;
		}
		
		battlePaperSubject.setIsSelect(1);
		
		battlePaperSubject.setSelectUserId(battleRoomMember.getUserId());
		
		battlePaperSubject.setSelectUserImg(battleRoomMember.getImgUrl());
		
		battleRoomPublish.publishShowSubjectStatus(battlePaperSubject);
	}

	@Override
	public void submitResults() {

		battleDataManager.clearMembers();
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE);
		if(battleRoomMemberVos.size()==0){
			Map<String, Object> data = new HashMap<>();
			eventManager.publishEvent(Event.ROOM_END_CODE, data);
		}
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		

		applicationContext.publishEvent(new BattleRestEvent(battleDataManager.getBattleRoom()));
		
		
		List<BattleRoomMemberVo> battleRoomMembers = battleDataManager.getBattleMembers();
		
		if(battleRoomMembers==null){
			battleRoomMembers = new ArrayList<>();
		}
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			EventManager eventManager = battleDataManager.getEventManager();
			Map<String, Object> data = new HashMap<>();
			data.put("myInfo", battleRoomMember);
			eventManager.publishEvent(Event.MY_INFO, data);
			if(battleRoomMember.getRemainLove()<=0){
				Map<String, Object> publishData = new HashMap<>();
				publishData.put("member", battleRoomMember);
				publishData.put("type", BattleRoomPublish.LOVE_DIE_TYPE);
				eventManager.publishEvent(Event.PUBLISH_DIE, publishData);
			}
		}
		
		
		
		battleRoomPublish.publishRest();
		battleRoomPublish.publishMembers();
		
	}

	@Override
	public void roomInfo() {
		battleRoomPublish.publishMembers();
	}

	@Override
	public BattleRoomMemberVo takepart(UserInfo userInfo){
		BattleRoomMemberVo battleRoomMemberVo =  battleRoomMemberTakepart.takepart(userInfo);
		final EventManager eventManager = battleDataManager.getEventManager();
		if(battleRoomMemberVo.getBeanNum().intValue()<=0){
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_DIE);
			Map<String, Object> data = new HashMap<>();
			data.put("member", battleRoomMemberVo);
			data.put("type", BattleRoomPublish.BEAN_DIE_TYPE);
			eventManager.publishEvent(Event.PUBLISH_DIE, data);
		}else if(battleRoomMemberVo.getRemainLove().intValue()<=0){
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_DIE);
			Map<String, Object> data = new HashMap<>();
			data.put("member", battleRoomMemberVo);
			data.put("type", BattleRoomPublish.BEAN_DIE_TYPE);
			eventManager.publishEvent(Event.PUBLISH_DIE, data);
		}
		
		TakepartVo takepartVo = new TakepartVo();
		takepartVo.setRankId(battleDataManager.getRankId());
		takepartVo.setRoomId(battleDataManager.getBattleRoom().getId());
		takepartVo.setUserId(userInfo.getId());
		ApplicationContextProvider.getApplicationContext().publishEvent(new TakepartEvent(takepartVo));
		
		return battleRoomMemberVo;
		
	}

	@Override
	public BattleRoomVo getRoom(){
		BattleRoomVo battleRoom = battleDataManager.getBattleRoom();
		return battleRoom;
	}

	@Override
	public boolean superLove(UserInfo userInfo){
		
		Account account = accountService.fineOne(userInfo.getAccountId());
		Integer love = account.getLoveLife();
		if(love==null||love==0){
			return false;
		}else{
			BattleRoomMemberVo battleRoomMember = battleDataManager.getBattleMemberByUserId(userInfo.getId());
			Integer remainLove = battleRoomMember.getRemainLove();
			Integer limitLove = battleRoomMember.getLimitLove();
			int diff = limitLove-remainLove;
			if(diff<0||diff==0){
				return false;
			}
			
			if(diff>love){
				battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_IN);
				battleRoomMember.setRemainLove(remainLove+love);
				account.setLoveLife(0);
			}else{
				battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_IN);
				battleRoomMember.setRemainLove(limitLove);
				account.setLoveLife(love-diff);
			}
			
			accountService.update(account);
			
			
			return true;
		}
	}

	@Override
	public void submitResult(){
	
		BattleStageVo stageVo = battleDataManager.currentStage();
		BattlePaperQuestionVo battlePaperQuestion = stageVo.currentQuestion();
		List<BattleRoomMemberVo> battleRoomMembers = battleDataManager.getBattleMembers();
		List<QuestionAnswerVo> questionAnswers = battlePaperQuestion.getQuestionAnswerVos();
		Map<String,QuestionAnswerVo> questionAnswerMap = new HashMap<>();
		for(QuestionAnswerVo questionAnswer:questionAnswers){
			questionAnswerMap.put(questionAnswer.getUserId(), questionAnswer);
		}
		
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			QuestionAnswerVo questionAnswer = questionAnswerMap.get(battleRoomMember.getUserId());
			
			if(true){
				if(questionAnswer==null||
						CommonUtil.isEmpty(questionAnswer.getMyAnswer())||
						!questionAnswer.getMyAnswer().equals(battlePaperQuestion.getRightAnswer())){
					Integer remainLove = battleRoomMember.getRemainLove();
					remainLove--;
				
					battleRoomMember.setRemainLove(remainLove);
					
					BattleRoomCoolMemberVo battleRoomCoolMemberVo = battleRoomMember.getBattleRoomCoolMemberVo();
					
					if(battleRoomCoolMemberVo!=null){
						battleRoomCoolMemberVo.setLoveCount(remainLove);
					}
			
					EventManager eventManager = battleDataManager.getEventManager();
					Map<String, Object> data = new HashMap<>();
					data.put("myInfo", battleRoomMember);
					eventManager.publishEvent(Event.MY_INFO, data);
					
					if(battleRoomMember.getRemainLove()<=0){
						battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_DIE);
						battleRoomMember.setRemainLove(0);
						if(battleRoomCoolMemberVo!=null){
							battleRoomCoolMemberVo.setLoveCount(0);
						}
						
					}
					
				}else{
					Integer process = battleRoomMember.getProcess();
					process++;
					battleRoomMember.setProcess(process);
				}
			}
		}

		
	}

	@Override
	public int share(String userId) {
		BattleRoomMemberVo battleRoomMember = battleDataManager.getBattleMemberByUserId(userId);
		Integer shareNum = battleRoomMember.getShareNum();
		if(shareNum==null){
			shareNum = 0;
		}
		shareNum++;
		BattleRoomVo battleRoomVo = battleDataManager.getBattleRoom();
		List<BattleRoomShareRewardVo> battleRoomShareRewards = battleRoomVo.getBattleRoomShareRewards();
		if(battleRoomShareRewards==null){
			return 0;
		}
		for(BattleRoomShareRewardVo battleRoomShareReward:battleRoomShareRewards){
			
			if(battleRoomShareReward.getShareNum().intValue()==shareNum){
				int remainLove = battleRoomMember.getRemainLove();
				int limitLove = battleRoomMember.getLimitLove();
				int diff = limitLove-remainLove;
				int rewardLove = battleRoomShareReward.getRewardLove();
				if(diff<0){
					return 0;
				}
				EventManager eventManager = battleDataManager.getEventManager();
				if(rewardLove>diff){
					
					battleRoomMember.setShareNum(battleRoomShareReward.getShareNum());
					battleRoomMember.setRemainLove(limitLove);
					Map<String, Object> data = new HashMap<>();
					data.put("myInfo", battleRoomMember);
					eventManager.publishEvent(Event.MY_INFO, data);
					return diff;
				}else{
					battleRoomMember.setShareNum(battleRoomShareReward.getShareNum());
					battleRoomMember.setRemainLove(remainLove+rewardLove);
					Map<String, Object> data = new HashMap<>();
					data.put("myInfo", battleRoomMember);
					eventManager.publishEvent(Event.MY_INFO, data);
					return rewardLove;
				}
			}
		}
		return 0;
	}
}
