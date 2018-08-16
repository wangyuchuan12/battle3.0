package com.battle.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleRedpack;
import com.battle.domain.BattleRedpackDistribution;
import com.battle.domain.BattleRedpackMember;
import com.battle.domain.BattleRedpackStorage;
import com.battle.domain.BattleRedpackStorageRecord;
import com.battle.domain.BattleRedpackTask;
import com.battle.domain.BattleRedpackTaskMember;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleRedpackDistributionService;
import com.battle.service.BattleRedpackService;
import com.battle.service.BattleRedpackStorageRecordService;
import com.battle.service.BattleRedpackStorageService;
import com.battle.service.BattleRedpackTaskMemberService;
import com.battle.service.BattleRedpackTaskService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.service.AccountService;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.util.DistributionAmountUtil;
import com.wyc.common.util.MySimpleDateFormat;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/redpack")
public class BattleRedpackApi {
	
	@Autowired
	private BattleRedpackService battleRedpackService;
	
	@Autowired
	private BattleRedpackDistributionService battleRedpackDistributionService;
	
	@Autowired
	private BattleRedpackTaskMemberService battleRedpackTaskMemberService;
	
	@Autowired
	private BattleRedpackTaskService battleRedpackTaskService;
	
	@Autowired
	private MySimpleDateFormat mySimpleDateFormat;
	
	@Autowired
	private BattleRedpackStorageService battleRedpackStorageService;
	
	@Autowired
	private BattleRedpackStorageRecordService battleRedpackStorageRecordService;
	
	
	
