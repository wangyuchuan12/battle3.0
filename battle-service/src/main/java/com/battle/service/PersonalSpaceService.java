package com.battle.service;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.dao.PersonalSpaceDao;
import com.battle.domain.PersonalSpace;

@Service
public class PersonalSpaceService {

	@Autowired
	private PersonalSpaceDao personalSpaceDao;

	public void add(PersonalSpace personalSpace) {
		
		personalSpace.setId(UUID.randomUUID().toString());
		personalSpace.setUpdateAt(new DateTime());
		personalSpace.setCreateAt(new DateTime());
		personalSpace.setActivityTime(new DateTime());
		
		personalSpaceDao.save(personalSpace);
	}


	public List<PersonalSpace> findAllByUserIdAndIsDel(String userId, int isDel, Pageable pageable) {
		
		return personalSpaceDao.findAllByUserIdAndIsDel(userId,isDel,pageable);
	}


	public PersonalSpace findOne(String id) {
		return personalSpaceDao.findOne(id);
	}


	public PersonalSpace findOneByUserIdAndRankIdAndType(String userId, String rankId, Integer type) {
		
		return personalSpaceDao.findOneByUserIdAndRankIdAndType(userId,rankId,type);
	}


	public Page<PersonalSpace> findAllByRankIdAndIsDel(String rankId, int isDel, Pageable pageable) {
		
		return personalSpaceDao.findAllByRankIdAndIsDel(rankId,isDel,pageable);
	}


	public PersonalSpace findOneByRankIdAndIsRoot(String rankId, int isRoot) {
		
		return personalSpaceDao.findOneByRankIdAndIsRoot(rankId,isRoot);
	}


	public void update(PersonalSpace personalSpace) {
		personalSpace.setUpdateAt(new DateTime());
		
		personalSpaceDao.save(personalSpace);
		
	}


	public List<PersonalSpace> findAllByIsRootAndUserId(int isRoot, String userId) {
	
		return personalSpaceDao.findAllByIsRootAndUserId(isRoot,userId);
	}
}
