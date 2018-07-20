package com.battle.room.executer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.battle.domain.BattleWaitRoom;
import com.battle.domain.BattleWaitRoomGroup;
import com.battle.domain.BattleWaitRoomMember;
import com.battle.domain.BattleWaitRoomNum;
import com.battle.room.exception.BattleWaitRoomStartException;
import com.battle.room.vo.BattleWaitRoomMemberVo;
import com.battle.room.vo.BattleWaitRoomVo;
import com.battle.service.BattleWaitRoomGroupService;
import com.battle.service.BattleWaitRoomNumService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Service
public class BattleWaitRoomConnector {
	
	private Map<String, BattleWaitRoomExecuter> battleWaitRoomExecuterMap = new ConcurrentHashMap<>();
	
	@Autowired
	private BattleWaitRoomGroupService battleWaitRoomGroupService;
	
	@Autowired
	private BattleWaitRoomNumService battleWaitRoomNumService;
	
	@Autowired
    private AutowireCapableBeanFactory factory;
	
	public List<BattleWaitRoomMemberVo> getMembers(String roomId){
		
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		
		return battleWaitRoomExecuter.getBattleWaitRoomDataManager().getMembers();
	}
	
	
	public BattleWaitRoomMemberVo findMember(String roomId,String userId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		
		return battleWaitRoomExecuter.getBattleWaitRoomDataManager().findMemberByUserId(userId);
	}
	
	
	@Scheduled(cron="0/10 * *  * * ? ")
	public void checkOut(){
		for(Entry<String, BattleWaitRoomExecuter> entry:battleWaitRoomExecuterMap.entrySet()){
			BattleWaitRoomExecuter battleWaitRoomExecuter = entry.getValue();
			boolean flag = battleWaitRoomExecuter.checkRoom();
			if(!flag){
				battleWaitRoomExecuterMap.remove(battleWaitRoomExecuter.getBattleWaitRoomDataManager().getBattleWaitRoom().getId());
			}
		}
	}
	
	@Scheduled(cron="0/10 * *  * * ? ")
	public void checkOwner(){
		for(Entry<String, BattleWaitRoomExecuter> entry:battleWaitRoomExecuterMap.entrySet()){
			BattleWaitRoomExecuter battleWaitRoomExecuter = entry.getValue();
			battleWaitRoomExecuter.checkOwner();
		}
	}
	
	public BattleWaitRoomVo create(UserInfo ownerUser,String searchKey){
		
		
		if(CommonUtil.isEmpty(searchKey)){
			searchKey = "personal";
		}
		
		
		List<BattleWaitRoomVo> battleWaitRooms = findAll(ownerUser.getId(), searchKey);
		
		
		BattleWaitRoomVo battleWaitRoomVo = null;
		if(battleWaitRooms==null||battleWaitRooms.size()==0){
			battleWaitRoomVo = new BattleWaitRoomVo();
			List<BattleWaitRoomGroup> battleWaitRoomGroups = battleWaitRoomGroupService.findAllByIsDefault(1);
			BattleWaitRoomGroup battleWaitRoomGroup = battleWaitRoomGroups.get(0);
			
			battleWaitRoomVo.setGroupId(battleWaitRoomGroup.getGroupId());
			battleWaitRoomVo.setGroupName(battleWaitRoomGroup.getName());
		
			BattleWaitRoomNum battleWaitRoomNum = battleWaitRoomNumService.findOneBySearchKey(searchKey);
			if(battleWaitRoomNum==null){
				List<BattleWaitRoomNum> battleWaitRoomNums = battleWaitRoomNumService.findAllByIsDefault(1);
				battleWaitRoomNum = battleWaitRoomNums.get(0);
			}
			battleWaitRoomVo.setMaxNum(battleWaitRoomNum.getMaxNum());
			battleWaitRoomVo.setMinNum(battleWaitRoomNum.getMinNum());
			
			battleWaitRoomVo.setOwnerId(ownerUser.getId());
			battleWaitRoomVo.setStatus(BattleWaitRoom.FREE_STATUS);
			battleWaitRoomVo.setIsPublic(1);
			battleWaitRoomVo.setIsFull(0);
			battleWaitRoomVo.setNum(0);
			battleWaitRoomVo.setSearchKey(searchKey);
			battleWaitRoomVo.setId(UUID.randomUUID().toString());
			
			BattleWaitRoomDataManager battleWaitRoomDataManager = new BattleWaitRoomDataManager(battleWaitRoomVo);
			
			factory.autowireBean(battleWaitRoomDataManager);
			
			BattleWaitRoomPublish battleWaitRoomPublish = new BattleWaitRoomPublish(battleWaitRoomDataManager);
			
			factory.autowireBean(battleWaitRoomPublish);
			
			BattleWaitRoomExecuter battleWaitRoomExecuter = new BattleWaitRoomExecuter(battleWaitRoomDataManager,battleWaitRoomPublish);
			
			factory.autowireBean(battleWaitRoomExecuter);
			
			battleWaitRoomExecuterMap.put(battleWaitRoomVo.getId(), battleWaitRoomExecuter);
			
			List<BattleWaitRoomMemberVo> battleWaitRoomMemberVos = new ArrayList<>();
			
			BattleWaitRoomMemberVo battleWaitRoomMember = new BattleWaitRoomMemberVo();
			battleWaitRoomMember = new BattleWaitRoomMemberVo();
			battleWaitRoomMember.setImgUrl(ownerUser.getHeadimgurl());
			battleWaitRoomMember.setNickname(ownerUser.getNickname());
			battleWaitRoomMember.setRoomId(battleWaitRoomVo.getId());
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);
			battleWaitRoomMember.setIsOwner(1);
			battleWaitRoomMember.setUserId(ownerUser.getId());
			battleWaitRoomMember.setToken(ownerUser.getToken());
			battleWaitRoomMember.setIsEnd(0);
			battleWaitRoomMember.setId(UUID.randomUUID().toString());
			battleWaitRoomMemberVos.add(battleWaitRoomMember);
			battleWaitRoomVo.setBattleWaitRoomMembers(battleWaitRoomMemberVos);
			
		}else{
			battleWaitRoomVo = battleWaitRooms.get(0);
		}
		
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(battleWaitRoomVo.getId());
		
		BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomExecuter.getBattleWaitRoomDataManager();
		
		BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(ownerUser.getId());
		
		BattleWaitRoomPublish battleWaitRoomPublish = battleWaitRoomExecuter.getBattleWaitRoomPublish();
		List<BattleWaitRoomMemberVo> members = battleWaitRoomDataManager.getMembers();
		if(battleWaitRoomMember==null){
			
		}else{
			battleWaitRoomMember.setStatus(BattleWaitRoomMember.READY_STATUS);

			for(BattleWaitRoomMemberVo battleWaitRoomMember2:members){
				if(battleWaitRoomMember2.getId().equals(battleWaitRoomMember.getId())){
					battleWaitRoomMember2.setStatus(BattleWaitRoomMember.READY_STATUS);
				}
			}
		}
		

		
		battleWaitRoomPublish.waitRoomMemberPublish(ownerUser.getId());
		
		return battleWaitRoomVo;
	}
	
	public void out(String roomId,String userId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		if(battleWaitRoomExecuter!=null){
			battleWaitRoomExecuter.out(userId);
		}
	}
	
	public void into(String roomId,UserInfo userInfo){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		battleWaitRoomExecuter.into(userInfo);
	}
	
	public void start(String roomId)throws BattleWaitRoomStartException{
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		battleWaitRoomExecuter.start();
	}
	
	public BattleWaitRoomMemberVo cancel(String roomId,String userId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		return battleWaitRoomExecuter.cancel(userId);
	}
	
	public BattleWaitRoomMemberVo ready(String roomId,String userId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		return battleWaitRoomExecuter.ready(userId);
	}
	
	public BattleWaitRoomMemberVo ownerChange(String roomId,String userId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		return battleWaitRoomExecuter.ownerChange(userId);
	}
	
	public List<BattleWaitRoomVo> searchRoom(String searchKey){
		List<BattleWaitRoomVo> battleWaitRoomVos = new ArrayList<>();
		
		for(Entry<String, BattleWaitRoomExecuter> battleWaitRoomExecuterEntry:battleWaitRoomExecuterMap.entrySet()){
			
			BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterEntry.getValue();
			
			BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomExecuter.getBattleWaitRoomDataManager();
			
			BattleWaitRoomVo battleWaitRoom = battleWaitRoomDataManager.getBattleWaitRoom();
			
			if(battleWaitRoom.getSearchKey().equals(searchKey)){
				battleWaitRoomVos.add(battleWaitRoom);
			}
		}
		
		
		return battleWaitRoomVos;
		
		
	}
	
	public void kickOut(String roomId,String memberId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
		battleWaitRoomExecuter.kickOut(memberId);
	}
	
	public void info(String roomId){
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(roomId);
	}
	
	public List<BattleWaitRoomVo> findAll(String ownerId,String searchKey){
		List<BattleWaitRoomVo> battleWaitRoomVos = new ArrayList<>();
		
		for(Entry<String, BattleWaitRoomExecuter> battleWaitRoomExecuterEntry:battleWaitRoomExecuterMap.entrySet()){
			
			BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterEntry.getValue();
			
			BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomExecuter.getBattleWaitRoomDataManager();
			
			BattleWaitRoomVo battleWaitRoom = battleWaitRoomDataManager.getBattleWaitRoom();
			
			if(battleWaitRoom.getSearchKey().equals(searchKey)&&battleWaitRoom.getOwnerId().equals(ownerId)){
				battleWaitRoomVos.add(battleWaitRoom);
			}
		}
		
		return battleWaitRoomVos;
	}


	public BattleWaitRoomDataManager findDataManager(String id) {
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(id);
		
		if(battleWaitRoomExecuter!=null){
			return battleWaitRoomExecuter.getBattleWaitRoomDataManager();
		}else{
			return null;
		}
	}


	public BattleWaitRoomPublish findPublish(String id) {
		BattleWaitRoomExecuter battleWaitRoomExecuter = battleWaitRoomExecuterMap.get(id);
		
		if(battleWaitRoomExecuter!=null){
			return battleWaitRoomExecuter.getBattleWaitRoomPublish();
		}else{
			return null;
		}
	}
	
	
	
	public BattleWaitRoomVo findRoomByUserId(String userId){
	
		for(Entry<String, BattleWaitRoomExecuter> entry:battleWaitRoomExecuterMap.entrySet()){
			BattleWaitRoomExecuter battleWaitRoomExecuter = entry.getValue();
			BattleWaitRoomDataManager battleWaitRoomDataManager = battleWaitRoomExecuter.getBattleWaitRoomDataManager();
			BattleWaitRoomMemberVo battleWaitRoomMember = battleWaitRoomDataManager.findMemberByUserId(userId);
			
			if(battleWaitRoomMember!=null){
				return battleWaitRoomDataManager.getBattleWaitRoom();
			}
		}
		
		return null;
	}
	
}
