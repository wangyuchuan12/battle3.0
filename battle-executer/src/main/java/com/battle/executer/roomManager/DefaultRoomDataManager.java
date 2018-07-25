package com.battle.executer.roomManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleDataRoomManager;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomRewardRecord;
import com.battle.executer.vo.BattleRoomShareRewardVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleShareRewardVo;
import com.battle.socket.WebSocketManager;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.wx.domain.UserInfo;

public class DefaultRoomDataManager implements BattleDataRoomManager{
	
	private BattleRoomVo battleRoom;
	
	@Autowired
	private WxUserInfoService wxUserInfoService;
	
	@Autowired
	private WebSocketManager webSocketManager;

	@Override
	public List<BattleRoomMemberVo> getBattleMembers() {
		return battleRoom.getMembers();
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
	public void init(List<UserParam> userParams,Integer type ,Map<String, Object> data) {
		
		Integer subBean = (Integer)data.get("subBean");
		if(subBean==null){
			subBean = 0;
		}
		battleRoom = new BattleRoomVo();
		
		battleRoom.setNum(userParams.size());
		
		battleRoom.setRangeGogal(1000);
		
		List<BattleRewardVo> battleRewards = null;
		if(data!=null&&data.get("rewards")!=null){
			battleRewards = (List<BattleRewardVo>)data.get("rewards");
		}
		
		List<BattleShareRewardVo> battleShareRewardVos = null;
		if(data!=null&&data.get("shareRewards")!=null){
			battleShareRewardVos = (List<BattleShareRewardVo>) data.get("shareRewards");
		}
		
		List<BattleRoomShareRewardVo> battleRoomShareRewards = new ArrayList<>();
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
		
		if(battleShareRewardVos!=null){
			for(BattleShareRewardVo battleShareRewardVo:battleShareRewardVos){
				BattleRoomShareRewardVo battleRoomShareRewardVo = new BattleRoomShareRewardVo();
				battleRoomShareRewardVo.setId(UUID.randomUUID().toString());
				battleRoomShareRewardVo.setRewardLove(battleShareRewardVo.getRewardLove());
				battleRoomShareRewardVo.setShareNum(battleShareRewardVo.getShareNum());
				battleRoomShareRewards.add(battleRoomShareRewardVo);
			}
			
		}
		
		battleRoom.setRewardBean(5);
		battleRoom.setRewardBean2(15);
		battleRoom.setRewardBean3(20);
		battleRoom.setRewardBean4(25);
		battleRoom.setRewardBean5(30);
		battleRoom.setRewardBean6(35);
		battleRoom.setRewardBean7(40);
		battleRoom.setRewardBean8(45);
		battleRoom.setRewardBean9(50);
		battleRoom.setRewardBean10(60);
		
		battleRoom.setData(data);
		
		battleRoom.setSubBean(subBean);
		
		battleRoom.setType(type);
		
		battleRoom.setId(UUID.randomUUID().toString());
		
		battleRoom.setBattleRoomShareRewards(battleRoomShareRewards);
		
		battleRoom.setBattleRoomRewardRecords(battleRoomRewardRecords);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = new ArrayList<>();
		
		
		battleRoom.setLoveCount(5);
		for(UserParam userParam:userParams){
			BattleRoomMemberVo battleRoomMemberVo = new BattleRoomMemberVo();
			UserInfo userInfo = wxUserInfoService.findOne(userParam.getUserId());
			battleRoomMemberVo.setImgUrl(userInfo.getHeadimgurl());
			battleRoomMemberVo.setNickname(userInfo.getNickname());
			battleRoomMemberVo.setRangeGogal(battleRoom.getRangeGogal());
			battleRoomMemberVo.setRemainLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setLimitLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setRoomId(battleRoom.getId());
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_IN);
			battleRoomMemberVo.setUserId(userParam.getUserId());
			battleRoomMemberVo.setProcess(0);
			battleRoomMemberVo.setId(UUID.randomUUID().toString());
			battleRoomMemberVo.setCnRightCount(0);
			battleRoomMemberVo.setIsPass(0);
			battleRoomMemberVo.setAccountId(userInfo.getAccountId());
			battleRoomMemberVo.setIsEnd(0);
			battleRoomMemberVo.setToken(userInfo.getToken());
			battleRoomMemberVo.setPreClear(0);
			battleRoomMemberVo.setIsOut(0);
			battleRoomMemberVo.setShareNum(0);
			battleRoomMemberVos.add(battleRoomMemberVo);
		}
		
		
		battleRoom.setMembers(battleRoomMemberVos);
		
	}

	@Override
	public BattleRoomVo getBattleRoom() {
		return battleRoom;
	}

	@Override
	public void clear() {
		List<BattleRoomMemberVo> battleRoomMembers = getBattleMembers();
		
		Iterator<BattleRoomMemberVo> it = battleRoomMembers.iterator();
		
		while(it.hasNext()){
			System.out.println("..........新的模式");
			BattleRoomMemberVo battleRoomMemberVo = it.next();
			if(!webSocketManager.isOpen(battleRoomMemberVo.getToken())){
				battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_OUT);
				battleRoomMemberVo.setIsOut(1);
			}
			int status = battleRoomMemberVo.getStatus();
			if(battleRoomMemberVo.getPreClear()==1){
				it.remove();
			}
			
			if(battleRoomMemberVo.getIsOut()==1){
				battleRoomMemberVo.setPreClear(1);
			}
		}
		
	}

}
