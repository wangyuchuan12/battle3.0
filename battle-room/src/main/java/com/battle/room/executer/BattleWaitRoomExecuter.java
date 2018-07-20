package com.battle.room.executer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.battle.domain.BattleSearchRoomReward;
import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.domain.UserStatus;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.room.exception.BattleWaitRoomStartException;
import com.battle.room.vo.BattleWaitRoomMemberVo;
import com.battle.room.vo.BattleWaitRoomVo;
import com.battle.service.BattleSearchRoomRewardService;
import com.battle.service.UserStatusService;
import com.battle.socket.WebSocketManager;
import com.battle.socket.service.UserStatusManager;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

public class BattleWaitRoomExecuter {
	private BattleWaitRoomDataManager battleWaitRoomDataManager;
	private BattleWaitRoomPublish battleWaitRoomPublish;
	@Autowired
	private WebSocketManager webSocketManager;
	
	@Autowired
	private UserStatusManager userStatusManager;
	
	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
	private BattleSearchRoomRewardService battleSearchRoomRewardService;
	
	public boolean checkRoom(){
		
		BattleWaitRoomVo battleWaitRoomVo = battleWaitRoomDataManager.getBattleWaitRoom();
		
		if(battleWaitRoomVo.getStatus().intValue()!=BattleWaitRoomVo.FREE_STATUS){
			return false;
		}
		
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoomDataManager.checkOut();
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			out(battleWaitRoomMember.getUserId());
		}
		
