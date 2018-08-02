package com.battle.api;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleSelectSubject;
import com.battle.domain.PersonalSpace;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleSelectSubjectService;
import com.battle.service.PersonalSpaceService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/personalSpace")
public class PersonalSpaceApi {

	@Autowired
	private PersonalSpaceService personalSpaceService;
	
	@Autowired
	private BattleSelectSubjectService battleSelectSubjectService;
	
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
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(personalSpaces);
		return resultVo;
		
	}
	
	@RequestMapping(value="selectSubjects")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo selectSubjects(HttpServletRequest httpServletRequest)throws Exception{
		
		String page = httpServletRequest.getParameter("page");
		String size = httpServletRequest.getParameter("size");
		Order order = new Order(Direction.ASC, "index");
		Sort sort = new Sort(order);
		Pageable pageable = new PageRequest(Integer.parseInt(page), Integer.parseInt(size), sort);
		List<BattleSelectSubject> battleSelectSubjects = battleSelectSubjectService.findAllByIsDel(0,pageable);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setData(battleSelectSubjects);
		resultVo.setSuccess(true);
		
		return resultVo;
		
	}
}
