package com.battle.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.PersonalSpace;
import com.battle.domain.UserFriend;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.PersonalSpaceService;
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
	
	@Autowired
	private PersonalSpaceService personalSpaceService;
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
				
				/*PersonalSpace personalSpace = new PersonalSpace();
				personalSpace.setUserId(userInfo.getId());
				personalSpace.setImg1(frendUserInfo.getHeadimgurl());
				personalSpace.setImgNum(1);
				personalSpace.setIsDel(0);
				personalSpace.setType(PersonalSpace.FREND_TYPE);
				personalSpace.setName(frendUserInfo.getNickname());
				
				personalSpaceService.add(personalSpace);*/
	
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
				
				
				List<PersonalSpace> personalSpaces = personalSpaceService.findAllByIsRootAndUserId(0,userInfo.getId());
				for(PersonalSpace personalSpace:personalSpaces){
					PersonalSpace personalSpace2 = new PersonalSpace();
					personalSpace2.setUserId(frendUserInfo.getId());
					personalSpace2.setImg1(userInfo.getHeadimgurl());
					personalSpace2.setImgNum(1);
					personalSpace2.setIsDel(0);
					personalSpace2.setType(PersonalSpace.RANK_TYPE);
					personalSpace2.setName(personalSpace.getName());
					personalSpace2.setRankId(personalSpace.getRankId());
					personalSpace2.setIsRoot(0);
					personalSpaceService.add(personalSpace2);
				}
				
			}
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
}
