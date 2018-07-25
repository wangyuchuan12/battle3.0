package com.battle.service.other;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.battle.domain.BattleRoomCoolMember;
import com.battle.executer.vo.BattleRoomCoolMemberVo;
import com.battle.service.BattleRoomMemberCoolService;

@Service
public class BattleRoomCoolHandle {

	@Autowired
	private BattleRoomMemberCoolService battleRoomMemberCoolService;
	
	public BattleRoomCoolMemberVo filterMember(BattleRoomCoolMemberVo battleMemberLoveCool){
		if(battleMemberLoveCool==null){
			return null;
		}
		
		if(battleMemberLoveCool.getStatus()!=BattleRoomCoolMember.STATUS_IN){
			battleMemberLoveCool.setStartDatetime(new DateTime());
		}
		
		Integer loveCount = battleMemberLoveCool.getLoveCount();
		
		Integer loveLimit = battleMemberLoveCool.getLoveLimit();
		if(loveCount==null||loveCount<0){
			loveCount = 0;
		}
		
		
		
		System.out.println("..........loveLimit:"+loveLimit+",loveCount:"+loveCount);
		if(loveLimit>loveCount){
			
			battleMemberLoveCool.setCoolLoveSeq(loveCount+1);
			Integer millisec = battleMemberLoveCool.getMillisec();
			Integer unit = battleMemberLoveCool.getUnit();
			DateTime startDatetime = battleMemberLoveCool.getStartDatetime();
			Integer upperLimit = battleMemberLoveCool.getUpperLimit();
			
			DateTime nowDatetime = new DateTime();
			
			Long differ = nowDatetime.getMillis()-startDatetime.getMillis();
			
			System.out.println(".......now.mills:"+nowDatetime.getMillis()+",startMill:"+startDatetime.getMillis());
			
			Long schedule = (differ/millisec)*unit+battleMemberLoveCool.getSchedule();
			
			System.out.println("..........before.schedule:"+schedule);
			
			long time = schedule/upperLimit;
			
			schedule = schedule - upperLimit*time;
			
			
			System.out.println("..........schedule:"+schedule+",differ:"+differ+",millisec:"+millisec+",unit:"+unit+",time:"+time);
			
			Long loveCount2 = time+loveCount;
			
			if(loveCount2<loveLimit){
				battleMemberLoveCool.setStatus(BattleRoomCoolMember.STATUS_IN);
				battleMemberLoveCool.setSchedule(schedule.intValue());
				battleMemberLoveCool.setLoveCount(loveCount2.intValue());
				battleMemberLoveCool.setCoolLoveSeq(loveCount2.intValue()+1);
				battleMemberLoveCool.setStartDatetime(new DateTime());
			}else{
				battleMemberLoveCool.setStatus(BattleRoomCoolMember.STATUS_COMPLETE);
				battleMemberLoveCool.setSchedule(upperLimit);
				battleMemberLoveCool.setSchedule(0);
				battleMemberLoveCool.setCoolLoveSeq(0);
				battleMemberLoveCool.setLoveCount(loveCount);
			}
		}else{
			battleMemberLoveCool.setSchedule(battleMemberLoveCool.getUpperLimit());
			battleMemberLoveCool.setStatus(BattleRoomCoolMember.STATUS_COMPLETE);
			battleMemberLoveCool.setSchedule(0);
			battleMemberLoveCool.setCoolLoveSeq(0);
			
		}
		
		System.out.println("..........after.schedule:"+battleMemberLoveCool.getSchedule()+",id:"+battleMemberLoveCool.getId()+",loveCount:"+battleMemberLoveCool.getLoveCount());
		
		return battleMemberLoveCool;
	}
	
	public BattleRoomCoolMemberVo filterAndSaveCoolMember(BattleRoomCoolMemberVo battleRoomCoolMemberVo){
		battleRoomCoolMemberVo = filterMember(battleRoomCoolMemberVo);
		
		BattleRoomCoolMember battleMemberLoveCool = battleRoomMemberCoolService.findOneByRoomIdAndUserId(battleRoomCoolMemberVo.getRoomId(),battleRoomCoolMemberVo.getUserId());
		
		battleMemberLoveCool.setCoolLoveSeq(battleRoomCoolMemberVo.getCoolLoveSeq());
		battleMemberLoveCool.setLoveCount(battleRoomCoolMemberVo.getLoveCount());
		battleMemberLoveCool.setLoveLimit(battleRoomCoolMemberVo.getLoveLimit());
		battleMemberLoveCool.setMillisec(battleRoomCoolMemberVo.getMillisec());
		battleMemberLoveCool.setSchedule(battleRoomCoolMemberVo.getSchedule());
		battleMemberLoveCool.setStartDatetime(battleRoomCoolMemberVo.getStartDatetime());
		battleMemberLoveCool.setStatus(battleRoomCoolMemberVo.getStatus());
		battleMemberLoveCool.setUnit(battleRoomCoolMemberVo.getUnit());
		battleMemberLoveCool.setUpperLimit(battleRoomCoolMemberVo.getUpperLimit());
		
		battleRoomMemberCoolService.update(battleMemberLoveCool);
		
		return battleRoomCoolMemberVo;
	}
	
