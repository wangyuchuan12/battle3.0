package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.PersonalSpace;

public interface PersonalSpaceDao extends CrudRepository<PersonalSpace, String>{

	List<PersonalSpace> findAllByUserIdAndIsDel(String userId, int isDel, Pageable pageable);

	PersonalSpace findOneByUserIdAndRankIdAndType(String userId, String rankId, Integer type);

	Page<PersonalSpace> findAllByRankIdAndIsDel(String rankId, int isDel, Pageable pageable);

	PersonalSpace findOneByRankIdAndIsRoot(String rankId, int isRoot);

	List<PersonalSpace> findAllByIsRootAndUserId(int isRoot, String userId);

	List<PersonalSpace> findAllByIsPublic(int isPublic);


}
