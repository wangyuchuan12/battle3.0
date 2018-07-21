package com.battle.executer.roomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.BattleDataRoomManager;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomRewardRecord;
import com.battle.executer.vo.BattleRoomVo;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.wx.domain.UserInfo;

public class DefaultRoomDataManager implements BattleDataRoomManager{
	
	private BattleRoomVo battleRoom;
	
	@Autowired
	private WxUserInfoService wxUserInfoService;

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
	public void init(List<String> userIds,Integer type ,Map<String, Object> data) {
		
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
		
		
		battleRoom.setLoveCount(5);
		for(String userId:userIds){
			BattleRoomMemberVo battleRoomMemberVo = new BattleRoomMemberVo();
			UserInfo userInfo = wxUserInfoService.findOne(userId);
			battleRoomMemberVo.setImgUrl(userInfo.getHeadimgurl());
			battleRoomMemberVo.setNickname(userInfo.getNickname());
			battleRoomMemberVo.setRangeGogal(battleRoom.getRangeGogal());
			battleRoomMemberVo.setRemainLove(battleRoom.getLoveCount());
			battleRoomMemberVo.setLimitLove(battleRoom.getLoveCount());
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
		
	}

	@Override
	public BattleRoomVo getBattleRoom() {
		return battleRoom;
	}

}
