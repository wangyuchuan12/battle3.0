package com.battle.room.executer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.room.vo.BattleWaitRoomMemberVo;
import com.battle.room.vo.BattleWaitRoomVo;
import com.battle.socket.WebSocketManager;

public class BattleWaitRoomDataManager {
	
	private BattleWaitRoomVo battleWaitRoom;
	
	@Autowired
	private WebSocketManager webSocketManager;

	public BattleWaitRoomDataManager(BattleWaitRoomVo battleWaitRoom){
		this.battleWaitRoom = battleWaitRoom;
	}
	
	
	public BattleWaitRoomVo getBattleWaitRoom(){
		return battleWaitRoom;
	}
	
	public List<BattleWaitRoomMemberVo> checkOut(){
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = getMembers();
		List<BattleWaitRoomMemberVo> outMembers = new ArrayList<>();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMemberVo.FREE_STATUS||
					battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMemberVo.READY_STATUS){
				if(!webSocketManager.isOpen(battleWaitRoomMember.getToken())){
					battleWaitRoomMember.setStatus(BattleWaitRoomMemberVo.END_STATUS);
					outMembers.add(battleWaitRoomMember);
				}
			}
		}
		
		return outMembers;
	}
	
	
	public BattleWaitRoomMemberVo findMemberByUserId(String userId){
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoom.getBattleWaitRoomMembers();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getUserId().equals(userId)){
				return battleWaitRoomMember;
			}
		}
		
		return null;
		
	}
	
	public synchronized List<BattleWaitRoomMemberVo>  getMembers(){
		return battleWaitRoom.getBattleWaitRoomMembers();
	}


	public void addMember(BattleWaitRoomMemberVo battleWaitRoomMember) {
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = getMembers();
		battleWaitRoomMembers.add(battleWaitRoomMember);
	}


	public BattleWaitRoomMemberVo getOwnerMember() {
		
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = getMembers();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getIsOwner().intValue()==1){
				return battleWaitRoomMember;
			}
		}
		return null;
	}
	
	public List<BattleWaitRoomMemberVo> findAllByStatues(int... statues){
		
		List<BattleWaitRoomMemberVo> returnMembers = new ArrayList<>();
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = getMembers();
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			for(int status:statues){
				if(battleWaitRoomMember.getStatus().intValue()==status){
					returnMembers.add(battleWaitRoomMember);
				}
			}
		}
		
		return returnMembers;
	}


	public BattleWaitRoomMemberVo findMemberByMemberId(String memberId) {
		
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoom.getBattleWaitRoomMembers();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getId().equals(memberId)){
				return battleWaitRoomMember;
			}
		}
		
		return null;
	}
	
}
