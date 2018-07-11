package com.battle.api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.battle.domain.BattleAccountResult;
import com.battle.domain.BattleDan;
import com.battle.domain.BattleDanPoint;
import com.battle.domain.BattleDanUser;
import com.battle.domain.BattleWait;
import com.battle.filter.element.CurrentAccountResultFilter;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleDanPointService;
import com.battle.service.BattleDanService;
import com.battle.service.BattleDanUserService;
import com.battle.service.BattleWaitService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/dan")
public class BattleDanApi {

	@Autowired
	private BattleDanPointService battleDanPointService;
	
	@Autowired
	private BattleDanService battleDanService;
	
	@Autowired
	private BattleDanUserService battleDanUserService;
	
	@Autowired
	private BattleWaitService battleWaitService;
	
	final static Logger logger = LoggerFactory.getLogger(BattleDanApi.class);
	
	
	
	@RequestMapping(value="accountResultInfo")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=CurrentAccountResultFilter.class)
	public Object accountResultInfo(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		BattleAccountResult battleAccountResult = sessionManager.getObject(BattleAccountResult.class);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("nickname", userInfo.getNickname());
		data.put("headimgurl", userInfo.getHeadimgurl());
		data.put("exp", battleAccountResult.getExp());
		data.put("level", battleAccountResult.getLevel());
		
		data.put("danName", battleAccountResult.getDanName());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		resultVo.setData(data);
		
		return resultVo;
	}
	
	@RequestMapping(value="battleDanSign")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object battleDanSign(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		String danId = httpServletRequest.getParameter("danId");
		
		if(CommonUtil.isEmpty(danId)){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			
			resultVo.setErrorMsg("缺少参数");
			
			return resultVo;
		}
		
		BattleDan battleDan = battleDanService.findOne(danId);
		
		if(battleDan==null){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			
			resultVo.setErrorMsg("输入的是无效参数");
			
			return resultVo;
		}
		
		
		List<BattleDanUser> battleDanUsers = battleDanUserService.findAllByDanIdAndUserId(danId, userInfo.getId());
		
		BattleDanUser battleDanUser = null;
		if(battleDanUsers.size()>0){
			battleDanUser = battleDanUsers.get(0);
			if(battleDanUsers.size()>1){
				BattleDanUser battleDanUser2 = battleDanUsers.get(1);
				battleDanUser2.setIsDel(1);
				battleDanUserService.update(battleDanUser2);
			}
		}
		
		
		if(battleDanUser==null){
			battleDanUser = new BattleDanUser();
			battleDanUser.setStatus(BattleDanUser.STATUS_FREE);
			battleDanUser.setDanId(battleDan.getId());
			battleDanUser.setDanName(battleDan.getName());
			battleDanUser.setImgUrl(battleDan.getImgUrl());
			battleDanUser.setLevel(battleDan.getLevel());
			battleDanUser.setPointId(battleDan.getPointId());
			
			battleDanUser.setBattleId(battleDan.getBattleId());
			
			battleDanUser.setPeriodId(battleDan.getPeriodId());
			
			battleDanUser.setPlaces(battleDan.getPlaces());
			
			battleDanUser.setUserId(userInfo.getId());
			
			battleDanUser.setSign1BeanCost(battleDan.getSign1BeanCost());
			
			battleDanUser.setSign2BeanCost(battleDan.getSign2BeanCost());
			
			battleDanUser.setSign3BeanCost(battleDan.getSign3BeanCost());
			
			battleDanUser.setSign4BeanCost(battleDan.getSign4BeanCost());
			
			battleDanUser.setIsSign(0);
			
			battleDanUser.setSignCount(0);
			
			battleDanUser.setScoreGogal(battleDan.getScoreGogal());
			
			battleDanUser.setMaxNum(battleDan.getMaxNum());
			
			battleDanUser.setTimeLong(battleDan.getTimeLong());
			
			battleDanUser.setMinNum(battleDan.getMinNum());
			
			battleDanUser.setLoveCount(battleDan.getLoveCount());
			
			battleDanUser.setIsDel(0);
			
			battleDanUserService.add(battleDanUser);
			
			battleDanUsers.add(battleDanUser);
		}
		
		Integer signCount = battleDanUser.getSignCount();
		
		List<BattleWait> battleWaits = battleWaitService.findAllByBattleIdAndStatus(battleDan.getBattleId(),BattleWait.CALL_STATUS);
		
		BattleWait battleWait;
		if(battleWaits==null||battleWaits.size()==0){
			
			battleWait = new BattleWait();
			battleWait.setActDelay(2000);
			battleWait.setBattleId(battleDan.getBattleId());
			battleWait.setIsPrepareStart(0);
			battleWait.setMaxinum(battleDan.getMaxNum());
			battleWait.setMininum(battleDan.getMinNum());
			battleWait.setNum(0);
			battleWait.setPeriodId(battleDan.getPeriodId());
			battleWait.setStatus(BattleWait.CALL_STATUS);
			battleWait.setDanId(battleDan.getId());
			battleWait.setGroupId(battleDan.getGroupId());
			battleWait.setPassNum(battleDan.getPlaces());
			battleWaitService.add(battleWait);
		}else{
			battleWait = battleWaits.get(0);
		}
		
		
		battleDanUser.setSignCount(battleDanUser.getSignCount()+1);
		battleDanUser.setIsSign(1);
		
		battleDanUserService.update(battleDanUser);
		
		//accountService.update(account);
		
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		Map<String, Object> data = new HashMap<>();
		data.put("waitId", battleWait.getId());
		data.put("danUserId", battleDanUser.getId());
		
		resultVo.setData(data);
		
		return resultVo;
	}
	
	@RequestMapping(value="list")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=CurrentAccountResultFilter.class)
	public Object list(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		List<BattleDanPoint> battleDanPoints = battleDanPointService.findAllByIsRun(1);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		BattleDanPoint battleDanPoint = null;
		if(battleDanPoints!=null&&battleDanPoints.size()==1){
			battleDanPoint = battleDanPoints.get(0);
		}else if(battleDanPoints.size()>0){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("关卡有多条记录并发");
			return resultVo;
		}else{
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("关卡没卡记录");
			return resultVo;
		}
	
		
		List<BattleDan> battleDans = battleDanService.findAllByPointIdOrderByLevelAsc(battleDanPoint.getId());
		
		List<Map<String, Object>> list = new ArrayList<>();
		
		BattleAccountResult battleAccountResult = sessionManager.getObject(BattleAccountResult.class);
		
		List<BattleDanUser> battleDanUsers = battleDanUserService.findAllByUserIdAndPointIdOrderByLevelAsc(userInfo.getId(), battleDanPoint.getId());
		Map<String, BattleDanUser> battleDanUserMap = new HashMap<>();
		
		for(BattleDanUser battleDanUser:battleDanUsers){
			battleDanUserMap.put(battleDanUser.getDanId(), battleDanUser);
		}
		
		for(BattleDan battleDan:battleDans){
			Map<String, Object> map = new HashMap<>();
			map.put("id", battleDan.getId());
			map.put("danId", battleDan.getId());
			map.put("battleId", battleDan.getBattleId());
			map.put("imgUrl", battleDan.getImgUrl());
			map.put("level", battleDan.getLevel());
			map.put("maxNum", battleDan.getMaxNum());
			map.put("minNum", battleDan.getMinNum());
			map.put("name", battleDan.getName());
			map.put("danName", battleDan.getName());
			map.put("periodId", battleDan.getPeriodId());
			map.put("places", battleDan.getPlaces());
			map.put("pointId", battleDan.getPointId());
			map.put("scoreGogal", battleDan.getScoreGogal());
			map.put("sign1BeanCost", battleDan.getSign1BeanCost());
			map.put("sign2BeanCost", battleDan.getSign2BeanCost());
			map.put("sign3BeanCost", battleDan.getSign3BeanCost());
			map.put("sign4BeanCost", battleDan.getSign4BeanCost());
			map.put("timeLong", battleDan.getTimeLong());
			if(battleDan.getLevel()<battleAccountResult.getLevel()){
				map.put("status", BattleDan.STATUS_SUCCESS);
			}else if(battleDan.getLevel()==battleAccountResult.getLevel()){
				map.put("status", BattleDan.STATUS_IN);
			}else{
				map.put("status", BattleDan.STATUS_FREE);
			}
			
			BattleDanUser battleDanUser = battleDanUserMap.get(battleDan.getId());
			
			if(battleDanUser==null){
				map.put("costBean", battleDan.getSign1BeanCost());
				map.put("signCount", 0);
				map.put("isSign", 0);
			}else if(battleDanUser.getSignCount()==0){
				map.put("costBean", battleDan.getSign1BeanCost());
				/*map.put("signCount", battleDanUser.getSignCount());
				map.put("isSign", 0);*/
				map.put("signCount", 0);
				map.put("isSign", 0);
			}else if(battleDanUser.getSignCount()==1){
				map.put("costBean", battleDan.getSign2BeanCost());
				/*map.put("signCount", battleDanUser.getSignCount());
				map.put("isSign", 1);*/
				map.put("signCount", 0);
				map.put("isSign", 0);
			}else if(battleDanUser.getSignCount()==2){
				map.put("costBean", battleDan.getSign3BeanCost());
				/*map.put("signCount", battleDanUser.getSignCount());
				map.put("isSign", 1);*/
				map.put("signCount", 0);
				map.put("isSign", 0);
			}else if(battleDanUser.getSignCount()>=3){
				map.put("costBean", battleDan.getSign4BeanCost());
				/*map.put("signCount", battleDanUser.getSignCount());
				map.put("isSign", 1);*/
				map.put("signCount", 0);
				map.put("isSign", 0);
			}
			
			list.add(map);
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(list);
		
		return resultVo;
	}
}
