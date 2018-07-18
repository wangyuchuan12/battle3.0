package com.wyc.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.domain.UserStatus;
import com.battle.exception.SendMessageException;
import com.battle.executer.BattleRoomConnector;
import com.battle.service.BattleWaitRoomMemberService;
import com.battle.service.BattleWaitRoomService;
import com.battle.socket.DownEvent;
import com.battle.socket.service.BattleWaitRoomSocketService;


@Service
public class DownListener implements ApplicationListener<DownEvent>{

	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	@Autowired
	private BattleWaitRoomMemberService battleWaitRoomMemberService;
	
	@Autowired
	private BattleWaitRoomSocketService battleWaitRoomSocketService;
	
	@Autowired
	private BattleWaitRoomService battleWaitRoomService;

	
	@Override
	public void onApplicationEvent(DownEvent event) {
		
		UserStatus userStatus = (UserStatus)event.getSource();
		String roomId = userStatus.getRoomId();
		battleRoomConnector.signOut(roomId, userStatus.getUserId());

		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByUserIdAndStatus(userStatus.getUserId(),BattleWaitRoomMember.FREE_STATUS);
		List<BattleWaitRoomMember> battleWaitRoomMembers2 = battleWaitRoomMemberService.findAllByUserIdAndStatus(userStatus.getUserId(),BattleWaitRoomMember.READY_STATUS);
		
		battleWaitRoomMembers.addAll(battleWaitRoomMembers2);
		
		
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.END_STATUS);
			battleWaitRoomMemberService.update(battleWaitRoomMember);
			
			List<BattleWaitRoomMember> battleWaitRoomMembers3 = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoomMember.getRoomId());
			List<String> userIds = new ArrayList<>();
			for(BattleWaitRoomMember battleWaitRoomMember3:battleWaitRoomMembers3){
				if(!battleWaitRoomMember3.getUserId().equals(userStatus.getUserId())){
					userIds.add(battleWaitRoomMember3.getUserId());
				}
			}
			
			
			boolean flag = false;
			if(battleWaitRoomMember.getIsOwner().intValue()==1){
				BattleWaitRoomMember ownerMember = battleWaitRoomMember;
				if(battleWaitRoomMembers3.size()>1){
					for(BattleWaitRoomMember switchBattleWaitRoomMember:battleWaitRoomMembers3){
						int status = switchBattleWaitRoomMember.getStatus();
						if(switchBattleWaitRoomMember.getIsOwner().intValue()==0&&
								(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS)){
							
							try {
								battleWaitRoomSocketService.changeOwnerPublish(switchBattleWaitRoomMember);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}
						flag = true;
					}
				}else{
		
				}
			}
			
			
			
			if(!flag){
				try {				
					battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
	
				} catch (SendMessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(battleWaitRoomMember.getRoomId());
				battleWaitRoom.setStatus(BattleWaitRoom.COMPLETE_STATUS);
				battleWaitRoomService.update(battleWaitRoom);
			}
		}
	}
}
