package com.battle.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleSearchRoomReward;
import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomGroup;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.domain.BattleWaitRoomNum;
import com.battle.domain.UserStatus;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleSearchRoomRewardService;
import com.battle.service.BattleWaitRoomGroupService;
import com.battle.service.BattleWaitRoomMemberService;
import com.battle.service.BattleWaitRoomNumService;
import com.battle.service.BattleWaitRoomService;
import com.battle.service.UserStatusService;
import com.battle.socket.WebSocketManager;
import com.battle.socket.service.BattleWaitRoomSocketService;
import com.battle.socket.service.UserStatusManager;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;
import com.wyc.handle.BattleWaitRoomHandle;

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
	
	@Autowired
	private BattleWaitRoomHandle battleWaitRoomHandle;
	
	@Autowired
	private BattleSearchRoomRewardService battleSearchRoomRewardService;
	
	
	
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
		
		
		boolean flag = false;
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			if(battleWaitRoomMember2.getStatus().intValue()==BattleWaitRoomMember.FREE_STATUS||battleWaitRoomMember2.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS){
				if(webSocketManager.isOpen(battleWaitRoomMember2.getToken())){
					flag = true;
				}
			}
			if(!battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
				userIds.add(battleWaitRoomMember2.getUserId());
			}
		}
		
		if(!flag){
			BattleWaitRoom battleWaitRoom = battleWaitRoomService.findOne(id);
			battleWaitRoom.setStatus(BattleWaitRoom.COMPLETE_STATUS);
			battleWaitRoomService.update(battleWaitRoom);
		}else if(battleWaitRoomMember.getIsOwner().intValue()==1){
			if(battleWaitRoomMembers.size()>1){
				for(BattleWaitRoomMember switchBattleWaitRoomMember:battleWaitRoomMembers){
					int status = switchBattleWaitRoomMember.getStatus();
					if(switchBattleWaitRoomMember.getIsOwner().intValue()==0&&
							(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS)){
						
						try {
							battleWaitRoomSocketService.changeOwnerPublish(switchBattleWaitRoomMember);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
				}
			}else{
	
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
				resultVo.setErrorCode(0);
				return resultVo;
			}
		}
		
		List<BattleWaitRoomMember> downLineMembers = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			battleWaitRoomMember.setIsEnd(1);
			battleWaitRoomMember.setEndContent("比赛已经开始");
			battleWaitRoomMemberService.update(battleWaitRoomMember);
			UserStatus userStatus = userStatusManager.getUserStatus(battleWaitRoomMember.getToken());
			if(userStatus!=null&&webSocketManager.isOpen(battleWaitRoomMember.getToken())&&battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS){
				userStatus.setRoomId(id);
				userStatus.setStatus(UserStatus.IN_STATUS);
				userStatusService.update(userStatus);
				userIds.add(battleWaitRoomMember.getUserId());
			}else{
				int status = battleWaitRoomMember.getStatus();
				if(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS){
					downLineMembers.add(battleWaitRoomMember);
				}
			}
		}
		
		if(downLineMembers.size()>0){
			for(BattleWaitRoomMember downLineMember:downLineMembers){
				downLineMember.setStatus(BattleWaitRoomMember.OUT_STATUS);
				battleWaitRoomMemberService.update(downLineMember);
				battleWaitRoomSocketService.waitRoomMemberPublish(downLineMember, userIds);
			}
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorCode(0);
			return resultVo;
		}
		
		
		int num = 0;
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			if(battleWaitRoomMember.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS){
				num++;
			}
		}
		if(num<battleWaitRoom.getMinNum()){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorCode(1);
			return resultVo;
		}
		
		
		battleWaitRoom.setStatus(BattleWaitRoom.IN_STATUS);
		
		battleWaitRoomService.update(battleWaitRoom);
		
		
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
		battleRoomFactory.init(battleWaitRoom.getGroupId(),userIds,BattleRoomVo.ROOM_TYPE,data);
		
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.END_STATUS);
			battleWaitRoomMember.setEndContent("比赛已经开始");
			battleWaitRoomMemberService.update(battleWaitRoomMember);
		}
		
		
		
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
		battleWaitRoomMember.setIsEnd(1);
		battleWaitRoomMember.setEndContent("你已经被踢出房间");
		battleWaitRoomMemberService.update(battleWaitRoomMember);
		
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoomMember.getRoomId());
		List<String> userIds = new ArrayList<>();
		for(BattleWaitRoomMember battleWaitRoomMember2:battleWaitRoomMembers){
			userIds.add(battleWaitRoomMember2.getUserId());
		}
		
		battleWaitRoomSocketService.kickOutPublish(battleWaitRoomMember);
		
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
		
		for(BattleWaitRoomMember battleWaitRoomMember:battleWaitRoomMembers){
			if(!webSocketManager.isOpen(battleWaitRoomMember.getToken())&&
					(battleWaitRoomMember.getStatus()==BattleWaitRoomMember.FREE_STATUS||battleWaitRoomMember.getStatus()==BattleWaitRoomMember.READY_STATUS)){
				battleWaitRoomMember.setStatus(BattleWaitRoomMember.END_STATUS);
				battleWaitRoomMemberService.update(battleWaitRoomMember);
			}
		}
	
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
		Integer num = battleWaitRoom.getNum();
		if(battleWaitRoomMember==null){
			battleWaitRoomMember = new BattleWaitRoomMember();
			battleWaitRoomMember.setImgUrl(userInfo.getHeadimgurl());
			battleWaitRoomMember.setNickname(userInfo.getNickname());
			battleWaitRoomMember.setRoomId(battleWaitRoom.getId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMember.setUserId(userInfo.getId());
			battleWaitRoomMember.setIsOwner(0);
			battleWaitRoomMember.setToken(userInfo.getToken());
			battleWaitRoomMember.setIsEnd(0);
			battleWaitRoomMemberService.add(battleWaitRoomMember);
		}else{
			if(battleWaitRoomMember.getIsEnd()!=null&&battleWaitRoomMember.getIsEnd().intValue()==1){
				battleWaitRoomSocketService.kickOutPublish(battleWaitRoomMember);
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				resultVo.setData(null);
				return resultVo;
			}
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMemberService.update(battleWaitRoomMember);
		}
		
		List<BattleWaitRoomMember> ownerMembers = battleWaitRoomMemberService.findAllByRoomIdAndIsOwner(battleWaitRoomMember.getRoomId(),1);
		
		BattleWaitRoomMember ownerMember = null;
		
		if(ownerMembers.size()>0){
			ownerMember = ownerMembers.get(0);
		}else{
			
		}
		
		if(ownerMember==null||((ownerMember.getStatus().intValue()==BattleWaitRoomMember.FREE_STATUS||ownerMember.getStatus().intValue()==BattleWaitRoomMember.READY_STATUS)&&webSocketManager.isOpen(ownerMember.getToken()))){
			
		}else{
			
			List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(battleWaitRoomMember.getRoomId());
			for(BattleWaitRoomMember switchBattleWaitRoomMember:battleWaitRoomMembers){
				int status = switchBattleWaitRoomMember.getStatus();
				if(switchBattleWaitRoomMember.getIsOwner().intValue()==0&&
						(status==BattleWaitRoomMember.FREE_STATUS||status==BattleWaitRoomMember.READY_STATUS)){
					
					try {
						battleWaitRoomSocketService.changeOwnerPublish(switchBattleWaitRoomMember);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				}
			}
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
	
		
		num++;
		battleWaitRoom.setNum(num);
		battleWaitRoomService.update(battleWaitRoom);
		
		if(num>=battleWaitRoom.getMaxNum()){
			battleWaitRoom.setIsFull(1);
			battleWaitRoomService.update(battleWaitRoom);
		}
		battleWaitRoomSocketService.waitRoomMemberPublish(battleWaitRoomMember, userIds);
		
		System.out.println("...........into3");
		
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
		List<BattleWaitRoomMember> battleWaitRoomMembers = battleWaitRoomMemberService.findAllByRoomId(id);
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		BattleWaitRoomMember battleWaitRoomMember = battleWaitRoomMemberService.findOneByRoomIdAndUserId(id, userInfo.getId());
		
		for(BattleWaitRoomMember ownerMember:battleWaitRoomMembers){
			if(ownerMember.getIsOwner().intValue()==1){
				battleWaitRoomMember = battleWaitRoomHandle.switchOwner(ownerMember, battleWaitRoomMember, battleWaitRoomMembers);
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(true);
				resultVo.setData(battleWaitRoomMember);
				return resultVo;
			}
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(false);
		return resultVo;
	}
	
	
	@RequestMapping(value="searchRoom")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo searchRoom(HttpServletRequest httpServletRequest)throws Exception{
		
		
		
		
		Pageable pageable = new PageRequest(0, 1);
		String searchKey = httpServletRequest.getParameter("searchKey");
		Page<BattleWaitRoom> battleWaitRoomPage = battleWaitRoomService.findAllByStatusAndSearchKeyAndIsPublicAndIsFull(BattleWaitRoom.FREE_STATUS, searchKey,1,0,pageable);
		
		List<BattleWaitRoom> battleWaitRooms = battleWaitRoomPage.getContent();
	
		
		
		if(battleWaitRooms==null||battleWaitRooms.size()==0){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		Random r = new Random();
		
		if(battleWaitRooms.size()<2&&r.nextInt(10)<2){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		
		
		Integer roomNum = battleWaitRooms.size();
		
		Integer index = r.nextInt(roomNum);
		BattleWaitRoom battleWaitRoom = battleWaitRooms.get(index);
		
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
		
		
		List<BattleWaitRoom> battleWaitRooms = battleWaitRoomService.findAllByOwnerIdAndSearchKeyAndStatus(userInfo.getId(),searchKey,BattleWaitRoom.FREE_STATUS);
		BattleWaitRoom battleWaitRoom = null;
		if(battleWaitRooms==null||battleWaitRooms.size()==0){
			battleWaitRoom = new BattleWaitRoom();
			List<BattleWaitRoomGroup> battleWaitRoomGroups = battleWaitRoomGroupService.findAllByIsDefault(1);
			BattleWaitRoomGroup battleWaitRoomGroup = battleWaitRoomGroups.get(0);
			
			battleWaitRoom.setGroupId(battleWaitRoomGroup.getGroupId());
			battleWaitRoom.setGroupName(battleWaitRoomGroup.getName());
		
			BattleWaitRoomNum battleWaitRoomNum = battleWaitRoomNumService.findOneBySearchKey(searchKey);
			if(battleWaitRoomNum==null){
				List<BattleWaitRoomNum> battleWaitRoomNums = battleWaitRoomNumService.findAllByIsDefault(1);
				battleWaitRoomNum = battleWaitRoomNums.get(0);
			}
			battleWaitRoom.setMaxNum(battleWaitRoomNum.getMaxNum());
			battleWaitRoom.setMinNum(battleWaitRoomNum.getMinNum());
			
			battleWaitRoom.setOwnerId(userInfo.getId());
			battleWaitRoom.setStatus(BattleWaitRoom.FREE_STATUS);
			battleWaitRoom.setIsPublic(1);
			battleWaitRoom.setIsFull(0);
			battleWaitRoom.setNum(0);
			battleWaitRoom.setSearchKey(searchKey);
			
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
			battleWaitRoomMember.setIsEnd(0);
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
