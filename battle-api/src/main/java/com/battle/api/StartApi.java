package com.battle.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleRank;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.ExecuterStore;
import com.battle.executer.param.RoomParam;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleRankService;
import com.wyc.annotation.HandlerAnnotation;

@Controller
@RequestMapping(value="/api/battle/start")
public class StartApi {
	@Autowired
	private BattleRoomFactory battleRoomFactory;
	
	@Autowired
	private BattleRankService battleRankService;
	
	
	@RequestMapping(value="startRank")
	@ResponseBody
	@Transactional
	public void startRank(HttpServletRequest httpServletRequest) {
		
		List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
		
		BattleRank battleRank = null;
		
		if(battleRanks.size()>0){
			battleRank = battleRanks.get(0);	
		}
		
		if(battleRank!=null&&battleRank.getIsStart().intValue()==0){
			Map<String, Object> data = new HashMap<>();
			data.put("subBean", battleRank.getSubBean());
			data.put("beanCheck",false);
			RoomParam roomParam = new RoomParam();
			roomParam.setType(BattleRoomVo.RANK_TYPE);
			roomParam.setUserParams(new ArrayList<UserParam>());
			roomParam.setData(data);
			roomParam.setGroupId("");
			ExecuterStore executerStore = battleRoomFactory.init(roomParam);
			BattleDataManager battleDataManager = executerStore.getBattleDataManager();
			
			BattleRoomVo battleRoomVo = battleDataManager.getBattleRoom();
			battleRank.setRoomId(battleRoomVo.getId());
			battleRank.setIsStart(1);
			battleRankService.update(battleRank);
		}
		
	}
}
