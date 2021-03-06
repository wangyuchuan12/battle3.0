package com.battle.executer.vo;

import java.util.List;
import java.util.Map;

public class BattleRoomVo {
	
	public static final Integer DAN_TYPE = 0;
	
	public static final Integer ROOM_TYPE = 1;
	
	public static final Integer RANK_TYPE = 2;

	private String id;
	
	private Integer num;
	
	private Integer rangeGogal;
	
	private String battleId;
	
	private String periodId;
	
	private String rankId;
	
	private List<BattleRoomMemberVo> members;
	
	private List<BattleRoomRewardRecord> battleRoomRewardRecords;
	
	private List<BattleRoomShareRewardVo> battleRoomShareRewards;
	
	private List<BattleRankGoodVo> battleRankGood;
	
	private Integer type;
	
	private Map<String, Object> data;
	
	private Integer isStop;
	
	private Integer loveCount;
	
	private String groupId;
	
	private Integer rewardBean;
	
	private Integer rewardBean2;
	
	private Integer rewardBean3;
	
	private Integer rewardBean4;
	
	private Integer rewardBean5;
	
	private Integer rewardBean6;
	
	private Integer rewardBean7;
	
	private Integer rewardBean8;
	
	private Integer rewardBean9;
	
	private Integer rewardBean10;
	
	private Integer subBean;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getRangeGogal() {
		return rangeGogal;
	}

	public void setRangeGogal(Integer rangeGogal) {
		this.rangeGogal = rangeGogal;
	}

	public String getBattleId() {
		return battleId;
	}

	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}


	public List<BattleRoomMemberVo> getMembers() {
		return members;
	}

	public void setMembers(List<BattleRoomMemberVo> members) {
		this.members = members;
	}

	public Integer getRewardBean() {
		return rewardBean;
	}

	public void setRewardBean(Integer rewardBean) {
		this.rewardBean = rewardBean;
	}

	public Integer getSubBean() {
		return subBean;
	}

	public void setSubBean(Integer subBean) {
		this.subBean = subBean;
	}

	public Integer getRewardBean2() {
		return rewardBean2;
	}

	public void setRewardBean2(Integer rewardBean2) {
		this.rewardBean2 = rewardBean2;
	}

	public Integer getRewardBean3() {
		return rewardBean3;
	}

	public void setRewardBean3(Integer rewardBean3) {
		this.rewardBean3 = rewardBean3;
	}

	public Integer getRewardBean4() {
		return rewardBean4;
	}

	public void setRewardBean4(Integer rewardBean4) {
		this.rewardBean4 = rewardBean4;
	}

	public Integer getRewardBean5() {
		return rewardBean5;
	}

	public void setRewardBean5(Integer rewardBean5) {
		this.rewardBean5 = rewardBean5;
	}

	public Integer getRewardBean6() {
		return rewardBean6;
	}

	public void setRewardBean6(Integer rewardBean6) {
		this.rewardBean6 = rewardBean6;
	}

	public Integer getRewardBean7() {
		return rewardBean7;
	}

	public void setRewardBean7(Integer rewardBean7) {
		this.rewardBean7 = rewardBean7;
	}

	public Integer getRewardBean8() {
		return rewardBean8;
	}

	public void setRewardBean8(Integer rewardBean8) {
		this.rewardBean8 = rewardBean8;
	}

	public Integer getRewardBean9() {
		return rewardBean9;
	}

	public void setRewardBean9(Integer rewardBean9) {
		this.rewardBean9 = rewardBean9;
	}

	public Integer getRewardBean10() {
		return rewardBean10;
	}

	public void setRewardBean10(Integer rewardBean10) {
		this.rewardBean10 = rewardBean10;
	}

	public List<BattleRoomRewardRecord> getBattleRoomRewardRecords() {
		return battleRoomRewardRecords;
	}

	public void setBattleRoomRewardRecords(List<BattleRoomRewardRecord> battleRoomRewardRecords) {
		this.battleRoomRewardRecords = battleRoomRewardRecords;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Integer getLoveCount() {
		return loveCount;
	}

	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}

	public List<BattleRoomShareRewardVo> getBattleRoomShareRewards() {
		return battleRoomShareRewards;
	}

	public void setBattleRoomShareRewards(List<BattleRoomShareRewardVo> battleRoomShareRewards) {
		this.battleRoomShareRewards = battleRoomShareRewards;
	}

	public Integer getIsStop() {
		return isStop;
	}

	public void setIsStop(Integer isStop) {
		this.isStop = isStop;
	}

	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<BattleRankGoodVo> getBattleRankGood() {
		return battleRankGood;
	}

	public void setBattleRankGood(List<BattleRankGoodVo> battleRankGood) {
		this.battleRankGood = battleRankGood;
	}
}
