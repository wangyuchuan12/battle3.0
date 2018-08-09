package com.battle.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleFactory;
import com.battle.domain.BattleRank;
import com.battle.domain.BattleSelectSubject;
import com.battle.domain.PersonalSpace;
import com.battle.domain.UserFriend;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.rank.manager.RankManager;
import com.battle.rank.service.BattleRankHandleService;
import com.battle.service.BattleFactoryService;
import com.battle.service.BattleRankService;
import com.battle.service.BattleSelectSubjectService;
import com.battle.service.PersonalSpaceService;
import com.battle.service.UserFrendService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/manager")
public class BattleManagerApi {

	@Autowired
	private RankManager rankManager;
	
	@Autowired
	private BattleRankService battleRankService;

	@Autowired
	private BattleSelectSubjectService battleSelectSubjectService;
	
	@Autowired
	private PersonalSpaceService personalSpaceService;
	
	@Autowired
	private UserFrendService userFrendService;
	
	@Autowired
	private BattleRankHandleService battleRankHandleService;
	
	@Autowired
	private BattleFactoryService battleFactoryService;
	
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo info(HttpServletRequest httpServletRequest)throws Exception{
		String id = httpServletRequest.getParameter("id");
		PersonalSpace personalSpace = personalSpaceService.findOne(id);
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(personalSpace);
		return resultVo;
	}
	
	
	@RequestMapping(value="ranks")
	@ResponseBody
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo ranks(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		BattleFactory battleFactory = battleFactoryService.findOneByUserId(userInfo.getId());
		
		List<BattleRank> battleRanks = battleRankService.findAllByFactoryId(battleFactory.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRanks);
		return resultVo;
	}
	
	
	@RequestMapping(value="createRank")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo createRank(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		BattleFactory battleFactory = battleFactoryService.findOneByUserId(userInfo.getId());
		
		String subjectIds = httpServletRequest.getParameter("subjectIds");
		
		BattleRank battleRank  = null;
		List<BattleRank> battleRanks = battleRankService.findAllByOwnerUserId(userInfo.getId());
		
		if(battleRanks!=null&&battleRanks.size()>0){
			battleRank = battleRanks.get(0);
		}
		
		if(battleRank==null){
		
			battleRank = new BattleRank();
			battleRank.setEndTime(new DateTime());
			battleRank.setStartDate(new DateTime());
			battleRank.setIndex(0);
			battleRank.setIsDefault(0);
			battleRank.setIsStart(0);
			battleRank.setQuestionCount(3);
			battleRank.setStageIndex(0);
			battleRank.setSubBean(0);
			battleRank.setSubjectCount(3);
			battleRank.setTimeLong(10);
			battleRank.setOwnerUserId(userInfo.getId());
			battleRank.setFactoryId(battleFactory.getId());
			battleRank.setImgUrl(userInfo.getHeadimgurl());
			battleRank.setName(userInfo.getNickname()+"创建的空间");
			battleRank.setDetail(userInfo.getNickname()+"创建的空间");
			battleRankService.add(battleRank);
			
			battleRankHandleService.takepart(battleRank.getId(), userInfo.getId());
			Integer num = 100;
			String[] subjectIdArray = subjectIds.split(",");
			
			for(String subjectId:subjectIdArray){
				BattleSelectSubject battleSelectSubject = battleSelectSubjectService.findOne(subjectId);
				rankManager.addBySubjectId(battleRank.getId(), battleSelectSubject.getPeriodId(), battleSelectSubject.getSubjectId(),num);
			}
			
			PersonalSpace personalSpace = new PersonalSpace();
			personalSpace.setActivityTime(new DateTime());
			personalSpace.setCreateAt(new DateTime());
			personalSpace.setDetail(userInfo.getNickname()+"创建的空间");
			personalSpace.setImg1(userInfo.getHeadimgurl());
			personalSpace.setImgNum(1);
			personalSpace.setIsDel(0);
			personalSpace.setIsPublic(0);
			personalSpace.setName(userInfo.getNickname()+"创建的空间");
			personalSpace.setRankId(battleRank.getId());
			personalSpace.setType(PersonalSpace.RANK_TYPE);
			personalSpace.setUserId(userInfo.getId());
			personalSpace.setIsRoot(1);
			
			personalSpaceService.add(personalSpace);
			
			List<UserFriend> userFriends = userFrendService.findAllByFriendUserId(userInfo.getId());
			
			for(UserFriend userFriend:userFriends){
				PersonalSpace personalSpace2 = new PersonalSpace();
				personalSpace2.setActivityTime(new DateTime());
				personalSpace2.setCreateAt(new DateTime());
				personalSpace2.setDetail(userInfo.getNickname()+"创建的空间");
				personalSpace2.setImg1(userInfo.getHeadimgurl());
				personalSpace2.setImgNum(1);
				personalSpace2.setIsDel(0);
				personalSpace2.setIsPublic(0);
				personalSpace2.setName(userInfo.getNickname()+"创建的空间");
				personalSpace2.setRankId(battleRank.getId());
				personalSpace2.setType(PersonalSpace.RANK_TYPE);
				personalSpace2.setUserId(userFriend.getUserId());
				personalSpace2.setIsRoot(0);
				personalSpaceService.add(personalSpace2);
				
				battleRankHandleService.takepart(battleRank.getId(), userFriend.getUserId());
			}
			
			
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(true);
			return resultVo;
		}else{
			String[] subjectIdArray = subjectIds.split(",");
			Integer num = 100;
			for(String subjectId:subjectIdArray){
				BattleSelectSubject battleSelectSubject = battleSelectSubjectService.findOne(subjectId);
				rankManager.addBySubjectId(battleRank.getId(), battleSelectSubject.getPeriodId(), battleSelectSubject.getSubjectId(),num);
			}
			
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(true);
			return resultVo;
		}
		
		
		
		
	}
}
