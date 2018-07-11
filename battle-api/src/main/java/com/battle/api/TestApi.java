package com.battle.api;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleQuestionDistribution;
import com.battle.executer.BattleRoomConnector;
import com.battle.service.other.BattleQuestionDistributionHandleService;


@Controller
@RequestMapping(value="/api/test")
public class TestApi {
	
	@Autowired
	private BattleQuestionDistributionHandleService battleQuestionDistributionHandleService;
	
	
	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	@RequestMapping(value="flushDistribution")
	@ResponseBody
	@Transactional
	public void flushDistribution(HttpServletRequest httpServletRequest){
		String ids = httpServletRequest.getParameter("ids");
		
		for(String id:ids.split(",")){
			BattleQuestionDistribution battleQuestionDistribution = battleQuestionDistributionHandleService.findOne(id);
			battleQuestionDistributionHandleService.flushDistribution(battleQuestionDistribution);
		}
	}
	
	
	@RequestMapping(value="removeRoom")
	@ResponseBody
	@Transactional
	public void removeRoom(HttpServletRequest httpServletRequest){
		String id = httpServletRequest.getParameter("id");
		battleRoomConnector.removeExecuter(id);
	}
	
}
