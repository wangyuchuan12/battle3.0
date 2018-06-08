package com.battle.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.battle.domain.UserStatus;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.UserStatusService;
import com.battle.socket.WebSocketManager;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/userStatus")
public class UserStatusApi {

	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
	private WebSocketManager webSocketManager;
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object userStatus(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		UserStatus userStatus = userStatusService.findOneByUserId(userInfo.getId());
		Map<String, Object> data = new HashMap<>();
		if(webSocketManager.isOpen(userInfo.getToken())){
			int isLine = userStatus.getIsLine();
			data.put("isLine", 1);
			if(isLine==0){
				userStatus.setIsLine(1);
				userStatusService.update(userStatus);
			}
		}else{
			int isLine = userStatus.getIsLine();
			data.put("isLine", 0);
			if(isLine==1){
				userStatus.setIsLine(0);
				userStatusService.update(userStatus);
			}
		}
		
		
		System.out.println("userStatus:"+userStatus);
		
		if(userStatus!=null){
			System.out.println("userStatus.isLine:"+userStatus.getIsLine());
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		return resultVo;
		
	}
}
