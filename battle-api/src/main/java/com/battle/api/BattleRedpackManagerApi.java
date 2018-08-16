package com.battle.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleFactory;
import com.battle.domain.BattleRank;
import com.battle.domain.BattleRedpack;
import com.battle.domain.BattleRedpackDistribution;
import com.battle.domain.BattleRedpackTask;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleFactoryService;
import com.battle.service.BattleRankService;
import com.battle.service.BattleRedpackDistributionService;
import com.battle.service.BattleRedpackService;
import com.battle.service.BattleRedpackTaskService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.DistributionAmountUtil;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle/manager/redpack")
public class BattleRedpackManagerApi {
	
	public static void main(String[]args){
		DistributionAmountUtil distributionAmountUtil = new DistributionAmountUtil();
		
		System.out.println(distributionAmountUtil.splitRedPackets(1000, 2, 1, 600));
	}
	@Autowired
	private BattleRedpackDistributionService battleRedpackDistributionService;
	
	@Autowired
	private BattleRedpackService battleRedpackService;
	
	@Autowired
	private BattleRankService battleRankService;
	
	
	@Autowired
	private BattleRedpackTaskService battleRedpackTaskService;
	
	@Autowired
	private BattleFactoryService battleFactoryService;
	
	@RequestMapping(value="publicRanks")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo publicRanks(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		List<BattleFactory> battleFactories = battleFactoryService.findAllByUserId(userInfo.getId());
		
		List<String> factoryIds = new ArrayList<>();
		
		for(BattleFactory battleFactory:battleFactories){
			factoryIds.add(battleFactory.getId());
		}
		
		List<BattleRank> battleRanks = new ArrayList<>();
		
		if(factoryIds!=null&&factoryIds.size()>0){
			battleRanks = battleRankService.findAllByFactoryIdIn(factoryIds);
		}
		
	
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRanks);
		return resultVo;
	}
	
	
	@RequestMapping(value="tasks")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo tasks(HttpServletRequest httpServletRequest)throws Exception{
		String redPackId = httpServletRequest.getParameter("redPackId");
		
		List<BattleRedpackTask> battleRedpackTasks = battleRedpackTaskService.findAllByRedpackIdOrderByIndexAsc(redPackId);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRedpackTasks);
		
		return resultVo;
		
	}
	
	@RequestMapping(value="submitRedpack")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo submitRedpack(HttpServletRequest httpServletRequest)throws Exception{
		String redPackId = httpServletRequest.getParameter("redPackId");
		
		BattleRedpack battleRedpack = battleRedpackService.findOne(redPackId);
		
		battleRedpack.setStatus(BattleRedpack.IN_STATUS);
		
		
		battleRedpackService.update(battleRedpack);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo; 
	}
	
	
	@RequestMapping(value="addTask")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo addTask(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String beanNum = httpServletRequest.getParameter("beanNum");
		String redpackId = httpServletRequest.getParameter("redpackId");
		String rankId = httpServletRequest.getParameter("rankId");
		String process = httpServletRequest.getParameter("process");
		BattleRedpackTask battleRedpackTask = new BattleRedpackTask();
		
		BattleRank battleRank = battleRankService.findOne(rankId);
		
		
		battleRedpackTask.setBeanNum(Integer.parseInt(beanNum));
		battleRedpackTask.setIndex(0);
		battleRedpackTask.setRedpackId(redpackId);
		battleRedpackTask.setRewardType(BattleRedpackTask.BEAN_REWARD_TYPE);
		battleRedpackTask.setType(BattleRedpackTask.RANK_TYPE);
		battleRedpackTask.setRankId(rankId);
		battleRedpackTask.setProcess(Integer.parseInt(process));
		battleRedpackTask.setName("任务:【"+battleRank.getName()+"】完成"+process+"米");
		battleRedpackTaskService.add(battleRedpackTask);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	@RequestMapping(value="addRankRedpack")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo addRankRedpack(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);

		String amount = httpServletRequest.getParameter("amount");
		String count = httpServletRequest.getParameter("count");
		
		Integer countInt = Integer.parseInt(count);
		
		
		if(countInt<2){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		BigDecimal amountBigDecimal = new BigDecimal(amount);
		
		if(amountBigDecimal.intValue()>100){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		if(amountBigDecimal.divide(new BigDecimal(countInt),RoundingMode.DOWN).floatValue()<0.01){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		String name = httpServletRequest.getParameter("name");
		
		
		amountBigDecimal = amountBigDecimal.multiply(new BigDecimal(1000));
		DistributionAmountUtil distributionAmountUtil = new DistributionAmountUtil();
		
		List<Integer> amountThousands = distributionAmountUtil.splitRedPackets(amountBigDecimal.intValue(), countInt, 1, amountBigDecimal.multiply(new BigDecimal(0.75)).intValue());
		
		if(countInt.intValue()==1){
			amountThousands = new ArrayList<>();
			amountThousands.add(amountBigDecimal.intValue());
		}
		
		BattleRedpack battleRedpack = new BattleRedpack();
		battleRedpack.setAmount(new BigDecimal(amount));
		battleRedpack.setImgUrl(userInfo.getHeadimgurl());
		battleRedpack.setLinkImgUrl("");
		battleRedpack.setName(name);
		battleRedpack.setNum(countInt);
		battleRedpack.setRemainAmount(new BigDecimal(amount));
		battleRedpack.setRemainNum(countInt);
		battleRedpack.setStatus(BattleRedpack.FREE_STATUS);
		battleRedpack.setIsPublic(1);
		
		battleRedpackService.add(battleRedpack);
		
		for(Integer amountThousand:amountThousands){
			BigDecimal bigDecimal = new BigDecimal(amountThousand);
			bigDecimal = bigDecimal.divide(new BigDecimal(1000));
			
			BattleRedpackDistribution battleRedpackDistribution = new BattleRedpackDistribution();
			battleRedpackDistribution.setAmount(bigDecimal);
			battleRedpackDistribution.setRedpackId(battleRedpack.getId());
			battleRedpackDistribution.setStatus(BattleRedpackDistribution.UN_RECEIVE_STATUS);
			battleRedpackDistributionService.add(battleRedpackDistribution);
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRedpack);
		return resultVo;
	}

}
