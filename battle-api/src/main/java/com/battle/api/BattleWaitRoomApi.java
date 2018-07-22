package com.battle.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleSearchRoomReward;
import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.param.RoomParam;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.room.exception.BattleWaitRoomStartException;
import com.battle.room.executer.BattleWaitRoomConnector;
import com.battle.room.executer.BattleWaitRoomDataManager;
import com.battle.room.vo.BattleWaitRoomMemberVo;
import com.battle.room.vo.BattleWaitRoomVo;
import com.battle.service.BattleSearchRoomRewardService;
import com.battle.service.BattleWaitRoomMemberService;
import com.battle.service.BattleWaitRoomService;
import com.battle.socket.service.BattleWaitRoomSocketService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/battleWaitRoom")
public class BattleWaitRoomApi {
	
	@Autowired
	private BattleWaitRoomService battleWaitRoomService;
	
	@Autowired
	private BattleWaitRoomMemberService battleWaitRoomMemberService;
	
	
	@Autowired
	private  BattleWaitRoomSocketService battleWaitRoomSocketService;
	
	@Autowired
	private BattleRoomFactory battleRoomFactory;
	
	
	@Autowired
	private BattleSearchRoomRewardService battleSearchRoomRewardService;
	
	@Autowired
	private BattleWaitRoomConnector battleWaitRoomConnector;
	
	
	@RequestMapping(value="out")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo out(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		
		battleWaitRoomConnector.out(id, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	@RequestMapping(value="start")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo start(HttpServletRequest httpServletRequest) throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");

		try {
			battleWaitRoomConnector.start(id);
		} catch (BattleWaitRoomStartException e) {
			ResultVo resultVo = new ResultVo();
			
			resultVo.setSuccess(false);
			
			resultVo.setErrorCode(e.getType());
			
			return resultVo;
		}
		
		BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomConnector.findDataManager(id);
		
		BattleWaitRoomVo battleWaitRoom = battleWaitRoomDataManager.getBattleWaitRoom();
		
		List<BattleWaitRoomMemberVo> battleWaitRoomMembers = battleWaitRoomDataManager.getMembers();
		
		List<String> userIds = new ArrayList<>();
		
		for(BattleWaitRoomMemberVo battleWaitRoomMember:battleWaitRoomMembers){
			userIds.add(battleWaitRoomMember.getUserId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.END_STATUS);
			battleWaitRoomMember.setEndContent("比赛已经开始");
		}
		List<BattleSearchRoomReward> battleSearchRoomRewards = battleSearchRoomRewardService.findAllBySearchKey(battleWaitRoom.getSearchKey());
		List<BattleRewardVo> battleRewards = new ArrayList<>();
		for(BattleSearchRoomReward battleSearchRoomReward:battleSearchRoomRewards){
			BattleRewardVo battleReward = new BattleRewardVo();
			battleReward.setRank(battleSearchRoomReward.getRank());
			battleReward.setRewardBean(battleSearchRoomReward.getRewardBean());
			battleReward.setRewardLove(battleSearchRoomReward.getRewardLove());
			battleRewards.add(battleReward);
		}
		
		
		
		Map<String, Object> data = new HashMap<>();
		data.put("rewards", battleRewards);
		
		RoomParam roomParam = new RoomParam();
		roomParam.setData(data);
		roomParam.setGroupId(battleWaitRoom.getGroupId());
		roomParam.setType(BattleRoomVo.DAN_TYPE);
		
		List<UserParam> userParams = new ArrayList<>();
		roomParam.setUserParams(userParams);
		
		for(String userId:userIds){
			UserParam userParam = new UserParam();
			userParam.setIsRobot(0);
			userParam.setUserId(userId);
			userParams.add(userParam);
		}
		battleRoomFactory.init(roomParam);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	@RequestMapping(value="cancel")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo cancel(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
	
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomConnector.cancel(id, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitRoomMember);
		return resultVo;
	}
	
	
	@RequestMapping(value="kickOut")
	@ResponseBody
	@Transactional
	public ResultVo kickOut(HttpServletRequest httpServletRequest)throws Exception{

		String roomId = httpServletRequest.getParameter("roomId");
		String memberId = httpServletRequest.getParameter("memberId");
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
	
		
		
		battleWaitRoomConnector.kickOut(roomId,memberId);
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	public ResultVo info(HttpServletRequest httpServletRequest)throws Exception{
		
		String id = httpServletRequest.getParameter("id");
		
		
		BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomConnector.findDataManager(id);
	
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoomDataManager.getBattleWaitRoom());
		data.put("members", battleWaitRoomDataManager.getMembers());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		return resultVo;
	}
	
	
	@RequestMapping(value="ready")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo ready(HttpServletRequest httpServletRequest)throws Exception{

		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomConnector.ready(id, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitRoomMember);
		return resultVo;
	}
	
	
	@RequestMapping(value="into")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo into(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		battleWaitRoomConnector.into(id, userInfo);
		
		
		BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomConnector.findDataManager(id);
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoomDataManager.getBattleWaitRoom());
		data.put("members", battleWaitRoomDataManager.getMembers());
		data.put("member", battleWaitRoomDataManager.findMemberByUserId(userInfo.getId()));
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		return resultVo;
		
	}
	
	
	@RequestMapping(value="toPrivite")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo toPrivite(HttpServletRequest httpServletRequest)throws Exception{
		
		System.out.println(".........toPrivite");
		String id = httpServletRequest.getParameter("id");
		
		BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(id);
		
		battleWaitRoom.setIsPublic(0);
		
		battleWaitRoomService.update(battleWaitRoom);
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoom.getId());
		List<String> userIds = new ArrayList<>();
		
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			int status = battleWaitRoomMember.getStatus();
			if(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS){
				userIds.add(battleWaitRoomMember.getUserId());
			}
		}
		
