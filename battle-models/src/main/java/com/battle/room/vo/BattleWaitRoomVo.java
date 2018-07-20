package com.battle.room.vo;

import java.util.List;

public class BattleWaitRoomVo {
	
	public static final Integer FREE_STATUS = 0;
	public static final Integer IN_STATUS = 1;
	public static final Integer COMPLETE_STATUS = 2;

	private String id;
	
	private String groupId;
	
	private String groupName;
	
	private String ownerId;
	
	private Integer num;
	
	private Integer maxNum;
	
	private Integer minNum;
	
	private Integer status;
	
	private Integer isPublic;
	
	private Integer isFull;
	
	private String searchKey;
	
	private List<BattleWaitRoomMemberVo> battleWaitRoomMembers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getIsFull() {
		return isFull;
	}

	public void setIsFull(Integer isFull) {
		this.isFull = isFull;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<BattleWaitRoomMemberVo> getBattleWaitRoomMembers() {
		return battleWaitRoomMembers;
	}

	public void setBattleWaitRoomMembers(List<BattleWaitRoomMemberVo> battleWaitRoomMembers) {
		this.battleWaitRoomMembers = battleWaitRoomMembers;
	}

}
