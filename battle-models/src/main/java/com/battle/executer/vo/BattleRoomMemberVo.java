package com.battle.executer.vo;

public class BattleRoomMemberVo {
	
	public static final Integer STATUS_IN = 0;
	public static final Integer STATUS_DIE = 1;
	public static final Integer STATUS_COMPLETE = 2;
	public static final Integer STATUS_OUT = 3;
	
	private String id;
	private Integer remainLove;
	private Integer limitLove;
	private Integer rangeGogal;
	private Integer process;
	private String roomId;
	private String battleId;
	private String periodId;
	private Integer status;
	private String imgUrl;
	private String nickname;
	private String userId;
	private Integer isPass;
	//连续答对次数
	private Integer cnRightCount;
	
	private String accountId;
	
	private Integer rank;
	
	private Integer rewardBean;
	
	private Integer rewardLove;
	
	//如果为1表示不能允许再加进来了
	private Integer isEnd;
	
	private String token;
	
	//结束语
	private String endCotent;
	
	private Integer beanNum;
	
	private Integer preClear;
	
	private Integer isOut;
	
	private Integer shareNum;
	
	private BattleRoomCoolMemberVo battleRoomCoolMemberVo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getRemainLove() {
		return remainLove;
	}
	public void setRemainLove(Integer remainLove) {
		if(battleRoomCoolMemberVo!=null){
			battleRoomCoolMemberVo.setLoveCount(remainLove);
		}
		this.remainLove = remainLove;
	}
	public Integer getLimitLove() {
		return limitLove;
	}
	public void setLimitLove(Integer limitLove) {
		this.limitLove = limitLove;
	}
	public Integer getRangeGogal() {
		return rangeGogal;
	}
	public void setRangeGogal(Integer rangeGogal) {
		this.rangeGogal = rangeGogal;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getProcess() {
		return process;
	}
	public void setProcess(Integer process) {
		this.process = process;
	}
	public Integer getCnRightCount() {
		return cnRightCount;
	}
	public void setCnRightCount(Integer cnRightCount) {
		this.cnRightCount = cnRightCount;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getRewardBean() {
		return rewardBean;
	}
	public void setRewardBean(Integer rewardBean) {
		this.rewardBean = rewardBean;
	}
	public Integer getRewardLove() {
		return rewardLove;
	}
	public void setRewardLove(Integer rewardLove) {
		this.rewardLove = rewardLove;
	}
	public Integer getIsPass() {
		return isPass;
	}
	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}
	public String getEndCotent() {
		return endCotent;
	}
	public void setEndCotent(String endCotent) {
		this.endCotent = endCotent;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public Integer getBeanNum() {
		return beanNum;
	}
	public void setBeanNum(Integer beanNum) {
		this.beanNum = beanNum;
	}
	public BattleRoomCoolMemberVo getBattleRoomCoolMemberVo() {
		return battleRoomCoolMemberVo;
	}
	public void setBattleRoomCoolMemberVo(BattleRoomCoolMemberVo battleRoomCoolMemberVo) {
		this.battleRoomCoolMemberVo = battleRoomCoolMemberVo;
	}
	public Integer getPreClear() {
		return preClear;
	}
	public void setPreClear(Integer preClear) {
		this.preClear = preClear;
	}
	public Integer getIsOut() {
		return isOut;
	}
	public void setIsOut(Integer isOut) {
		this.isOut = isOut;
	}
	public Integer getShareNum() {
		return shareNum;
	}
	public void setShareNum(Integer shareNum) {
		this.shareNum = shareNum;
	}
	
}
