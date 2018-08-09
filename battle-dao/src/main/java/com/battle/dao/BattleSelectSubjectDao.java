package com.battle.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleSelectSubject;

public interface BattleSelectSubjectDao extends CrudRepository<BattleSelectSubject, String>{

	List<BattleSelectSubject> findAllByIsDel(int isDel, Pageable pageable);

	BattleSelectSubject findOneBySubjectId(String subjectId);

	List<BattleSelectSubject> findAllByFactoryIdAndIsDel(String factoryId, int isDel, Pageable pageable);

}
