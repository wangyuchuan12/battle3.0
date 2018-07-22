package com.wyc.listener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleRank;
import com.battle.executer.BattleDataManager;
import com.battle.executer.BattleRoomFactory;
import com.battle.executer.ExecuterStore;
import com.battle.executer.param.RoomParam;
import com.battle.executer.param.UserParam;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.service.BattleRankService;

@Service
public class InitListener implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private BattleRoomFactory battleRoomFactory;
	
	@Autowired
	private BattleRankService battleRankService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		RoomParam roomParam = new RoomParam();
		roomParam.setType(BattleRoomVo.RANK_TYPE);
		roomParam.setUserParams(new ArrayList<UserParam>());
		roomParam.setData(new HashMap<String,Object>());
		roomParam.setGroupId("");
		ExecuterStore executerStore = battleRoomFactory.init(roomParam);
		BattleDataManager battleDataManager = executerStore.getBattleDataManager();
		
		BattleRoomVo battleRoomVo = battleDataManager.getBattleRoom();
		
		List<BattleRank> battleRanks = battleRankService.findAllByIsDefault(1);
		
		BattleRank battleRank = null;
		
		if(battleRanks.size()>0){
			battleRank = battleRanks.get(0);
			battleRank.setRoomId(battleRoomVo.getId());
			
			battleRankService.update(battleRank);
		}
		
		
	}

	
}
