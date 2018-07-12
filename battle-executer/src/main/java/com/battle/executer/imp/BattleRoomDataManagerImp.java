package com.battle.executer.imp;
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
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.EventManager;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattlePaperVo;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomRewardRecord;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.service.BattleQuestionDistributionQuestionService;
import com.battle.service.BattleQuestionDistributionService;
import com.battle.service.BattleQuestionDistributionStageService;
import com.battle.service.BattleQuestionDistributionSubjectService;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.wx.domain.UserInfo;
public class BattleRoomDataManagerImp implements BattleRoomDataManager{

	
	private BattleRoomVo battleRoom;
	
	private BattlePaperVo battlePaper;
	
	@Autowired
	private BattleQuestionDistributionService battleQuestionDistributionService;
	
	@Autowired
	private WxUserInfoService wxUserInfoService;
	
	private EventManager eventManager;
	
	@Autowired
	private BattleQuestionDistributionStageService battleQuestionDistributionStageService;
	
	@Autowired
	private BattleQuestionDistributionSubjectService battleQuestionDistributionSubjectService;
	
	@Autowired
	private BattleQuestionDistributionQuestionService battleQuestionDistributionQuestionService;
	
	

	@Override
	public BattleRoomVo getBattleRoom() {
		
		return battleRoom;
	}

	@Override
	public BattlePaperVo getBattlePaper() {
		return battlePaper;
	}

	@Override
	public List<BattleRoomMemberVo> getBattleMembers() {
		return battleRoom.getMembers();
	}


	@Override
	public BattleRoomMemberVo getBattleMemberByUserId(String userId) {
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoom.getMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			if(battleRoomMemberVo.getUserId().equals(userId)){
				return battleRoomMemberVo;
			}
		}
		return null;
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
	public void init(String groupId, List<String> userIds,Integer type ,Map<String, Object> data) {
		
		eventManager = new EventManager();
		
		battleRoom = new BattleRoomVo();
		
		battleRoom.setNum(userIds.size());
		
		battleRoom.setRangeGogal(1000);
		
		List<BattleRewardVo> battleRewards = null;
		if(data!=null&&data.get("rewards")!=null){
			battleRewards = (List<BattleRewardVo>)data.get("rewards");
		}
		List<BattleRoomRewardRecord> battleRoomRewardRecords = new ArrayList<>();
		
		if(battleRewards!=null){
			for(BattleRewardVo battleReward:battleRewards){
				
				BattleRoomRewardRecord battleRoomRewardRecord = new BattleRoomRewardRecord();
				battleRoomRewardRecord.setId(UUID.randomUUID().toString());
				battleRoomRewardRecord.setRank(battleReward.getRank());
				battleRoomRewardRecord.setRewardBean(battleReward.getRewardBean());
				battleRoomRewardRecord.setRewardLove(battleReward.getRewardLove());
				
				battleRoomRewardRecords.add(battleRoomRewardRecord);
				
			}
		}
		
		battleRoom.setRewardBean(10);
		battleRoom.setRewardBean2(20);
		battleRoom.setRewardBean3(30);
		battleRoom.setRewardBean4(40);
		battleRoom.setRewardBean5(50);
		battleRoom.setRewardBean6(60);
		battleRoom.setRewardBean7(70);
		battleRoom.setRewardBean8(80);
		battleRoom.setRewardBean9(90);
		battleRoom.setRewardBean10(100);
		
		battleRoom.setData(data);
		
		battleRoom.setSubBean(50);
		
		battleRoom.setType(type);
		
		battleRoom.setId(UUID.randomUUID().toString());
		
		
		battleRoom.setBattleRoomRewardRecords(battleRoomRewardRecords);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = new ArrayList<>();
		
		
		List<BattleQuestionDistribution> battleQuestionDistributions = battleQuestionDistributionService.findAllByGroupId(groupId);
		
		System.out.println("........battleQuestionDistributions:"+battleQuestionDistributions);
		BattleQuestionDistribution battleQuestionDistribution;
		
		if(battleQuestionDistributions.size()>0){
			Collections.shuffle(battleQuestionDistributions);
			battleQuestionDistribution = battleQuestionDistributions.get(0);
		}else{
			throw new RuntimeException("battleQuestionDistribution 为空");
		}
		
		for(String userId:userIds){
			BattleRoomMemberVo battleRoomMemberVo = new BattleRoomMemberVo();
			UserInfo userInfo = wxUserInfoService.findOne(userId);
			battleRoomMemberVo.setImgUrl(userInfo.getHeadimgurl());
			battleRoomMemberVo.setLimitLove(battleQuestionDistribution.getLoveCount());
			battleRoomMemberVo.setNickname(userInfo.getNickname());
			battleRoomMemberVo.setRangeGogal(battleRoom.getRangeGogal());
			battleRoomMemberVo.setRemainLove(battleQuestionDistribution.getLoveCount());
			battleRoomMemberVo.setRoomId(battleRoom.getId());
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
			battleRoomMemberVo.setUserId(userId);
			battleRoomMemberVo.setProcess(0);
			battleRoomMemberVo.setId(UUID.randomUUID().toString());
			battleRoomMemberVo.setCnRightCount(0);
			battleRoomMemberVo.setIsPass(0);
			battleRoomMemberVo.setAccountId(userInfo.getAccountId());
			battleRoomMemberVo.setIsEnd(0);
			battleRoomMemberVos.add(battleRoomMemberVo);
		}
		
		
		battleRoom.setMembers(battleRoomMemberVos);

		
		System.out.println("........groupId:"+groupId);
		
		
		battlePaper = new BattlePaperVo();
		battlePaper.setId(UUID.randomUUID().toString());
		battlePaper.setStageIndex(1);
		
		List<BattleStageVo> battleStageVos = new ArrayList<>();
		
		
		battlePaper.setBattleStages(battleStageVos);
		
		List<BattleQuestionDistributionStage> battleQuestionDistributionStages = battleQuestionDistributionStageService.findAllByDistributionIdOrderByStageIndexAsc(battleQuestionDistribution.getId());
		
		
		battleRoom.setStageCount(battleQuestionDistributionStages.size());
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
				battlePaperQuestionVo.setRoomId(battleRoom.getId());
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
	public EventManager getEventManager() {
		
		return eventManager;
	}

	@Override
	public List<BattleRoomMemberVo> getBattleMembers(Integer... statuses) {
		List<BattleRoomMemberVo> battleRoomMembers = getBattleMembers();
		List<BattleRoomMemberVo> validMembers = new ArrayList<>();
		
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			for(Integer status:statuses){
				if(battleRoomMember.getStatus().intValue()==status.intValue()){
					validMembers.add(battleRoomMember);
					break;
				}
			}
		}
		return validMembers;
	}
}
