package com.battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleSelectPaperDao;

@Service
public class BattleSelectPaperService {

	@Autowired
	private BattleSelectPaperDao battleSelectPaperDao;
}
