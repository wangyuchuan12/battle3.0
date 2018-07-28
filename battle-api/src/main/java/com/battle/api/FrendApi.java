package com.battle.api;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.UserFriend;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.UserFrendService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;


@Controller
@RequestMapping(value="/api/battle/frend")
public class FrendApi {

	@Autowired
	private UserFrendService userFrendService;
	
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@RequestMapping(value="registerFrend")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo registerFrend(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);

		/*UserFriend mySelfFriend = userFrendService.findOneByUserIdAndFriendUserId(userInfo.getId(), userInfo.getId());
		
		if(mySelfFriend==null){
			mySelfFriend = new UserFriend();
			mySelfFriend.setFriendUserId(userInfo.getId());
			mySelfFriend.setUserId(userInfo.getId());
			mySelfFriend.setUserName(userInfo.getNickname());
			mySelfFriend.setUserImg(userInfo.getHeadimgurl());
			mySelfFriend.setMeetTime(new DateTime());
			userFrendService.add(mySelfFriend);
		}*/
		
	
		String recommendUserId = httpServletRequest.getParameter("recommendUserId");

		if(!CommonUtil.isEmpty(recommendUserId)){
			
			if(recommendUserId.equals(userInfo.getId())){
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				return resultVo;
			}
			
			UserFriend userFriend = userFrendService.findOneByUserIdAndFriendUserId(userInfo.getId(),recommendUserId);
			
			
			
			UserInfo frendUserInfo = wxUserInfoService.findOne(recommendUserId);
			
			if(frendUserInfo==null){
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				
				resultVo.setErrorMsg("朋友信息为空");
				
				resultVo.setErrorCode(300);
				
				return resultVo;
			}
			
			
			if(userFriend==null){
				userFriend = new UserFriend();
				userFriend.setFriendUserId(recommendUserId);
				userFriend.setUserId(userInfo.getId());
				userFriend.setFrendUserImg(frendUserInfo.getHeadimgurl());
				userFriend.setFrendUserName(frendUserInfo.getNickname());
				userFriend.setUserName(userInfo.getNickname());
				userFriend.setUserImg(userInfo.getHeadimgurl());
				userFriend.setMeetTime(new DateTime());
				userFrendService.add(userFriend);
			}
			
			UserFriend userFriend2 = userFrendService.findOneByUserIdAndFriendUserId(recommendUserId,userInfo.getId());
			if(userFriend2==null){
				userFriend2 = new UserFriend();
				userFriend2.setFriendUserId(userInfo.getId());
				userFriend2.setUserId(recommendUserId);
				userFriend2.setMeetTime(new DateTime());
				userFriend2.setUserName(frendUserInfo.getNickname());
				userFriend2.setUserImg(frendUserInfo.getHeadimgurl());
				userFriend2.setFrendUserImg(userInfo.getHeadimgurl());
				userFriend2.setFrendUserName(userInfo.getNickname());
				userFrendService.add(userFriend2);
			}
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
}
