package com.battle.executer.imp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.battle.domain.UserStatus;
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomExecuter;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.BattleRoomQuestionExecuter;
import com.battle.executer.Event;
import com.battle.executer.EventManager;
import com.battle.executer.ExecuterStore;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomRewardRecord;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.battle.service.UserStatusService;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

public class BattleRoomExecuterImp implements BattleRoomExecuter{

	private EventManager eventManager;
	
	private BattleRoomQuestionExecuter battleRoomQuestionExecuter;
	
	private BattleRoomDataManager battleRoomDataManager;
	
	private BattleRoomPublish battleRoomPublish;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	@Autowired
	private UserStatusService userStatusService;


	@Override
	public void answerQuestion(QuestionAnswerVo questionAnswer) {
		battleRoomQuestionExecuter.answerQuestion(questionAnswer);
	}

	@Override
	public void signOut(String userId) {
		
		List<BattleRoomMemberVo> battleRoomMembers = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			if(battleRoomMember.getUserId().equals(userId)){
				battleRoomMember.setStatus(BattleRoomMemberVo.STATUS_OUT);
			}
		}
		
	}

	@Override
	public void init(EventManager eventManager,ExecuterStore executerStore) {
		this.eventManager = eventManager;
		this.battleRoomDataManager = executerStore.getBattleRoomDataManager();
		this.battleRoomQuestionExecuter = executerStore.getBattleQuestionExecuter();
		this.battleRoomPublish = executerStore.getBattleRoomPublish();
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
	public void startRoom() {
		battleRoomPublish.publishRoomStart();
		eventManager.publishEvent(Event.START_ROOM_CODE, null);
		
		List<BattleRoomMemberVo> battleRoomMembers = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		
		BattleRoomVo battleRoom = battleRoomDataManager.getBattleRoom();
		
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			UserStatus userStatus = userStatusService.findOneByUserId(battleRoomMember.getUserId());
			userStatus.setRoomId(battleRoom.getId());
			userStatus.setStatus(UserStatus.IN_STATUS);
			userStatusService.update(userStatus);
		}
	}

	@Override
	public void endRoom() {
		
		BattleRoomVo battleroom = battleRoomDataManager.getBattleRoom();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus transactionStatus = platformTransactionManager.getTransaction(def);

		List<BattleRoomMemberVo> battleRoomMembers = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		
		
		List<BattleRoomRewardRecord> battleRoomRewardRecords = battleRoomDataManager.getBattleRoom().getBattleRoomRewardRecords();
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
	public synchronized void subjectSelect(String subjectId, String userId) {
		BattlePaperVo battlePaperVo = battleRoomDataManager.getBattlePaper();
		
		BattleRoomVo battleRoomVo = battleRoomDataManager.getBattleRoom();
		
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
		
		if(battlePaperSubject.getIsSelect()==1){
			return;
		}
		
		battlePaperSubject.setIsSelect(1);
		
		battlePaperSubject.setSelectUserId(battleRoomMember.getUserId());
		
		battlePaperSubject.setSelectUserImg(battleRoomMember.getImgUrl());
		
		battleRoomPublish.publishShowSubjectStatus(battlePaperSubject);
	}

	@Override
	public void submitResult() {
		BattleStageVo stageVo = battleRoomDataManager.getBattlePaper().currentStage();
		List<BattlePaperQuestionVo> battlePaperQuestions = stageVo.getSelectBattlePaperQuestions();
		

		List<BattleRoomMemberVo> battleRoomMembers = battleRoomDataManager.getBattleMembers();
	

		for(BattlePaperQuestionVo battlePaperQuestion:battlePaperQuestions){
			List<QuestionAnswerVo> questionAnswers = battlePaperQuestion.getQuestionAnswerVos();
			Map<String,QuestionAnswerVo> questionAnswerMap = new HashMap<>();
			for(QuestionAnswerVo questionAnswer:questionAnswers){
				questionAnswerMap.put(questionAnswer.getUserId(), questionAnswer);
			}
			
			for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
				QuestionAnswerVo questionAnswer = questionAnswerMap.get(battleRoomMember.getUserId());
				if(battleRoomMember.getStatus().intValue()==BattleRoomMemberVo.STATUS_IN.intValue()){
					if(questionAnswer==null||
							CommonUtil.isEmpty(questionAnswer.getMyAnswer())||
							!questionAnswer.getMyAnswer().equals(battlePaperQuestion.getRightAnswer())){
						Integer remainLove = battleRoomMember.getRemainLove();
						remainLove--;
						if(remainLove<=0){
							remainLove = 0;
							
							battleRoomPublish.publishDie(battleRoomMember);
						}
						battleRoomMember.setRemainLove(remainLove);
						
						
					}else{
						Integer process = battleRoomMember.getProcess();
						process++;
						battleRoomMember.setProcess(process);
					}
				}
			}
			
		}
		
		battleRoomPublish.publishRest();
		
	}

	@Override
	public void members() {
		battleRoomPublish.publishMembers();
		
	}

	@Override
	public BattleRoomMemberVo takepart(UserInfo userInfo) {
		
		List<BattleRoomMemberVo> battleRoomMembers =  battleRoomDataManager.getBattleMembers();
		BattleRoomVo battleRoom = battleRoomDataManager.getBattleRoom();
		BattleRoomMemberVo battleRoomMemberVo = null;
		for(BattleRoomMemberVo thisMember:battleRoomMembers){
			if(thisMember.getUserId().equals(userInfo.getId())){
				battleRoomMemberVo = thisMember;
			}
		}
		if(battleRoomMemberVo==null){
			battleRoomMemberVo = new BattleRoomMemberVo();
			battleRoomMemberVo.setAccountId(userInfo.getAccountId());
			battleRoomMemberVo.setBattleId(battleRoom.getBattleId());
			battleRoomMemberVo.setCnRightCount(0);
			battleRoomMemberVo.setEndCotent("");
			battleRoomMemberVo.setId(UUID.randomUUID().toString());
			battleRoomMemberVo.setImgUrl(userInfo.getHeadimgurl());
			battleRoomMemberVo.setIsEnd(0);
			battleRoomMemberVo.setIsPass(0);
			battleRoomMemberVo.setRemainLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setLimitLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setNickname(userInfo.getNickname());
			battleRoomMemberVo.setPeriodId(battleRoom.getPeriodId());
			battleRoomMemberVo.setProcess(0);
			battleRoomMemberVo.setRangeGogal(battleRoom.getRangeGogal());
			battleRoomMemberVo.setRank(100);
			battleRoomMemberVo.setRewardBean(0);
			battleRoomMemberVo.setRewardLove(0);
			battleRoomMemberVo.setRoomId(battleRoom.getId());
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
			battleRoomMemberVo.setUserId(userInfo.getId());
			battleRoomMembers.add(battleRoomMemberVo);
		}else{
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
		}
		
		battleRoomPublish.publishTakepart(battleRoomMemberVo);
		return battleRoomMemberVo;
	}

	@Override
	public BattleRoomVo getRoom() {
		
		BattleRoomVo battleRoom = battleRoomDataManager.getBattleRoom();
		return battleRoom;
	}
}
