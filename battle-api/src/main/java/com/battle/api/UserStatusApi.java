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
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;
import com.wyc.handle.UserStatusCheckHandle;

@Controller
@RequestMapping(value="/api/userStatus")
public class UserStatusApi {

	@Autowired
	private UserStatusCheckHandle userStatusCheckHandle;
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object userStatus(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		UserStatus userStatus = userStatusCheckHandle.userStatus(userInfo);
		
		Map<String, Object> data = new HashMap<>();
		data.put("isLine", userStatus.getIsLine());
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		return resultVo;
		
	}
}