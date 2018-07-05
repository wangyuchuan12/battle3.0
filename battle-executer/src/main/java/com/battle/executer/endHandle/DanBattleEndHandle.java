package com.battle.executer.endHandle;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.domain.BattleAccountResult;
import com.battle.domain.BattleDan;
import com.battle.executer.BattleEndHandle;
import com.battle.executer.BattleRoomDataManager;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.service.BattleAccountResultService;
import com.battle.service.BattleDanService;

public class DanBattleEndHandle implements BattleEndHandle{

	@Autowired
	private BattleAccountResultService battleAccountResultService;
	
	@Autowired
	private BattleDanService battleDanService;
	@Override
	public void end(BattleRoomDataManager battleRoomDataManager) {
		
	
		BattleRoomVo battleRoom = battleRoomDataManager.getBattleRoom();
		
		Map<String, Object> data = battleRoom.getData();
		
		String danId = data.get("danId").toString();
		
		BattleDan battleDan = battleDanService.findOne(danId);
		List<BattleRoomMemberVo> battleRoomMembers = battleRoomDataManager.getBattleMembers();
		for(BattleRoomMemberVo battleRoomMember:battleRoomMembers){
			
			System.out.println(".........battleRoomMember.getIsPass:"+battleRoomMember.getIsPass());
			if(battleRoomMember.getIsPass().intValue()==1){
				BattleAccountResult battleAccountResult = battleAccountResultService.findOneByUserId(battleRoomMember.getUserId());
				
				System.out.println("battleAccountResult.getLevel:"+battleAccountResult.getLevel()+",battleDan.getLevel():"+battleDan.getLevel());
				if(battleAccountResult.getLevel().intValue()<=battleDan.getLevel().intValue()){
					battleAccountResult.setLevel(battleDan.getLevel()+1);
					System.out.println("这里进来了，哈哈哈："+battleAccountResult.getLevel());
					battleAccountResultService.update(battleAccountResult);
				}
			}
		}
		
	}

}
