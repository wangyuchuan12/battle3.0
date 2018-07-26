package com.battle.api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
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

import com.battle.domain.BattleRank;
import com.battle.domain.BattleRankMember;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleRankMemberService;
import com.battle.service.BattleRankService;
import com.battle.service.other.BattleRoomCoolHandle;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/battleRank")
public class BattleRankApi {
	
	@Autowired
	private BattleRankService battleRankService;
	
	@Autowired
	private BattleRankMemberService battleRankMemberService;
	
	@Autowired
	private BattleRoomCoolHandle battleRoomCoolHandle;
	
	
	
	@RequestMapping(value="bInfo")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo bInfo(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
		
		
		BattleRank battleRank = null;
		
		if(battleRanks==null||battleRanks.size()==0){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}else{
			battleRank = battleRanks.get(0);
		}
		
		BattleRoomCoolMemberVo battleRoomCoolMemberVo = battleRoomCoolHandle.getCoolMember(battleRank.getRoomId(), userInfo.getId());
		
		battleRoomCoolMemberVo = battleRoomCoolHandle.filterMember(battleRoomCoolMemberVo);
		
		Map<String, Object> data = new HashMap<>();
		
		BattleRankMember battleRankMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(), userInfo.getId());
		if(battleRankMember!=null){
			long rank = battleRankMemberService.rank(battleRank.getId(), battleRankMember.getProcess());
			long diff = battleRank.getEndTime().getMillis()-new DateTime().getMillis();
			data.put("rank", rank);
			data.put("diff", diff);
			data.put("process", battleRankMember.getProcess());
		}
		
		
		data.put("cool", battleRoomCoolMemberVo);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
	}
	
	
	
	@RequestMapping(value="loveCool")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo loveCool(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
		
		
		BattleRank battleRank = null;
		
		if(battleRanks==null||battleRanks.size()==0){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}else{
			battleRank = battleRanks.get(0);
		}
		
		BattleRoomCoolMemberVo battleRoomCoolMemberVo = battleRoomCoolHandle.getCoolMember(battleRank.getRoomId(), userInfo.getId());
		
		battleRoomCoolMemberVo = battleRoomCoolHandle.filterMember(battleRoomCoolMemberVo);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRoomCoolMemberVo);
		
		return resultVo;
		
	}

	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo info(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
		
		
		BattleRank battleRank = null;
		
		if(battleRanks==null||battleRanks.size()==0){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}else{
			battleRank = battleRanks.get(0);
		}
	
		Order order = new Order(Direction.DESC, "process");
		Sort sort = new Sort(order);
		Pageable pageable = new PageRequest(0, 100, sort);
		Page<BattleRankMember> battleRankMemberPage = battleRankMemberService.findAllByRankId(battleRank.getId(),pageable);
		List<BattleRankMember> battleRankMembers = battleRankMemberPage.getContent();
		Map<String, Object> data = new HashMap<>();
		
		data.put("roomId", battleRank.getRoomId());
		
		BattleRankMember myMember = battleRankMemberService.findOneByRankIdAndUserId(battleRank.getId(), userInfo.getId());
		
		Map<String, Object> memberInfo = new HashMap<>();
		memberInfo.put("nickname", userInfo.getNickname());
		memberInfo.put("headImg", userInfo.getHeadimgurl());
		memberInfo.put("process",0);
		if(myMember!=null){
			memberInfo.put("nickname", myMember.getNickname());
			memberInfo.put("headImg", myMember.getHeadImg());
			memberInfo.put("process", myMember.getProcess());
			Long rank = battleRankMemberService.rank(battleRank.getId(), myMember.getProcess());
			memberInfo.put("rank",rank);
		}
		
		List<Map<String, Object>> members = new ArrayList<>();
		
		for(int i=0;i<battleRankMembers.size();i++){
			BattleRankMember battleRankMember = battleRankMembers.get(i);
			Map<String, Object> member = new HashMap<>();
			member.put("nickname", battleRankMember.getNickname());
			member.put("headImg", battleRankMember.getHeadImg());
			member.put("rank", i+1);
			member.put("process", battleRankMember.getProcess());
			members.add(member);
		}
		data.put("members", members);
		data.put("memberInfo", memberInfo);
		
		if(battleRankMembers.size()>0){
			BattleRankMember battleRankMember = battleRankMembers.get(0);
			Map<String, Object> firstMemberInfo = new HashMap<>();
			firstMemberInfo.put("nickname", battleRankMember.getNickname());
			firstMemberInfo.put("headImg", battleRankMember.getHeadImg());
			firstMemberInfo.put("coverUrl", battleRankMember.getCoverUrl());
			firstMemberInfo.put("process", battleRankMember.getProcess());
			data.put("firstMemberInfo",firstMemberInfo);
		}else{
			Map<String, Object> firstMemberInfo = new HashMap<>();
			firstMemberInfo.put("nickname", userInfo.getNickname());
			firstMemberInfo.put("headImg", userInfo.getHeadimgurl());
			firstMemberInfo.put("coverUrl", userInfo.getHeadimgurl());
			firstMemberInfo.put("process", 0);
			data.put("firstMemberInfo",firstMemberInfo);
		}
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
	}
}
