package com.battle.api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.battle.domain.BattleQuickRoom;
import com.battle.domain.BattleQuickRoomMember;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleQuickRoomMemberService;
import com.battle.service.BattleQuickRoomService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.Account;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.service.AccountService;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/battleQuickRoom")
public class BattleQuickRoomApi {

	@Autowired
	private BattleQuickRoomService battleQuickRoomService;
	
	@Autowired
	private BattleQuickRoomMemberService battleQuickRoomMemberService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value="list")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo list(HttpServletRequest httpServletRequest)throws Exception{
		
		Order order = new Order(Direction.ASC, "level");
		Sort sort = new Sort(order);
		Pageable pageable = new PageRequest(0, 100, sort);
		List<BattleQuickRoom> battleQuickRooms = battleQuickRoomService.findAllByStatusAndIsDel(BattleQuickRoom.IN_STATUS,0,pageable);
		
		List<Map<String, Object>> list = new ArrayList<>();
		
		for(BattleQuickRoom battleQuickRoom:battleQuickRooms){
			Map<String, Object> map = new HashMap<>();
			map.put("id", battleQuickRoom.getId());
			map.put("costBean", battleQuickRoom.getCostBean());
			map.put("costLove", battleQuickRoom.getCostLove());
			map.put("detail", battleQuickRoom.getDetail());
			map.put("groupId", battleQuickRoom.getGroupId());
			String imgs = battleQuickRoom.getImgs();
			map.put("imgs", imgs.split(","));
			map.put("imgUrl", battleQuickRoom.getImgUrl());
			map.put("level", battleQuickRoom.getLevel());
			map.put("name", battleQuickRoom.getName());
			map.put("num", battleQuickRoom.getNum());
			map.put("roomId", battleQuickRoom.getRoomId());
			map.put("status", battleQuickRoom.getStatus());
			
			Order order2 = new Order(Direction.DESC, "process");
			Sort sort2 = new Sort(order2);
			Pageable pageable2 = new PageRequest(0, 10, sort2);
			List<BattleQuickRoomMember> battleQuickRoomMembers = battleQuickRoomMemberService.findAllByQuickRoomId(battleQuickRoom.getId(),pageable2);
			map.put("members",battleQuickRoomMembers);
			list.add(map);
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(list);
		
		return resultVo;
		
	}
	
	@RequestMapping(value="takepart")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo takepart(HttpServletRequest httpServletRequest)throws Exception{

		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		BattleQuickRoom battleQuickRoom = battleQuickRoomService.findOne(id);
		
		Integer costBean = battleQuickRoom.getCostBean();
		
		Integer costLove = battleQuickRoom.getCostLove();
		
		Account account = accountService.fineOne(userInfo.getAccountId());
		
		Long beanNum = account.getWisdomCount();
		
		Integer loveNum = account.getLoveLife();
		
		if(costBean==null){
			costBean = 0;
		}
		
		if(costLove==null){
			costLove = 0;
		}
		
		if(beanNum==null){
			beanNum = 0L;
		}
		
		if(loveNum==null){
			loveNum = 0;
		}
		
		if(beanNum>=costBean){
			account.setWisdomCount(beanNum-costBean);
		}else{
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorCode(0);
			return resultVo;
		}
		
		if(loveNum>=costLove){
			account.setLoveLife(loveNum-costLove);
		}else{
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorCode(1);
			return resultVo;
		}
		
		accountService.update(account);;
		
		BattleQuickRoomMember battleQuickRoomMember = battleQuickRoomMemberService.findOneByQuickRoomIdAndUserId(battleQuickRoom.getId(),userInfo.getId());
		
		
		if(battleQuickRoomMember!=null){
			battleQuickRoomMember.setStatus(BattleQuickRoomMember.IN_STATUS);
			
			battleQuickRoomMemberService.update(battleQuickRoomMember);
		}else{
			battleQuickRoomMember = new BattleQuickRoomMember();
			battleQuickRoomMember.setImgUrl(userInfo.getHeadimgurl());
			battleQuickRoomMember.setIsDel(0);
			battleQuickRoomMember.setMedalNum(0);
			battleQuickRoomMember.setNickname(userInfo.getNickname());
			battleQuickRoomMember.setProcess(0);
			battleQuickRoomMember.setQuickRoomId(battleQuickRoom.getId());
			battleQuickRoomMember.setUserId(userInfo.getId());
			battleQuickRoomMember.setStatus(BattleQuickRoomMember.IN_STATUS);
			battleQuickRoomMemberService.add(battleQuickRoomMember);
		}

		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleQuickRoomMember);
		
		return resultVo;
	}
}
