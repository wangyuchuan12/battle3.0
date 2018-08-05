package com.battle.executer.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.battle.exception.SendMessageException;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.ExecuterStore;
import com.battle.executer.exception.BattleDataManagerException;
import com.battle.executer.exception.BattleDataRoomManagerException;
import com.battle.executer.exception.BattleQuestionManagerException;
import com.battle.executer.exception.PublishException;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattleRankGoodVo;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.executer.vo.BattleUserRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.QuestionAnswerResultVo;
import com.battle.service.other.BattleRoomCoolHandle;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

public class BattleRoomPublishImp implements BattleRoomPublish{
	@Autowired
	private MessageHandler messageHandler;
	
	private BattleDataManager battleRoomDataManager;
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;
	
	final static Logger logger = LoggerFactory.getLogger(BattleRoomPublishImp.class);
	
	@Override
	public void init(ExecuterStore executerStore) {
		
		this.battleRoomDataManager = executerStore.getBattleDataManager();
		
	}
	
	@Override
	public void publishRoomEnd(){
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.PUBLISH_ROOM_END);
			List<String> userIds = new ArrayList<>();
			userIds.add(battleRoomMemberVo.getUserId());
			Map<String, Object> data = new HashMap<>();
			data.put("rank", battleRoomMemberVo.getRank());
			data.put("rewardBean", battleRoomMemberVo.getRewardBean());
			data.put("rewardLove", battleRoomMemberVo.getRewardLove());
			data.put("isPass", battleRoomMemberVo.getIsPass());
			messageVo.setType(MessageVo.USERS_TYPE);
			messageVo.setData(data);
			messageVo.setUserIds(userIds);
			publishMessage(messageVo);
		}
	}

	@Override
	public void publishRoomStart() {
		
		BattleRoomVo battleRoomVo = battleRoomDataManager.getBattleRoom();
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			
			Map<String, Object> data = new HashMap<>();
			data.put("id", battleRoomVo.getId());
			data.put("battleId", battleRoomVo.getBattleId());
			data.put("members", battleRoomVo.getMembers());
			data.put("member", battleRoomMemberVo);
			data.put("periodId", battleRoomVo.getPeriodId());
			data.put("rangeGogal", battleRoomVo.getRangeGogal());
			data.put("stageCount", battleRoomDataManager.stageCount());
			data.put("members", battleRoomMemberVos);
			List<String> userIds = new ArrayList<>();
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.PUBLISH_ROOM_START);
			userIds.add(battleRoomMemberVo.getUserId());
			messageVo.setType(MessageVo.USERS_TYPE);
			messageVo.setData(battleRoomVo);
			messageVo.setUserIds(userIds);
			publishMessage(messageVo);
			
		}	
	}

	@Override
	public void publishMemberStatus(String userId){
		BattleRoomMemberVo publishBattleRoomMemberVo = battleRoomDataManager.getBattleMemberByUserId(userId);
		
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_ROOM_START);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		
		
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(publishBattleRoomMemberVo);
		publishMessage(messageVo);
	}

	@Override
	public void publishShowSubjects(BattleStageVo battleStageVo){
		
		List<BattlePaperSubjectVo> battlePaperSubjectVos = battleStageVo.getBattlePaperSubjects();
		
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.SHOW_SUBJECTS);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			
			userIds.add(battleRoomMemberVo.getUserId());
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("subjects", battlePaperSubjectVos);
		
		data.put("selectCount", battleStageVo.getSubjectCount());
		
		data.put("timeLong", battleStageVo.getTimeLong());
		
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(data);
		messageVo.setUserIds(userIds);
		publishMessage(messageVo);
		
	}
	
	
	@Override
	public void publishShowSubjectStatus(BattlePaperSubjectVo battlePaperSubject){
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			List<String> userIds = new ArrayList<>();
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.SHOW_SUBJECT_STATUS);
			Map<String, Object> data = new HashMap<>();
			data.put("imgUrl",battlePaperSubject.getImgUrl());
			data.put("isSelect",battlePaperSubject.getIsSelect());
			data.put("name",battlePaperSubject.getName());
			data.put("selectUserImg", battlePaperSubject.getSelectUserImg());
			data.put("id",battlePaperSubject.getId());
			if(battlePaperSubject.getSelectUserId().equals(battleRoomMemberVo.getUserId())){
				data.put("isSelf",1);
			}else{
				data.put("isSelf",0);
			}
			
			userIds.add(battleRoomMemberVo.getUserId());
			
			messageVo.setType(MessageVo.USERS_TYPE);
			messageVo.setData(data);
			messageVo.setUserIds(userIds);
			publishMessage(messageVo);
		}
		
	}

	@Override
	public void publishShowQuestion(BattlePaperQuestionVo battlePaperQuestion){
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		List<String> userIds = new ArrayList<>();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			System.out.println("............battleRoomMemberVo.status:"+battleRoomMemberVo.getStatus());
			userIds.add(battleRoomMemberVo.getUserId());
		}
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_SHOW_QUESTION);
		messageVo.setData(battlePaperQuestion);
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setUserIds(userIds);
		
		publishMessage(messageVo);
	}

	@Override
	public void publishDoAnswer(QuestionAnswerResultVo questionAnswerResultVo){
		
		System.out.println(".................publishDoAnswer");
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_DO_ANSWER);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(questionAnswerResultVo);
		
		messageVo.setUserIds(userIds);
		
		publishMessage(messageVo);
		
	}

	@Override
	public void publishRest(){
		Integer stageIndex = battleRoomDataManager.getBattlePaper().getStageIndex();
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			List<String> userIds = new ArrayList<>();
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.PUBLISH_REST);
			userIds.add(battleRoomMemberVo.getUserId());
			messageVo.setType(MessageVo.USERS_TYPE);
			messageVo.setUserIds(userIds);
			
			Map<String, Object> memberInfo = new HashMap<>();
			memberInfo.put("imgUrl", battleRoomMemberVo.getImgUrl());
			memberInfo.put("limitLove", battleRoomMemberVo.getLimitLove());
			memberInfo.put("nickname", battleRoomMemberVo.getNickname());
			memberInfo.put("process", battleRoomMemberVo.getProcess());
			memberInfo.put("rangeGogal", battleRoomMemberVo.getRangeGogal());
			memberInfo.put("remainLove", battleRoomMemberVo.getRemainLove());
			memberInfo.put("userId", battleRoomMemberVo.getUserId());
			memberInfo.put("status", battleRoomMemberVo.getStatus());
			memberInfo.put("stageIndex",stageIndex);
			memberInfo.put("id", battleRoomMemberVo.getId());
			
			Map<String, Object> data = new HashMap<>();
			
			
			data.put("members", battleRoomMemberVos);
			data.put("memberInfo", memberInfo);
			messageVo.setData(data);
			publishMessage(messageVo);
			
			System.out.println("......................publishRest");
		}
	}

	@Override
	public void publishReward(BattleUserRewardVo battleReward){
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			Map<String, Object> data = new HashMap<>();
			data.put("cnRightCount", battleReward.getCnRightCount());
			data.put("id", battleReward.getId());
			data.put("imgUrl", battleReward.getImgUrl());
			data.put("nickname", battleReward.getNickname());
			data.put("rewardBean", battleReward.getRewardBean());
			data.put("status", battleReward.getStatus());
			data.put("subBean", battleReward.getSubBean());
			data.put("userId", battleReward.getUserId());
			if(battleRoomMemberVo.getUserId().equals(battleReward.getUserId())){
				data.put("isOwner", 1);
			}else{
				data.put("isOwner", 0);
			}
			List<String> userIds = new ArrayList<>();
			userIds.add(battleRoomMemberVo.getUserId());
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.PUBLISH_REWARD);
			messageVo.setData(data);
			messageVo.setType(MessageVo.USERS_TYPE);
			messageVo.setUserIds(userIds);
			publishMessage(messageVo);
		}
		
	}

	@Override
	public void publishDie(BattleRoomMemberVo battleRoomMember,Integer type){
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_DIE);
		List<String> userIds = new ArrayList<>();
		userIds.add(battleRoomMember.getUserId());
		messageVo.setType(MessageVo.USERS_TYPE);
		Map<String, Object> data = new HashMap<>();
		data.put("type", type);
		data.put("roomId", battleRoomMember.getRoomId());
		messageVo.setData(data);
		messageVo.setUserIds(userIds);
		publishMessage(messageVo);
	}
	
	private void publishMessage(MessageVo messageVo){
		
		try{
			List<String> userIds = messageVo.getUserIds();
			
			if(userIds==null||userIds.size()==0){
				return;
			}
			
			List<String> loveUserIds = new ArrayList<>();
			for(String userId:userIds){
				BattleRoomMemberVo battleRoomMemberVo =	battleRoomDataManager.getBattleMemberByUserId(userId);
				if(battleRoomMemberVo!=null&&battleRoomMemberVo.getIsOut()==0){
					loveUserIds.add(userId);
				}
			}
			
			messageVo.setUserIds(loveUserIds);
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void publishMembers(){
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMember:battleRoomMemberVos){
			List<String> userIds = new ArrayList<>();
			userIds.add(battleRoomMember.getUserId());
			
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.PUBLISH_MEMBERS);
			messageVo.setType(MessageVo.USERS_TYPE);
			
			messageVo.setUserIds(userIds);
			
			Map<String, Object> data = new HashMap<>();
			data.put("memberInfo", battleRoomMember);
			data.put("members", battleRoomMemberVos);
			messageVo.setData(data);
			publishMessage(messageVo);
		}
		
	}

	@Override
	public void publishTakepart(BattleRoomMemberVo battleRoomMember) {
		
		
	}

	@Override
	public void publishLoveCool(BattleRoomMemberVo battleRoomMember){
		
		BattleRoomCoolMemberVo battleRoomCoolMemberVo = battleRoomMember.getBattleRoomCoolMemberVo();
		
		battleRoomCoolMemberVo = battleRoomCoolHandle.filterMember(battleRoomCoolMemberVo);
		
		
		MessageVo messageVo = new MessageVo();
		
		List<String> userIds = new ArrayList<>();
		userIds.add(battleRoomMember.getUserId());
		
		messageVo.setUserIds(userIds);;
		
		messageVo.setCode(MessageVo.PUBLISH_COOL);
		
		messageVo.setData(battleRoomCoolMemberVo);;
		
		messageVo.setType(MessageVo.USERS_TYPE);
		
		
		publishMessage(messageVo);
	}

	@Override
	public void publishMyInfo(BattleRoomMemberVo battleRoomMember){
		MessageVo messageVo = new MessageVo();
		
		List<String> userIds = new ArrayList<>();
		userIds.add(battleRoomMember.getUserId());
		
		messageVo.setUserIds(userIds);;
		
		messageVo.setCode(MessageVo.PUBLISH_MY_INFO);
		
		Map<String, Object> data = new HashMap<>();
		
		BattleRoomCoolMemberVo battleRoomCoolMember = battleRoomMember.getBattleRoomCoolMemberVo();

		data.put("cool",battleRoomCoolMember);
		
		messageVo.setData(data);;
		
		messageVo.setType(MessageVo.USERS_TYPE);
		
		
		publishMessage(messageVo);
		
	}

	@Override
	public void publishGoods() {
		
		List<BattleRankGoodVo> battleRankGoods = battleRoomDataManager.getGoods();
		
		
		System.out.println("********************************battleRankGoods:"+battleRankGoods);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers(BattleRoomMemberVo.STATUS_IN,BattleRoomMemberVo.STATUS_DIE,BattleRoomMemberVo.STATUS_COMPLETE);
		for(BattleRoomMemberVo battleRoomMember:battleRoomMemberVos){
			List<String> userIds = new ArrayList<>();
			userIds.add(battleRoomMember.getUserId());
			
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.PUBLISH_GOODS);
			messageVo.setType(MessageVo.USERS_TYPE);
			
			messageVo.setUserIds(userIds);
			Map<String, Object> data = new HashMap<>();
			data.put("goods", battleRankGoods);
			messageVo.setData(data);
			
			publishMessage(messageVo);
		}
		
	}
}