	public BattleRoomCoolMemberVo createBattleRoomCoolMember(String roomId,String userId,Integer loveCount){
		BattleRoomCoolMember battleRoomCoolMember = new BattleRoomCoolMember();
		battleRoomCoolMember.setCoolLoveSeq(0);
		battleRoomCoolMember.setLoveCount(loveCount);
		battleRoomCoolMember.setLoveLimit(loveCount);
		battleRoomCoolMember.setMillisec(1000);
		battleRoomCoolMember.setRoomId(roomId);
		battleRoomCoolMember.setSchedule(0);
		battleRoomCoolMember.setStartDatetime(new DateTime());
		battleRoomCoolMember.setStatus(BattleRoomCoolMember.STATUS_IN);
		battleRoomCoolMember.setUnit(1);
		battleRoomCoolMember.setUpperLimit(100);
		battleRoomCoolMember.setUserId(userId);
		
		battleRoomMemberCoolService.add(battleRoomCoolMember);
		
		BattleRoomCoolMemberVo battleRoomCoolMemberVo = new BattleRoomCoolMemberVo();
		battleRoomCoolMemberVo.setId(battleRoomCoolMember.getId());
		battleRoomCoolMemberVo.setCoolLoveSeq(battleRoomCoolMember.getCoolLoveSeq());
		battleRoomCoolMemberVo.setLoveCount(battleRoomCoolMember.getLoveCount());
		battleRoomCoolMemberVo.setLoveLimit(battleRoomCoolMember.getLoveLimit());
		battleRoomCoolMemberVo.setMillisec(battleRoomCoolMember.getMillisec());
		battleRoomCoolMemberVo.setRoomId(roomId);
		battleRoomCoolMemberVo.setSchedule(battleRoomCoolMember.getSchedule());
		battleRoomCoolMemberVo.setStartDatetime(battleRoomCoolMember.getStartDatetime());
		battleRoomCoolMemberVo.setStatus(battleRoomCoolMember.getStatus());
		battleRoomCoolMemberVo.setUnit(battleRoomCoolMember.getUnit());
		battleRoomCoolMemberVo.setUpperLimit(battleRoomCoolMember.getUpperLimit());
		battleRoomCoolMemberVo.setUserId(battleRoomCoolMember.getUserId());
		
		return battleRoomCoolMemberVo;
	}
	
	public BattleRoomCoolMemberVo getCoolMember(String roomId,String userId){
		
		BattleRoomCoolMember battleMemberLoveCool = battleRoomMemberCoolService.findOneByRoomIdAndUserId(roomId,userId);
		
		
		if(battleMemberLoveCool==null){
			return null;
			
		}
		
		BattleRoomCoolMemberVo battleRoomCoolMemberVo = new BattleRoomCoolMemberVo();
		battleRoomCoolMemberVo.setId(battleMemberLoveCool.getId());
		battleRoomCoolMemberVo.setCoolLoveSeq(battleMemberLoveCool.getCoolLoveSeq());
		battleRoomCoolMemberVo.setLoveCount(battleMemberLoveCool.getLoveCount());
		battleRoomCoolMemberVo.setLoveLimit(battleMemberLoveCool.getLoveLimit());
		battleRoomCoolMemberVo.setMillisec(battleMemberLoveCool.getMillisec());
		battleRoomCoolMemberVo.setRoomId(roomId);
		battleRoomCoolMemberVo.setSchedule(battleMemberLoveCool.getSchedule());
		battleRoomCoolMemberVo.setStartDatetime(battleMemberLoveCool.getStartDatetime());
		battleRoomCoolMemberVo.setStatus(battleMemberLoveCool.getStatus());
		battleRoomCoolMemberVo.setUnit(battleMemberLoveCool.getUnit());
		battleRoomCoolMemberVo.setUpperLimit(battleMemberLoveCool.getUpperLimit());
		battleRoomCoolMemberVo.setUserId(userId);
		
		battleRoomCoolMemberVo = filterMember(battleRoomCoolMemberVo);
		
		
		battleMemberLoveCool.setCoolLoveSeq(battleRoomCoolMemberVo.getCoolLoveSeq());
		battleMemberLoveCool.setLoveCount(battleRoomCoolMemberVo.getLoveCount());
		battleMemberLoveCool.setLoveLimit(battleRoomCoolMemberVo.getLoveLimit());
		battleMemberLoveCool.setMillisec(battleRoomCoolMemberVo.getMillisec());
		battleMemberLoveCool.setSchedule(battleRoomCoolMemberVo.getSchedule());
		battleMemberLoveCool.setStartDatetime(battleRoomCoolMemberVo.getStartDatetime());
		battleMemberLoveCool.setStatus(battleRoomCoolMemberVo.getStatus());
		battleMemberLoveCool.setUnit(battleRoomCoolMemberVo.getUnit());
		battleMemberLoveCool.setUpperLimit(battleRoomCoolMemberVo.getUpperLimit());
		
		battleRoomMemberCoolService.update(battleMemberLoveCool);
		return battleRoomCoolMemberVo;
	}
}
