package com.battle.rank.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.domain.BattleRedpackTask;
import com.battle.domain.BattleRedpackTaskMember;
import com.battle.executer.BattleRestEvent;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;
import com.battle.service.BattleRedpackTaskMemberService;
import com.battle.service.BattleRedpackTaskService;
import com.battle.service.other.BattleRoomCoolHandle;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;
import com.wyc.common.domain.Account;
import com.wyc.common.service.AccountService;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Service
public class BattleRestEventListener  implements ApplicationListener<BattleRestEvent>{

	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private BattleRankMemberService battleRankMemberService;
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;
	
	@Autowired
	private BattleRedpackTaskService battleRedpackTaskService;
	
	@Autowired
	private BattleRedpackTaskMemberService battleRedpackTaskMemberService;
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WxUserInfoService userInfoService;
	
	@Override
	@Transactional
	public void onApplicationEvent(BattleRestEvent event) {
		
		BattleRoomVo battleRoomVo = event.getSource();
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomVo.getMembers();
		battleRoomMemberVos = new ArrayList<>(battleRoomMemberVos);
		BattleRank battleRank = null;
		if(CommonUtil.isNotEmpty(battleRoomVo.getRankId())){
			battleRank = battleRankService.findOne(battleRoomVo.getRankId());
		}
		
		List<BattleRedpackTask> battleRedpackTasks = battleRedpackTaskService.findAllByRankId(battleRank.getId());
		if(battleRank!=null){
			for(BattleRoomMemberVo battleRoomMemberVo:battleRoomMemberVos){
				
				try{
					BattleRankMember battleRankMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(),battleRoomMemberVo.getUserId());
					if(battleRankMember==null){
						battleRankMember = new BattleRankMember();
						battleRankMember.setCoverUrl(battleRoomMemberVo.getImgUrl());
						battleRankMember.setHeadImg(battleRoomMemberVo.getImgUrl());
						battleRankMember.setProcess(battleRoomMemberVo.getProcess());
						battleRankMember.setRankId(battleRank.getId());
						battleRankMember.setUserId(battleRoomMemberVo.getUserId());
						battleRankMember.setNickname(battleRoomMemberVo.getNickname());
						battleRankMember.setShareNum(battleRoomMemberVo.getShareNum());
						
						battleRankMemberService.add(battleRankMember);
					}else{
						battleRankMember.setCoverUrl(battleRoomMemberVo.getImgUrl());
						battleRankMember.setHeadImg(battleRoomMemberVo.getImgUrl());
						battleRankMember.setProcess(battleRoomMemberVo.getProcess());
						battleRankMember.setNickname(battleRoomMemberVo.getNickname());
						battleRankMember.setShareNum(battleRoomMemberVo.getShareNum());
						battleRankMemberService.update(battleRankMember);
					}
					
					BattleRoomCoolMemberVo battleRoomCoolMember = battleRoomMemberVo.getBattleRoomCoolMemberVo();
					battleRoomCoolMember.setLoveCount(battleRoomMemberVo.getRemainLove());
					battleRoomCoolMember = battleRoomCoolHandle.filterAndSaveCoolMember(battleRoomCoolMember);
					
					for(BattleRedpackTask battleRedpackTask:battleRedpackTasks){
						if(battleRankMember.getProcess()>=battleRedpackTask.getProcess()){
							
							BattleRedpackTaskMember battleRedpackTaskMember = battleRedpackTaskMemberService.findOneByTaskIdAndUserId(battleRedpackTask.getId(), battleRankMember.getUserId());
							if(battleRedpackTaskMember!=null){
								battleRedpackTaskMember.setStatus(BattleRedpackTaskMember.COMPONENT_STATUS);
								battleRedpackTaskMemberService.update(battleRedpackTaskMember);

								Map<String, Object> data = new HashMap<>();
								
								data.put("beanNum", battleRedpackTask.getBeanNum());
								data.put("redpackId", battleRedpackTask.getRedpackId());
								
								MessageVo messageVo = new MessageVo();
								messageVo.setCode(MessageVo.TASK_COMPLETE);
								messageVo.setData(data);
								List<String> userIds = new ArrayList<>();
								userIds.add(battleRedpackTaskMember.getUserId());
								messageVo.setUserIds(userIds);
								messageVo.setType(MessageVo.USERS_TYPE);
								messageHandler.sendMessage(messageVo);
								
								UserInfo userInfo = userInfoService.findOne(battleRedpackTaskMember.getUserId());
								Account account = accountService.fineOneSync(userInfo.getAccountId());
								
								account.setWisdomCount(account.getWisdomCount()+battleRedpackTask.getBeanNum());
								
								accountService.update(account);
								
							}
						}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}

				
			}
		}
	}

}
