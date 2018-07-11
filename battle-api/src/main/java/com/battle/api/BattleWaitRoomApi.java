package com.battle.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleWait;
import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomGroup;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.domain.BattleWaitRoomNum;
import com.battle.domain.UserStatus;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleWaitRoomGroupService;
import com.battle.service.BattleWaitRoomMemberService;
import com.battle.service.BattleWaitRoomNumService;
import com.battle.service.BattleWaitRoomService;
import com.battle.service.UserStatusService;
import com.battle.socket.DownEvent;
import com.battle.socket.WebSocketManager;
import com.battle.socket.service.BattleWaitRoomSocketService;
import com.battle.socket.service.UserStatusManager;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/battleWaitRoom")
public class BattleWaitRoomApi {

	@Autowired
	private BattleWaitRoomService battleWaitRoomService;
	
	@Autowired
	private BattleWaitRoomMemberService battleWaitRoomMemberService;
	
	@Autowired
	private BattleWaitRoomGroupService battleWaitRoomGroupService;
	
	@Autowired
	private BattleWaitRoomNumService battleWaitRoomNumService;
	
	@Autowired
	private  BattleWaitRoomSocketService battleWaitRoomSocketService;
	
	@Autowired
	private BattleRoomFactory battleRoomFactory;
	
	@Autowired
	private UserStatusManager userStatusManager;
	
	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
	private WebSocketManager webSocketManager;
	
	
	
	@RequestMapping(value="out")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo out(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOneByRoomIdAndUserId(id, userInfo.getId());
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.OUT_STATUS);
		
		battleWaitRoomMemberService.update(battleWaitRoomMember);
		
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	@RequestMapping(value="start")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo start(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		
		BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(id);
		
		List<String> userIds = new ArrayList<>();

		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			int status = battleWaitRoomMember.getStatus().intValue();
			if(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.END_STATUS){
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				return resultVo;
			}
		}
		
