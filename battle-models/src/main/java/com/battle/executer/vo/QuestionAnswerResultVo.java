package com.battle.executer.vo;

public class QuestionAnswerResultVo {

	private String answer;
	
	private String userImg;
	
	private String userName;
	
	private String userId;
	
	private String optionId;
	
	private Integer type;
	
	private Integer isRight;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsRight() {
		return isRight;
	}

	public void setIsRight(Integer isRight) {
		this.isRight = isRight;
	}
}