		battleWaitRoomMembers = battleWaitRoomDataManager.getMembers();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMemberVo.FREE_STATUS||battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMemberVo.READY_STATUS){
				return true;
			}
		}
		
		
		
		return false;
	}
	
	
	public void checkOwner(){
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoomDataManager.getMembers();
		BattleWaitRoomMemberVo ownerMember = null;
		BattleWaitRoomMemberVo member = null;
		
		
		for(BattleWaitRoomMemberVo battleWaitRoomMemberVo:battleWaitRoomMembers){
			if(battleWaitRoomMemberVo.getIsOwner().intValue()==1){
				ownerMember = battleWaitRoomMemberVo;
			}else{
				if(webSocketManager.isOpen(battleWaitRoomMemberVo.getToken())){
					member = battleWaitRoomMemberVo;
				}
			}
		}
		
		if(ownerMember==null||!webSocketManager.isOpen(ownerMember.getToken())){
			if(member!=null){
				battleWaitRoomPublish.changeOwnerPublish(member.getUserId());
				if(ownerMember!=null){
					ownerMember.setStatus(BattleWaitRoomMemberVo.END_STATUS);
				}
			}
		}else{
			long ownTime = ownerMember.getOwnerTime().toDate().getTime();
			long nowTime = new Date().getTime();
			long diffSechod = (nowTime-ownTime)/1000;
			if(diffSechod>30){
				if(member!=null){
					battleWaitRoomPublish.changeOwnerPublish(member.getUserId());
				}
			}
		}
	}
	
	public BattleWaitRoomExecuter(BattleWaitRoomDataManager battleWaitRoomDataManager,BattleWaitRoomPublish battleWaitRoomPublish){
		this.battleWaitRoomDataManager = battleWaitRoomDataManager;
		this.battleWaitRoomPublish = battleWaitRoomPublish;
	}
	
	public BattleWaitRoomPublish getBattleWaitRoomPublish(){
		return this.battleWaitRoomPublish;
	}

	public BattleWaitRoomDataManager getBattleWaitRoomDataManager(){
		return this.battleWaitRoomDataManager;
	}
	
	public void out(String userId){
		
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.OUT_STATUS);
		
		
		boolean flag = false;
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoomDataManager.getMembers();
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMemberVo battleWaitRoomMember2:battleWaitRoomMembers){
			if(battleWaitRoomMember2.getStatus().intValue()==BattleWaitRoomMember.FREE_STATUS||battleWaitRoomMember2.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS){
				if(webSocketManager.isOpen(battleWaitRoomMember2.getToken())){
					flag = true;
				}
			}
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		if(!flag){
			BattleWaitRoomVo battleWaitRoom = battleWaitRoomDataManager.getBattleWaitRoom();
			battleWaitRoom.setStatus(BattleWaitRoom.COMPLETE_STATUS);
		}else if(battleWaitRoomMember.getIsOwner().intValue()==1){
			if(battleWaitRoomMembers.size()>1){
				for(BattleWaitRoomMemberVo switchBattleWaitRoomMember:battleWaitRoomMembers){
					int status = switchBattleWaitRoomMember.getStatus();
					if(switchBattleWaitRoomMember.getIsOwner().intValue()==0&&
							(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS)){
						
						try {
	
							battleWaitRoomPublish.changeOwnerPublish(switchBattleWaitRoomMember.getUserId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
				}
			}else{
	
			}
		}
		
		battleWaitRoomPublish.waitRoomMemberPublish(battleWaitRoomMember.getUserId());
		

	}
	
	public void into(UserInfo userInfo){

		BattleWaitRoomVo battleWaitRoom = battleWaitRoomDataManager.getBattleWaitRoom();
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userInfo.getId());
		Integer num = battleWaitRoom.getNum();
		
		System.out.println("battleWaitRoomMember:"+battleWaitRoomMember);
		if(battleWaitRoomMember==null){
			battleWaitRoomMember = new BattleWaitRoomMemberVo();
			battleWaitRoomMember.setImgUrl(userInfo.getHeadimgurl());
			battleWaitRoomMember.setNickname(userInfo.getNickname());
			battleWaitRoomMember.setRoomId(battleWaitRoom.getId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMember.setUserId(userInfo.getId());
			battleWaitRoomMember.setIsOwner(0);
			battleWaitRoomMember.setToken(userInfo.getToken());
			battleWaitRoomMember.setIsEnd(0);
			battleWaitRoomMember.setId(UUID.randomUUID().toString());
			battleWaitRoomDataManager.addMember(battleWaitRoomMember);
		}else{
			/*if(battleWaitRoomMember.getIsEnd()!=null&&battleWaitRoomMember.getIsEnd().intValue()==1){
				battleWaitRoomPublish.kickOutPublish(battleWaitRoomMember.getUserId());

			}*/
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
		}
		
		num++;
		battleWaitRoom.setNum(num);
		
		if(num>=battleWaitRoom.getMaxNum()){
			battleWaitRoom.setIsFull(1);
		}
		
		
		battleWaitRoomPublish.waitRoomMemberPublish(battleWaitRoomMember.getUserId());
	}
	
	public void start() throws BattleWaitRoomStartException{
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoomDataManager.getMembers();
		
		BattleWaitRoomVo battleWaitRoom = battleWaitRoomDataManager.getBattleWaitRoom();
		
		if(battleWaitRoom.getStatus().intValue()!=BattleWaitRoom.FREE_STATUS){
			/*ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;*/
			
			throw new BattleWaitRoomStartException(BattleWaitRoomStartException.ROOM_STATUS_ERROR_TYPE);
		}
		
		List<String> userIds = new ArrayList<>();

		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			int status = battleWaitRoomMember.getStatus().intValue();
			if(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.END_STATUS){
				/*ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				resultVo.setErrorCode(0);
				return resultVo;*/
				
				throw new BattleWaitRoomStartException(BattleWaitRoomStartException.MEMBER_STATUS_ERROR_TYPE);
			}
		}
		
		List<BattleWaitRoomMemberVo> downLineMembers = new ArrayList<>();
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			battleWaitRoomMember.setIsEnd(1);
			battleWaitRoomMember.setEndContent("比赛已经开始");
			UserStatus userStatus = userStatusManager.getUserStatus(battleWaitRoomMember.getToken());
			if(userStatus!=null&&webSocketManager.isOpen(battleWaitRoomMember.getToken())&&battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS){
				userStatus.setRoomId(battleWaitRoom.getId());
				userStatus.setStatus(UserStatus.IN_STATUS);
				userStatusService.update(userStatus);
				userIds.add(battleWaitRoomMember.getUserId());
			}else{
				int status = battleWaitRoomMember.getStatus();
				if(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS){
					downLineMembers.add(battleWaitRoomMember);
				}
			}
		}
		
		if(downLineMembers.size()>0){
			for(BattleWaitRoomMemberVo downLineMember:downLineMembers){
				downLineMember.setStatus(BattleWaitRoomMember.OUT_STATUS);
				battleWaitRoomPublish.waitRoomMemberPublish(downLineMember.getUserId());
			}
			/*ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorCode(0);
			return resultVo;*/
			
			throw new BattleWaitRoomStartException(BattleWaitRoomStartException.MEMBER_STATUS_ERROR_TYPE);
		}
		
		
		int num = 0;
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS){
				num++;
			}
		}
		if(num<battleWaitRoom.getMinNum()){
			/*ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorCode(1);
			return resultVo;*/
			
			throw new BattleWaitRoomStartException(BattleWaitRoomStartException.ROOM_NUM_ERROR_TYPE);
		}
		
		
		battleWaitRoom.setStatus(BattleWaitRoom.IN_STATUS);
		
		List<BattleSearchRoomReward> battleSearchRoomRewards = battleSearchRoomRewardService.findAllBySearchKey(battleWaitRoom.getSearchKey());
		List<BattleRewardVo> battleRewards = new ArrayList<>();
		for(BattleSearchRoomReward battleSearchRoomReward:battleSearchRoomRewards){
			BattleRewardVo battleReward = new BattleRewardVo();
			battleReward.setRank(battleSearchRoomReward.getRank());
			battleReward.setRewardBean(battleSearchRoomReward.getRewardBean());
			battleReward.setRewardLove(battleSearchRoomReward.getRewardLove());
			battleRewards.add(battleReward);
		}
		
		/*
		Map<String, Object> data = new HashMap<>();
		data.put("rewards", battleRewards);
		battleRoomFactory.init(battleWaitRoom.getGroupId(),userIds,BattleRoomVo.ROOM_TYPE,data);
		
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.END_STATUS);
			battleWaitRoomMember.setEndContent("比赛已经开始");
			battleWaitRoomMemberService.update(battleWaitRoomMember);
		}
		*/
	}
	
	public BattleWaitRoomMemberVo cancel(String userId){
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.FREE_STATUS);
		
		
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoomDataManager.getMembers();
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMemberVo battleWaitRoomMember2:battleWaitRoomMembers){
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		battleWaitRoomPublish.waitRoomMemberPublish(battleWaitRoomMember.getUserId());
		
		return battleWaitRoomMember;

	}
	
	public BattleWaitRoomMemberVo ready(String userId){

		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);

		battleWaitRoomPublish.waitRoomMemberPublish(battleWaitRoomMember.getUserId());
		
		return battleWaitRoomMember;
		
	}
	
	public BattleWaitRoomMemberVo ownerChange(String userId){
		BattleWaitRoomMemberVo ownerMember = battleWaitRoomDataManager.getOwnerMember();
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
		ownerMember.setIsOwner(0);
		battleWaitRoomMember.setIsOwner(1);
		battleWaitRoomMember.setOwnerTime(new DateTime());
		battleWaitRoomPublish.waitRoomMemberPublish(ownerMember.getUserId());
		battleWaitRoomPublish.waitRoomMemberPublish(battleWaitRoomMember.getUserId());
		return battleWaitRoomMember;
	}
	
	public void kickOut(String memberId){
		
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByMemberId(memberId);
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.OUT_STATUS);
		battleWaitRoomMember.setIsEnd(1);
		battleWaitRoomMember.setEndContent("你已经被踢出房间");
		

		battleWaitRoomPublish.kickOutPublish(battleWaitRoomMember.getUserId());
		
		battleWaitRoomPublish.waitRoomMemberPublish(battleWaitRoomMember.getUserId());
	}
	
}
