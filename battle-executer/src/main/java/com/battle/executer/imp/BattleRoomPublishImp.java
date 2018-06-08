package com.battle.executer.imp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.BattleRoomPublish;
import com.battle.executer.vo.BattlePaperQuestionVo;
import com.battle.executer.vo.BattlePaperSubjectVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.QuestionAnswerResultVo;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

public class BattleRoomPublishImp implements BattleRoomPublish{

	@Autowired
	private MessageHandler messageHandler;
	
	private BattleRoomDataManager battleRoomDataManager;
	
	final static Logger logger = LoggerFactory.getLogger(BattleRoomPublishImp.class);
	
	@Override
	public void init(BattleRoomDataManager battleRoomDataManager) {
		
		this.battleRoomDataManager = battleRoomDataManager;
	}
	
	@Override
	public void publishRoomEnd() {
		
		BattleRoomVo battleRoomVo = battleRoomDataManager.getBattleRoom();
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_ROOM_END);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(battleRoomVo);
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
		
	}

	@Override
	public void publishRoomStart() {
		BattleRoomVo battleRoomVo = battleRoomDataManager.getBattleRoom();
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_ROOM_START);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(battleRoomVo);
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
		
	}

	@Override
	public void publishMemberStatus(String userId) {
		BattleRoomMemberVo publishBattleRoomMemberVo = battleRoomDataManager.getBattleMemberByUserId(userId);
		
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_ROOM_START);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(publishBattleRoomMemberVo);
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
		
	}

	@Override
	public void publishShowSubjects(List<BattlePaperSubjectVo> battlePaperSubjectVos){
		
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.SHOW_SUBJECTS);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(battlePaperSubjectVos);
		
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
		
	}

	@Override
	public void publishShowQuestion(List<BattlePaperQuestionVo> battlePaperQuestionVos) {
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_SHOW_QUESTION);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(battlePaperQuestionVos);
		
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
		
	}

	@Override
	public void publishDoAnswer(QuestionAnswerResultVo questionAnswerResultVo) {
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_DO_ANSWER);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(questionAnswerResultVo);
		
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
		
	}

	@Override
	public void publishDoSelectSubject(BattlePaperSubjectVo battlePaperSubjectVo) {
		List<String> userIds = new ArrayList<>();
		MessageVo messageVo = new MessageVo();
		messageVo.setCode(MessageVo.PUBLISH_DO_SELECT_SUBJECT);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo.getUserId());
		}
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(battlePaperSubjectVo);
		
		try{
			messageHandler.sendMessage(messageVo);
		}catch(Exception e){
			logger.error("{}",e);
		}
	}

}
