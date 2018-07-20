package com.battle.dao;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleWaitRoom;

public interface BattleWaitRoomDao extends CrudRepository<BattleWaitRoom, String>{

	List<BattleWaitRoom> findAllByOwnerIdAndSearchKeyAndStatus(String userId, String searchKey ,Integer status);

	@Query(value="from com.battle.domain.BattleWaitRoom bwr where "
			+ "bwr.status=:status and bwr.searchKey=:searchKey and bwr.isPublic=:isPublic and bwr.isFull=:isFull order by rand()")
	Page<BattleWaitRoom> findAllByStatusAndSearchKeyAndIsPublicAndIsFull(@Param("status")Integer status,@Param("searchKey") String searchKey,@Param("isPublic") int isPublic,@Param("isFull")int isFull,Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Override
	BattleWaitRoom findOne(String id);
}
