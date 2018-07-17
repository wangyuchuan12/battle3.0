package com.battle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="battle_quick_room")
public class BattleQuickRoom {
	public static final Integer FREE_STATUS = 0;
	public static final Integer IN_STATUS = 1;
	
	//主题id
	@Id
	private String id;
	
	
	@Column(name="room_id")
	private String roomId;
	
	@Column
	private Integer status;
	
	@Column(name="group_id")
	private String groupId;
	
	@Column
	private Integer num;
	
	@Column(name="img_url")
	private String imgUrl;
	
	@Column
	private String imgs;
	
	@Column
	private String name;
	
	@Column
	private Integer level;
	
	@Column(name="is_del")
	private Integer isDel;
	
	@Column
	private String detail;
	
	@Column(name="cost_bean")
	private Integer costBean;
	
	@Column(name="cost_love")
	private Integer costLove;
	
	@Column(name="search_key")
	private String searchKey;
	
	@Column(name = "create_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime createAt;
	
    @Column(name = "update_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime updateAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getCostBean() {
		return costBean;
	}

	public void setCostBean(Integer costBean) {
		this.costBean = costBean;
	}

	public Integer getCostLove() {
		return costLove;
	}

	public void setCostLove(Integer costLove) {
		this.costLove = costLove;
	}
	

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public DateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(DateTime createAt) {
		this.createAt = createAt;
	}

	public DateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(DateTime updateAt) {
		this.updateAt = updateAt;
	}
}
