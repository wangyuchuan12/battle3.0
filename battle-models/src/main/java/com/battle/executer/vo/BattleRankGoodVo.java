package com.battle.executer.vo;

import java.math.BigDecimal;


public class BattleRankGoodVo {

	public static final Integer RED_PACK_TYPE = 0;
	private String id;
	

	private String index;
	

	private Integer type;
	

	private Integer num;
	

	private String rankId;
	

	private BigDecimal amount;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIndex() {
		return index;
	}


	public void setIndex(String index) {
		this.index = index;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Integer getNum() {
		return num;
	}


	public void setNum(Integer num) {
		this.num = num;
	}


	public String getRankId() {
		return rankId;
	}


	public void setRankId(String rankId) {
		this.rankId = rankId;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