		battleWaitRoomSocketService.roomInfoPublish(battleWaitRoom, userIds);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
		
	}
	
	
	@RequestMapping(value="toPublic")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo toPublic(HttpServletRequest httpServletRequest)throws Exception{
		
		System.out.println(".........toPublic");
		String id = httpServletRequest.getParameter("id");
		
		BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(id);
		
		battleWaitRoom.setIsPublic(1);
		
		battleWaitRoomService.update(battleWaitRoom);
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoom.getId());
		List<String> userIds = new ArrayList<>();
		
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			int status = battleWaitRoomMember.getStatus();
			if(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS){
				userIds.add(battleWaitRoomMember.getUserId());
			}
		}
		
		battleWaitRoomSocketService.roomInfoPublish(battleWaitRoom, userIds);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
		
	}
	
	
	@RequestMapping(value="ownerChange")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo ownerChange(HttpServletRequest httpServletRequest)throws Exception{
		String id = httpServletRequest.getParameter("id");
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);

		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomConnector.ownerChange(id, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitRoomMember);
		return resultVo;
	}
	
	
	@RequestMapping(value="searchRoom")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo searchRoom(HttpServletRequest httpServletRequest)throws Exception{
		
		String searchKey = httpServletRequest.getParameter("searchKey");
		
		List<BattleWaitRoomVo> battleWaitRooms = battleWaitRoomConnector.searchRoom(searchKey);
		
		if(battleWaitRooms==null||battleWaitRooms.size()==0){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		Random r = new Random();
		
		/*if(battleWaitRooms.size()<2&&r.nextInt(10)<2){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}*/
		
		
		
		Integer roomNum = battleWaitRooms.size();
		
		Integer index = r.nextInt(roomNum);
		BattleWaitRoomVo battleWaitRoom = battleWaitRooms.get(0);
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoom.getId());
		
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoom);
		data.put("members", battleWaitRoomMembers);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		return resultVo;
		
	}
	
	
	
	@RequestMapping(value="create")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo create(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		String searchKey = httpServletRequest.getParameter("searchKey");
		
		if(CommonUtil.isEmpty(searchKey)){
			searchKey = "personal";
		}
		
		
		BattleWaitRoomVo battleWaitRoom = battleWaitRoomConnector.create(userInfo, searchKey);
		
		
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoom);
		data.put("members",battleWaitRoomConnector.getMembers(battleWaitRoom.getId()));
		data.put("member", battleWaitRoomConnector.findMember(battleWaitRoom.getId(), userInfo.getId()));
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;

	}
}
