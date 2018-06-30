package com.battle.executer.vo;

public class BattleRewardVo {
	
	public static final Integer ADD_STATUS = 0;
	public static final Integer SUB_STATUS = 1;

	private String id;
	
	private Integer rewardBean;
	
	private Integer subBean;
	
	private String nickname;
	
	private String imgUrl;
	
	private String userId;
	
	private Integer status;
	
	private Integer cnRightCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRewardBean() {
		return rewardBean;
	}

	public void setRewardBean(Integer rewardBean) {
		this.rewardBean = rewardBean;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSubBean() {
		return subBean;
	}

	public void setSubBean(Integer subBean) {
		this.subBean = subBean;
	}

	public Integer getCnRightCount() {
		return cnRightCount;
	}

	public void setCnRightCount(Integer cnRightCount) {
		this.cnRightCount = cnRightCount;
	}
	
	
}
