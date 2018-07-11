package com.battle.api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleDan;
import com.battle.domain.BattleWait;
import com.battle.domain.BattleWaitUser;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.vo.BattleRewardVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleDanService;
import com.battle.service.BattleWaitService;
import com.battle.service.BattleWaitUserService;
import com.battle.socket.service.BattleWaitSocketService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;


@Controller
@RequestMapping(value="/api/battle/battleWait")
public class BattleWaitApi {
	@Autowired
	private BattleWaitUserService battleWaitUserService;
	
	@Autowired
	private BattleWaitSocketService battleWaitSocketService;
	
	@Autowired
	private BattleWaitService battleWaitService;
	
	@Autowired
	private BattleRoomFactory battleRoomFactory;
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	private BattleDanService battleDanService;
	
	
	@RequestMapping(value="waitUsers")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object waitUsers(HttpServletRequest httpServletRequest)throws Exception{
		
		String waitId = httpServletRequest.getParameter("waitId");
		
		
		List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitId(waitId);
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitUsers);
		return resultVo;
	}
	
	@RequestMapping(value="into")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object into(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String waitId = httpServletRequest.getParameter("waitId");
		String danUserId = httpServletRequest.getParameter("danUserId");
		BattleWaitUser battleWaitUser = battleWaitUserService.findOneByWaitIdAndUserId(waitId,userInfo.getId());
		if(battleWaitUser==null){
			
			battleWaitUser = new BattleWaitUser();
			battleWaitUser.setStatus(BattleWaitUser.INTO_STATUS);
			battleWaitUser.setUserId(userInfo.getId());
			battleWaitUser.setWaitId(waitId);
			battleWaitUser.setNickname(userInfo.getNickname());
			battleWaitUser.setImgUrl(userInfo.getHeadimgurl());
			if(CommonUtil.isNotEmpty(danUserId)){
				battleWaitUser.setDanUserId(danUserId);
			}
			battleWaitUserService.add(battleWaitUser);
			battleWaitSocketService.waitPublish(battleWaitUser);
		}else{
			
			if(battleWaitUser.getStatus().intValue()!=BattleWaitUser.READY_STATUS){
				battleWaitUser.setStatus(BattleWaitUser.INTO_STATUS);
				battleWaitUserService.update(battleWaitUser);
			}
			battleWaitSocketService.waitPublish(battleWaitUser);
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitUser);
		return resultVo;
	}
	
	
	
	@RequestMapping(value="ready")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object ready(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String waitId = httpServletRequest.getParameter("waitId");
		
		BattleWaitUser battleWaitUser = battleWaitUserService.findOneByWaitIdAndUserId(waitId,userInfo.getId());
		
		battleWaitUser.setStatus(BattleWaitUser.READY_STATUS);
		
		battleWaitUserService.update(battleWaitUser);
		
		battleWaitSocketService.waitPublish(battleWaitUser);
		
		final BattleWait battleWait = battleWaitService.findOne(waitId);
		Integer num = battleWait.getNum();
		if(num==null){
			num = 0;
		}
		num++;
		battleWait.setNum(num);
		
		battleWaitService.update(battleWait);
		if(num>=battleWait.getMininum()&&battleWait.getIsPrepareStart()==0){
			battleWait.setIsPrepareStart(1);
			battleWait.setStatus(BattleWait.START_STATUS);
			battleWaitService.update(battleWait);
			/*****
			 * 这里需要处理
			 */
			
			
			scheduledExecutorService.schedule(new Runnable() {
				
				@Override
				public void run() {
					List<String> userIds = new ArrayList<>();
					List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitIdAndStatus(battleWait.getId(), BattleWaitUser.READY_STATUS);
					for(BattleWaitUser battleWaitUser2:battleWaitUsers){
						userIds.add(battleWaitUser2.getUserId());
					}
					Map<String, Object> data = new HashMap<>();
					List<Map<String, Object>> users = new ArrayList<>();
					for(BattleWaitUser battleWaitUser:battleWaitUsers){
						Map<String, Object> user = new HashMap<>();
						user.put("userId", battleWaitUser.getUserId());
						user.put("danUserId", battleWaitUser.getDanUserId());
						user.put("danId",battleWait.getDanId());
						
						users.add(user);
					}
					data.put("danId",battleWait.getDanId());
					data.put("users", users);
					data.put("passNum", battleWait.getPassNum());
					
					BattleDan battledan = battleDanService.findOne(battleWait.getDanId());
				
					List<BattleRewardVo> battleRewards = new ArrayList<>();
					if(battledan.getRewardBean1()!=null&&battledan.getRewardBean1()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(1);
						battleRewardVo.setRewardBean(battledan.getRewardBean1());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					
					if(battledan.getRewardBean2()!=null&&battledan.getRewardBean2()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(2);
						battleRewardVo.setRewardBean(battledan.getRewardBean2());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean3()!=null&&battledan.getRewardBean3()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(3);
						battleRewardVo.setRewardBean(battledan.getRewardBean3());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean4()!=null&&battledan.getRewardBean4()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(4);
						battleRewardVo.setRewardBean(battledan.getRewardBean4());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean5()!=null&&battledan.getRewardBean5()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(5);
						battleRewardVo.setRewardBean(battledan.getRewardBean5());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean6()!=null&&battledan.getRewardBean6()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(6);
						battleRewardVo.setRewardBean(battledan.getRewardBean6());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean7()!=null&&battledan.getRewardBean7()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(7);
						battleRewardVo.setRewardBean(battledan.getRewardBean7());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean8()!=null&&battledan.getRewardBean8()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(8);
						battleRewardVo.setRewardBean(battledan.getRewardBean8());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean9()!=null&&battledan.getRewardBean9()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(9);
						battleRewardVo.setRewardBean(battledan.getRewardBean9());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					if(battledan.getRewardBean10()!=null&&battledan.getRewardBean10()>0){
						BattleRewardVo battleRewardVo = new BattleRewardVo();
						battleRewardVo.setRank(10);
						battleRewardVo.setRewardBean(battledan.getRewardBean10());
						battleRewardVo.setRewardLove(0);
						battleRewards.add(battleRewardVo);
					}
					
					data.put("rewards", battleRewards);
					battleRoomFactory.init(battleWait.getGroupId(),userIds,BattleRoomVo.DAN_TYPE,data);
					
				}
			}, 4, TimeUnit.SECONDS);
			

		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleWaitUser);
		return resultVo;
	}
	
	
	@RequestMapping(value="out")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object out(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String waitId = httpServletRequest.getParameter("waitId");
		
		BattleWait battleWait = battleWaitService.findOne(waitId);
		
		BattleWaitUser battleWaitUser = battleWaitUserService.findOneByWaitIdAndUserIdAndStatus(waitId,userInfo.getId(),BattleWaitUser.READY_STATUS);
		
		if(battleWaitUser!=null){
			Integer num = battleWait.getNum();
			if(CommonUtil.isNotEmpty(num)){
				num--;
			}
			
			battleWait.setNum(num);
			battleWaitUser.setStatus(BattleWaitUser.OUT_STATUS);
			
			battleWaitUserService.update(battleWaitUser);
			
			battleWaitSocketService.waitPublish(battleWaitUser);
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(true);
			resultVo.setData(battleWaitUser);
			return resultVo;
		}else{
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		
	}
	
}
