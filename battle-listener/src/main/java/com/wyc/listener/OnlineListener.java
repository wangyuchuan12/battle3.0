package com.wyc.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.domain.UserStatus;
import com.battle.service.BattleWaitRoomMemberService;
import com.battle.socket.OnlineEvent;
import com.battle.socket.service.BattleWaitRoomSocketService;


@Service
public class OnlineListener implements ApplicationListener<OnlineEvent>{
	
	@Autowired
	private BattleWaitRoomMemberService battleWaitRoomMemberService;
	
	@Autowired
	private BattleWaitRoomSocketService battleWaitRoomSocketService;
	@Override
	public void onApplicationEvent(OnlineEvent event) {
		/*UserStatus userStatus = (UserStatus)event.getSource();
		if(userStatus!=null){
			List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByUserIdAndStatus(userStatus.getUserId(),BattleWaitRoomMember.FREE_STATUS);
			List<BattleWaitRoomMember> battleWaitRoomMembers2 = battleWaitRoomMemberService.findAllByUserIdAndStatus(userStatus.getUserId(),BattleWaitRoomMember.READY_STATUS);
			
			battleWaitRoomMembers.addAll(battleWaitRoomMembers2);
			
			for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
				battleWaitRoomMember.setStatus(BattleWaitRoomMember.FREE_STATUS);
				battleWaitRoomMemberService.update(battleWaitRoomMember);
				
				List<BattleWaitRoomMember> battleWaitRoomMembers3 = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoomMember.getRoomId());
				List<String> userIds = new ArrayList<>();
				for(BattleWaitRoomMember battleWaitRoomMember3:battleWaitRoomMembers3){
					if(!battleWaitRoomMember3.getUserId().equals(userStatus.getUserId())){
						userIds.add(battleWaitRoomMember3.getUserId());
					}
				}
				
				try {				
					battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
		
	}
}
