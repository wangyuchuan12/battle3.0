package com.battle.api;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleQuestionDistribution;
import com.battle.domain.BattleRankMember;
import com.battle.executer.BattleRoomConnector;
import com.battle.rank.manager.RankManager;
import com.battle.service.other.BattleQuestionDistributionHandleService;


@Controller
@RequestMapping(value="/api/test")
public class TestApi {

	@Autowired
	private RankManager rankManager;
	@Autowired
	private BattleQuestionDistributionHandleService battleQuestionDistributionHandleService;
	
	
	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	@RequestMapping(value="addBySubjectId")
	@ResponseBody
	@Transactional
	public void addBySubjectId(HttpServletRequest httpServletRequest){
		
		String rankId = httpServletRequest.getParameter("rankId");
		
		String periodId = httpServletRequest.getParameter("periodId");
		String battleSubjectId = httpServletRequest.getParameter("battleSubjectId");
		String num = httpServletRequest.getParameter("num");
		rankManager.addBySubjectId(rankId, periodId, battleSubjectId, Integer.parseInt(num));
	}
	
	
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
