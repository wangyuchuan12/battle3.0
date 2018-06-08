package com.battle.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.battle.domain.UserStatus;
import com.battle.service.UserStatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.wx.domain.UserInfo;

public class MessageSender {
	
@Autowired
private SocketHandler socketHandler;

@Autowired
private UserStatusService userStatusService;

@Autowired
private WxUserInfoService wxUserInfoService;
	
public synchronized void sendMessage(MessageVo messageVo) throws IOException{
	
		List<String> tokens = new ArrayList<>();
		
		List<String> excludeUserIds = messageVo.getExcludeUserIds();
		
		if(messageVo.getType()==MessageVo.USERS_TYPE){
			List<String> userIds = messageVo.getUserIds();
			for(String userId:userIds){
				UserStatus userStatus = userStatusService.findOneByUserId(userId);
				if(userStatus==null){
					
					UserInfo userInfo = wxUserInfoService.findOne(userId);
					userStatus = new UserStatus();
					userStatus.setIsLine(0);
					userStatus.setUserId(userId);
					userStatus.setToken(userInfo.getToken());
					/*userStatusService.add(userStatus);
		
					userInfo.setStatusId(userStatus.getId());
					wxUserInfoService.update(userInfo);*/
				}
				if(userStatus.getIsLine()==null){
					userStatus.setIsLine(0);
					
				}
				if(userStatus.getIsLine()==1){
					if(excludeUserIds==null||excludeUserIds.size()==0){
						tokens.add(userStatus.getToken());
					}else{
						if(!excludeUserIds.contains(userStatus.getUserId())){
							tokens.add(userStatus.getToken());
						}
					}
				}
			}
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String value = objectMapper.writeValueAsString(messageVo);
		
		if(tokens.size()>0){
			socketHandler.sendMessage(tokens, Arrays.asList(value));
		}
		
	}
}