	@RequestMapping(value="receive")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo receive(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		BattleRedpack battleRedpack = battleRedpackService.findOne(id);
		int remainNum = battleRedpack.getRemainNum();
		BigDecimal remainAmount = battleRedpack.getRemainAmount();
		
		if(remainNum>0){
			List<BattleRedpackDistribution> battleRedpackDistributions = battleRedpackDistributionService.findAllByRedpackIdAndStatusOrderByReceiveAtDesc(id, BattleRedpackDistribution.UN_RECEIVE_STATUS);
			
			BattleRedpackDistribution battleRedpackDistribution = null;
			if(battleRedpackDistributions.size()>0){
				battleRedpackDistribution = battleRedpackDistributions.get(0);
			}
			
			if(battleRedpackDistribution==null){
				remainNum = 0;
				battleRedpack.setRemainAmount(new BigDecimal(0));
				battleRedpack.setRemainNum(0);
				battleRedpack.setStatus(BattleRedpack.END_STATUS);
				
				battleRedpackService.update(battleRedpack);
				
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				return resultVo;
			}else{
				battleRedpackDistribution.setNickname(userInfo.getNickname());
				battleRedpackDistribution.setReceiveAt(new DateTime());
				battleRedpackDistribution.setStatus(BattleRedpackDistribution.RECEIVED_STATUS);
				battleRedpackDistribution.setUserId(userInfo.getId());
				battleRedpackDistribution.setUserImg(userInfo.getHeadimgurl());
				battleRedpackDistributionService.update(battleRedpackDistribution);
				remainNum--;
				remainAmount = remainAmount.subtract(battleRedpackDistribution.getAmount());
				
				
			}
			battleRedpack.setRemainNum(remainNum);
			battleRedpack.setRemainAmount(remainAmount);
			
			if(remainNum==0){
				battleRedpack.setRemainAmount(new BigDecimal(0));
				battleRedpack.setRemainNum(0);
				battleRedpack.setStatus(BattleRedpack.END_STATUS);
			}
			
			battleRedpackService.update(battleRedpack);
			
			BattleRedpackStorage battleRedpackStorage = battleRedpackStorageService.findOneByUserId(userInfo.getId());
			battleRedpackStorage.setStatus(BattleRedpackStorage.COMPLEATE_STATUS);
			battleRedpackStorage.setRedPackId("");
			battleRedpackStorageService.update(battleRedpackStorage);
			
			Map<String, Object> data = new HashMap<>();
			data.put("amount", battleRedpackDistribution.getAmount());
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(true);
			return resultVo;
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(false);
		return resultVo;
	}
	
	@RequestMapping(value="list")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo list(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		List<BattleRedpack> battleRedpacks = battleRedpackService.findAllByStatus(BattleRedpack.IN_STATUS);
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRedpacks);
		
		return resultVo;
		
	}
	
	
	@RequestMapping(value="randomRedpack")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo randomRedpack(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		BattleRedpackStorage battleRedpackStorage = battleRedpackStorageService.findOneByUserId(userInfo.getId());
		
		System.out.println("*********************************battleRedpackStorage:"+battleRedpackStorage);
		if(battleRedpackStorage==null){
			
			battleRedpackStorage = new BattleRedpackStorage();
			battleRedpackStorage.setStatus(BattleRedpackStorage.FREE_STATUS);
			battleRedpackStorage.setUserId(userInfo.getId());
			
			battleRedpackStorageService.add(battleRedpackStorage);
		}
		
		
		System.out.println("..........................battleRedpackStorage.getStatus().intValue:"+battleRedpackStorage.getStatus().intValue());
		if(battleRedpackStorage.getStatus().intValue()==BattleRedpackStorage.FREE_STATUS.intValue()){
			List<BattleRedpack> battleRedpacks = battleRedpackService.findAllByStatus(BattleRedpack.IN_STATUS);
			if(battleRedpacks.size()>0){
				
				
				for(BattleRedpack battleRedpack:battleRedpacks){

					battleRedpackStorage.setRedPackId(battleRedpack.getId());
					battleRedpackStorage.setStatus(BattleRedpackStorage.IN_STATUS);
					
					battleRedpackStorageService.update(battleRedpackStorage);
					
					BattleRedpackStorageRecord battleRedpackStorageRecord = new BattleRedpackStorageRecord();
					battleRedpackStorageRecord.setRedPackId(battleRedpack.getId());
					battleRedpackStorageRecord.setUserId(userInfo.getId());
					battleRedpackStorageRecordService.add(battleRedpackStorageRecord);
					
					Map<String, Object> data = new HashMap<>();
					data.put("redPackId", battleRedpack.getId());
					ResultVo resultVo = new ResultVo();
					resultVo.setSuccess(true);
					resultVo.setData(data);
					return resultVo;
				}
			}else{
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				return resultVo;
			}
		}else if(battleRedpackStorage.getStatus().intValue()==BattleRedpackStorage.IN_STATUS.intValue()){
			String redPackId = battleRedpackStorage.getRedPackId();
			BattleRedpack battleRedpack = battleRedpackService.findOne(redPackId);
			
			
			if(battleRedpack.getStatus().intValue()==BattleRedpack.IN_STATUS.intValue()){
				Map<String, Object> data = new HashMap<>();
				data.put("redPackId", battleRedpack.getId());
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(true);
				resultVo.setData(data);
				return resultVo;
			}
		}
			
		List<BattleRedpack> battleRedpacks = battleRedpackService.findAllByStatus(BattleRedpack.IN_STATUS);
		for(BattleRedpack redpack:battleRedpacks){
			List<BattleRedpackStorageRecord> battleRedpackStorageRecords = battleRedpackStorageRecordService.
					findAllByRedPackAndUserId(redpack.getId(),userInfo.getId());
			
			if(battleRedpackStorageRecords.size()>0){
				
			}else{
				
				battleRedpackStorage.setStatus(BattleRedpackStorage.IN_STATUS);
				battleRedpackStorage.setRedPackId(redpack.getId());
				
				battleRedpackStorageService.update(battleRedpackStorage);
				
				BattleRedpackStorageRecord battleRedpackStorageRecord = new BattleRedpackStorageRecord();
				battleRedpackStorageRecord.setRedPackId(redpack.getId());
				battleRedpackStorageRecord.setUserId(userInfo.getId());
				
				battleRedpackStorageRecordService.add(battleRedpackStorageRecord);
				
				
				Map<String, Object> data = new HashMap<>();
				data.put("redPackId", redpack.getId());
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(true);
				resultVo.setData(data);
				return resultVo;
				
			
			}
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(false);
		return resultVo;
	}
	
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo info(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		String id = httpServletRequest.getParameter("id");
		
		BattleRedpack redpack = battleRedpackService.findOne(id);
		
		List<BattleRedpackDistribution> battleRedpackDistributions = battleRedpackDistributionService.findAllByRedpackIdAndStatusOrderByReceiveAtDesc(id,BattleRedpackDistribution.RECEIVED_STATUS);

		List<BattleRedpackTask> battleRedpackTasks = battleRedpackTaskService.findAllByRedpackIdOrderByIndexAsc(id);
		
		List<BattleRedpackTaskMember> battleRedpackTaskMembers = new ArrayList<>();
		
		for(BattleRedpackTask battleRedpackTask:battleRedpackTasks){
			BattleRedpackTaskMember battleRedpackTaskMember = battleRedpackTaskMemberService.findOneByTaskIdAndUserId(battleRedpackTask.getId(),userInfo.getId());
			if(battleRedpackTaskMember==null){
				battleRedpackTaskMember = new BattleRedpackTaskMember();
				battleRedpackTaskMember.setImgUrl(userInfo.getHeadimgurl());
				battleRedpackTaskMember.setNickname(userInfo.getNickname());
				battleRedpackTaskMember.setStatus(BattleRedpackTaskMember.IN_STATUS);
				battleRedpackTaskMember.setTaskId(battleRedpackTask.getId());
				battleRedpackTaskMember.setUserId(userInfo.getId());
				battleRedpackTaskMember.setName(battleRedpackTask.getName());
				battleRedpackTaskMember.setBeanNum(battleRedpackTask.getBeanNum());
				battleRedpackTaskMember.setRankId(battleRedpackTask.getRankId());
				battleRedpackTaskMemberService.add(battleRedpackTaskMember);
			}
			battleRedpackTaskMembers.add(battleRedpackTaskMember);
		}
		
		
		
		Map<String, Object> redpackMap = new HashMap<>();
		
		redpackMap.put("id", redpack.getId());
		redpackMap.put("amount", redpack.getAmount());
		redpackMap.put("imgUrl", redpack.getImgUrl());
		redpackMap.put("isPublic", redpack.getIsPublic());
		redpackMap.put("linkImgUrl", redpack.getLinkImgUrl());
		redpackMap.put("name", redpack.getName());
		redpackMap.put("num", redpack.getNum());
		redpackMap.put("remainAmount", redpack.getRemainAmount());
		redpackMap.put("remainNum", redpack.getRemainNum());
		redpackMap.put("status", redpack.getStatus());
		
		int isReceive = 0;
		
		List<Map<String,Object>> taskMaps = new ArrayList<>();
		for(BattleRedpackTaskMember battleRedpackTaskMember:battleRedpackTaskMembers){
			
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.put("id", battleRedpackTaskMember.getId());
			taskMap.put("beanNum", battleRedpackTaskMember.getBeanNum());
			taskMap.put("imgUrl", battleRedpackTaskMember.getImgUrl());
			taskMap.put("name", battleRedpackTaskMember.getName());
			taskMap.put("nickname", battleRedpackTaskMember.getNickname());
			taskMap.put("rankId", battleRedpackTaskMember.getRankId());
			taskMap.put("status", battleRedpackTaskMember.getStatus());
			taskMap.put("taskId", battleRedpackTaskMember.getTaskId());
			taskMap.put("userId", battleRedpackTaskMember.getUserId());
			taskMaps.add(taskMap);
		}
		
		
		List<Map<String, Object>> distributionMaps = new ArrayList<>();
		
		for(BattleRedpackDistribution battleRedpackDistribution:battleRedpackDistributions){
			Map<String, Object> battleRedpackDistributionMap = new HashMap<>();
			battleRedpackDistributionMap.put("id", battleRedpackDistribution.getId());
			battleRedpackDistributionMap.put("amount", battleRedpackDistribution.getAmount());
			battleRedpackDistributionMap.put("nickname", battleRedpackDistribution.getNickname());
			battleRedpackDistributionMap.put("receiveAt", mySimpleDateFormat.format(battleRedpackDistribution.getReceiveAt().toDate()));
			battleRedpackDistributionMap.put("userImg", battleRedpackDistribution.getUserImg());
			distributionMaps.add(battleRedpackDistributionMap);
			
			if(CommonUtil.isNotEmpty(battleRedpackDistribution.getUserId())){
				if(userInfo.getId().equals(battleRedpackDistribution.getUserId())){
					isReceive = 1;
					redpackMap.put("receiveAmount", battleRedpackDistribution.getAmount());
				}
			}
		}
		
		redpackMap.put("isReceive",isReceive);
		
		Map<String, Object> data = new HashMap<>();
		data.put("redpack", redpackMap);
		data.put("distributions", distributionMaps);
		data.put("tasks", taskMaps);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
		
	}
	
}
