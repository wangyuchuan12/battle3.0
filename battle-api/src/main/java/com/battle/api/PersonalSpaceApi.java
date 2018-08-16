package com.battle.api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleFactory;
import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.domain.BattleRankSubject;
import com.battle.domain.BattleSelectSubject;
import com.battle.domain.PersonalSpace;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleFactoryService;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;
import com.battle.service.BattleRankSubjectService;
import com.battle.service.BattleSelectSubjectService;
import com.battle.service.PersonalSpaceService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/personalSpace")
public class PersonalSpaceApi {

	@Autowired
	private PersonalSpaceService personalSpaceService;
	
	@Autowired
	private BattleSelectSubjectService battleSelectSubjectService;
	
	@Autowired
	private BattleRankMemberService battleRankMemberService;
	
	@Autowired
	private BattleRankSubjectService battleRankSubjectService;
	
	@Autowired
	private BattleFactoryService battleFactoryService;
	
	@Autowired
	private BattleRankService battleRankService;
	
	@RequestMapping(value="list")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo list(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		Order order = new Order(Direction.DESC, "activityTime");
		Sort sort = new Sort(order);
		Pageable pageable = new PageRequest(0, 100, sort);
		List<PersonalSpace> personalSpaces = personalSpaceService.findAllByUserIdAndIsDel(userInfo.getId(),0,pageable);
		
		List<PersonalSpace> publicPersonalSpace = personalSpaceService.findAllByIsPublic(1);
		
		publicPersonalSpace.addAll(personalSpaces);
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(publicPersonalSpace);
		return resultVo;
		
	}
	
	@RequestMapping(value="selectSubjects")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo selectSubjects(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		String factoryId = httpServletRequest.getParameter("factoryId");
		BattleFactory battleFactory = battleFactoryService.findOne(factoryId);
		String page = httpServletRequest.getParameter("page");
		String size = httpServletRequest.getParameter("size");
		Order order = new Order(Direction.ASC, "index");
		Sort sort = new Sort(order);
		Pageable pageable = new PageRequest(Integer.parseInt(page), Integer.parseInt(size), sort);
		List<BattleSelectSubject> battleSelectSubjects = battleSelectSubjectService.findAllByFactoryIdAndIsDel(battleFactory.getId(),0,pageable);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setData(battleSelectSubjects);
		resultVo.setSuccess(true);
		
		return resultVo;
		
	}
	
	
	@RequestMapping(value="infoByRankId")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo infoByRankId(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String rankId = httpServletRequest.getParameter("rankId");
		
		
		BattleRank battleRank = battleRankService.findOne(rankId);
		PersonalSpace personalSpace = personalSpaceService.findOneByUserIdAndRankIdAndType(userInfo.getId(), rankId, PersonalSpace.RANK_TYPE);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("id", personalSpace.getId());
		data.put("name", personalSpace.getName());
		data.put("imgNum", personalSpace.getImgNum());
		data.put("img1", personalSpace.getImg1());
		data.put("img2", personalSpace.getImg2());
		data.put("img3", personalSpace.getImg3());
		data.put("img4", personalSpace.getImg4());
		data.put("img5", personalSpace.getImg5());
		data.put("img6", personalSpace.getImg6());
		data.put("img7", personalSpace.getImg7());
		data.put("img8", personalSpace.getImg8());
		data.put("img9", personalSpace.getImg9());
		data.put("detail", personalSpace.getDetail());
		data.put("type", personalSpace.getType());
		data.put("rankId", personalSpace.getRankId());
		data.put("isRoot", personalSpace.getIsRoot());
		data.put("factoryId", battleRank.getFactoryId());
		
		if(personalSpace.getType().intValue()==PersonalSpace.RANK_TYPE){
			Order order = new Order(Direction.DESC, "process");
			Sort sort = new Sort(order);
			Pageable pageable = new PageRequest(0, 10, sort);
			Page<BattleRankMember> memberPage = battleRankMemberService.findAllByRankId(personalSpace.getRankId(), pageable);
			data.put("rankMembers", memberPage.getContent());
			
		
			pageable = new PageRequest(0, 10);
			Page<BattleRankSubject> subjectPage = battleRankSubjectService.findAllByRankIdAndIsDel(personalSpace.getRankId(),0,pageable);
			
			data.put("subjects", subjectPage.getContent());
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
		
	}
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo info(HttpServletRequest httpServletRequest)throws Exception{
		String id = httpServletRequest.getParameter("id");
		
		PersonalSpace personalSpace = personalSpaceService.findOne(id);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		BattleRank battleRank = battleRankService.findOne(personalSpace.getRankId());
		data.put("id", personalSpace.getId());
		data.put("name", personalSpace.getName());
		data.put("imgNum", personalSpace.getImgNum());
		data.put("img1", personalSpace.getImg1());
		data.put("img2", personalSpace.getImg2());
		data.put("img3", personalSpace.getImg3());
		data.put("img4", personalSpace.getImg4());
		data.put("img5", personalSpace.getImg5());
		data.put("img6", personalSpace.getImg6());
		data.put("img7", personalSpace.getImg7());
		data.put("img8", personalSpace.getImg8());
		data.put("img9", personalSpace.getImg9());
		data.put("detail", personalSpace.getDetail());
		data.put("type", personalSpace.getType());
		data.put("rankId", personalSpace.getRankId());
		data.put("isRoot", personalSpace.getIsRoot());
		data.put("factoryId", battleRank.getFactoryId());
		if(personalSpace.getType().intValue()==PersonalSpace.RANK_TYPE){
			Order order = new Order(Direction.DESC, "process");
			Sort sort = new Sort(order);
			Pageable pageable = new PageRequest(0, 10, sort);
			Page<BattleRankMember> memberPage = battleRankMemberService.findAllByRankId(personalSpace.getRankId(), pageable);
			data.put("rankMembers", memberPage.getContent());
			
		
			pageable = new PageRequest(0, 10);
			Page<BattleRankSubject> subjectPage = battleRankSubjectService.findAllByRankIdAndIsDel(personalSpace.getRankId(),0,pageable);
			
			data.put("subjects", subjectPage.getContent());
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
		
	}
}
