package com.battle.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.battle.executer.BattleRoomConnector;
import com.battle.executer.vo.QuestionAnswerVo;
import com.battle.filter.element.LoginStatusFilter;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle")
public class BattleApi {


	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	public ResultVo answerQuestion(HttpServletRequest httpServletRequest){
		
		QuestionAnswerVo questionAnswer = new QuestionAnswerVo();
		battleRoomConnector.answerQuestion(questionAnswer);
		return null;
	}
	
	
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo signOut(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		String roomId = httpServletRequest.getParameter("roomId");
		battleRoomConnector.signOut(roomId, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo startStage(HttpServletRequest httpServletRequest){
		String roomId = httpServletRequest.getParameter("roomId");
		String stageIndex = httpServletRequest.getParameter("stageIndex");
		battleRoomConnector.startStage(roomId, Integer.parseInt(stageIndex));
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo subjectReady(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String roomId = httpServletRequest.getParameter("roomId");
		
		battleRoomConnector.subjectReady(roomId, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
}
