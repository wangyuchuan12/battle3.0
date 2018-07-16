package com.battle.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.battle.domain.UserStatus;
import com.battle.executer.BattleRoomConnector;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.UserStatusService;
import com.battle.socket.WebSocketManager;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/userStatus")
public class UserStatusApi {

	@Autowired
	private WebSocketManager webSocketManager;
	
	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	@Autowired
	private UserStatusService userStatusService;
	
	final static Logger logger = LoggerFactory.getLogger(UserStatusApi.class);
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object userStatus(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		boolean isLine = webSocketManager.isOpen(userInfo.getToken());
		
		UserStatus userStatus = webSocketManager.getUserStatus(userInfo.getToken());
		
		boolean isProgress = false;
		
		
		Map<String, Object> data = new HashMap<>();
		data.put("isLine",isLine);
		
		logger.debug("isLine:{}",isLine);
		
		System.out.println(".....userStatuszzz:"+userStatus);
		
		if(userStatus!=null){
			
			
		}else{
			
			userStatus = userStatusService.findOne(userInfo.getStatusId()); 
			logger.debug("useStatus is null");
		}
		
		
		if(CommonUtil.isNotEmpty(userStatus.getRoomId())){
			isProgress = battleRoomConnector.isInProgress(userStatus.getRoomId());
			if(isProgress){
				data.put("roomId", userStatus.getRoomId());
			}
		}
		
		
		data.put("isProgress", isProgress);
	
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		return resultVo;
		
	}
}