		List<BattleWaitRoomMember> downLineMembers = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			UserStatus userStatus = userStatusManager.getUserStatus(battleWaitRoomMember.getToken());
			if(userStatus!=null&&webSocketManager.isOpen(battleWaitRoomMember.getToken())){
				userStatus.setRoomId(id);
				userStatus.setStatus(UserStatus.IN_STATUS);
				userStatusService.update(userStatus);
				userIds.add(battleWaitRoomMember.getUserId());
			}else{
				downLineMembers.add(battleWaitRoomMember);
			}
		}
		
		if(downLineMembers.size()>0){
			for(BattleWaitRoomMember downLineMember:downLineMembers){
				downLineMember.setStatus(BattleWaitRoomMember.END_STATUS);
				battleWaitRoomMemberService.update(downLineMember);
				battleWaitRoomSocketService.waitRoomMemberPublish(downLineMember, userIds);
			}
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		battleWaitRoom.setStatus(BattleWaitRoom.IN_STATUS);
		
		battleWaitRoomService.update(battleWaitRoom);
		battleRoomFactory.init(battleWaitRoom.getGroupId(),userIds,BattleRoomVo.ROOM_TYPE,null);
		
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
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOneByRoomIdAndUserId(id,userInfo.getId());
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.FREE_STATUS);
		
		battleWaitRoomMemberService.update(battleWaitRoomMember);
		
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitRoomMember);
		return resultVo;
	}
	
	
	@RequestMapping(value="kickOut")
	@ResponseBody
	@Transactional
	public ResultVo kickOut(HttpServletRequest httpServletRequest)throws Exception{
		
		String id = httpServletRequest.getParameter("id");
		
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOne(id);
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.OUT_STATUS);
		
		battleWaitRoomMemberService.update(battleWaitRoomMember);
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoomMember.getRoomId());
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			userIds.add(battleWaitRoomMember2.getUserId());
		}
		
		System.out.println("...........userIds:"+userIds);
		System.out.println("...........battleWaitRoomMembers:"+battleWaitRoomMembers);
		
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	public ResultVo info(HttpServletRequest httpServletRequest)throws Exception{
		
		String id = httpServletRequest.getParameter("id");
		
		
		BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(id);
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
	
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoom);
		data.put("members", battleWaitRoomMembers);
		
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
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOneByRoomIdAndUserId(id,userInfo.getId());
		
		battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
		
		battleWaitRoomMemberService.update(battleWaitRoomMember);
		
		List<String> userIds = new ArrayList<>();
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		
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
		BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(id);
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOneByRoomIdAndUserId(id, userInfo.getId());
		if(battleWaitRoomMember==null){
			battleWaitRoomMember = new BattleWaitRoomMember();
			battleWaitRoomMember.setImgUrl(userInfo.getHeadimgurl());
			battleWaitRoomMember.setNickname(userInfo.getNickname());
			battleWaitRoomMember.setRoomId(battleWaitRoom.getId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMember.setUserId(userInfo.getId());
			battleWaitRoomMember.setIsOwner(0);
			battleWaitRoomMember.setToken(userInfo.getToken());
			battleWaitRoomMemberService.add(battleWaitRoomMember);
		}else{
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.FREE_STATUS);
			battleWaitRoomMemberService.update(battleWaitRoomMember);
		}
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoom);
		data.put("members", battleWaitRoomMembers);
		data.put("member", battleWaitRoomMember);
		
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
	
		System.out.println("............battleWaitRoomMember.status:"+battleWaitRoomMember.getStatus());
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);

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
		
		
		List<BattleWaitRoom> battleWaitRooms = battleWaitRoomService.findAllByOwnerIdAndStatus(userInfo.getId(),BattleWaitRoom.FREE_STATUS);
		BattleWaitRoom battleWaitRoom = null;
		if(battleWaitRooms==null||battleWaitRooms.size()==0){
			battleWaitRoom = new BattleWaitRoom();
			List<BattleWaitRoomGroup> battleWaitRoomGroups = battleWaitRoomGroupService.findAllByIsDefault(1);
			BattleWaitRoomGroup battleWaitRoomGroup = battleWaitRoomGroups.get(0);
			
			battleWaitRoom.setGroupId(battleWaitRoomGroup.getGroupId());
			battleWaitRoom.setGroupName(battleWaitRoomGroup.getName());
			
			List<BattleWaitRoomNum> battleWaitRoomNums = battleWaitRoomNumService.findAllByIsDefault(1);
			BattleWaitRoomNum battleWaitRoomNum = battleWaitRoomNums.get(0);
			battleWaitRoom.setMaxNum(battleWaitRoomNum.getNum());
			
			battleWaitRoom.setOwnerId(userInfo.getId());
			battleWaitRoom.setStatus(BattleWaitRoom.FREE_STATUS);
			
			battleWaitRoomService.add(battleWaitRoom);
		}else{
			battleWaitRoom = battleWaitRooms.get(0);
		}
		
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOneByRoomIdAndUserId(battleWaitRoom.getId(), userInfo.getId());
		List<BattleWaitRoomMember> members = new ArrayList<>();
		if(battleWaitRoomMember==null){
			battleWaitRoomMember = new BattleWaitRoomMember();
			battleWaitRoomMember.setImgUrl(userInfo.getHeadimgurl());
			battleWaitRoomMember.setNickname(userInfo.getNickname());
			battleWaitRoomMember.setRoomId(battleWaitRoom.getId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMember.setIsOwner(1);
			battleWaitRoomMember.setUserId(userInfo.getId());
			battleWaitRoomMember.setToken(userInfo.getToken());
			battleWaitRoomMemberService.add(battleWaitRoomMember);
			members.add(battleWaitRoomMember);
		}else{
			members = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoom.getId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMemberService.update(battleWaitRoomMember);
			
			for(BattleWaitRoomMember battleWaitRoomMember2:members){
				if(battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
					battleWaitRoomMember2.setStatus(BattleWaitRoomMember.READY_STATUS);
				}
			}
		}
		
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoom.getId());
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		
		
		Map<String, Object> data = new HashMap<>();
		data.put("room", battleWaitRoom);
		data.put("members", members);
		data.put("member", battleWaitRoomMember);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
		
	}
}
