package com.battle.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.Question;
import com.battle.domain.QuestionOption;
import com.battle.executer.BattleRoomConnector;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.QuestionAnswerVo;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.QuestionOptionService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping(value="/api/battle")
public class BattleApi {
	@Autowired
	private BattleRoomConnector battleRoomConnector;
	
	@Autowired
	private QuestionOptionService questionOptionService;
	
	
	@RequestMapping(value="superLove")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo superLove(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);	
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		boolean success = battleRoomConnector.superLove(id, userInfo);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(success);
		return resultVo;
		
	}
	
	@RequestMapping(value="takepart")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo takepart(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);	
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		BattleRoomMemberVo battleRoomMemberVo = battleRoomConnector.takepart(id, userInfo);
		
		if(battleRoomMemberVo!=null){
			List<BattleRoomMemberVo> battleRoomMembers = battleRoomConnector.members(id);
			
			BattleRoomVo battleRoom = battleRoomConnector.getRoom(id);
			Map<String, Object> data = new HashMap<>();
			data.put("id", battleRoom.getId());
			data.put("memberInfo", battleRoomMemberVo);
			data.put("members",battleRoomMembers);
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(true);
			resultVo.setData(data);
			return resultVo;
		}else{
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
	}

	@RequestMapping(value="answerQuestion")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo answerQuestion(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);	
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String roomId = httpServletRequest.getParameter("roomId");
		String answer = httpServletRequest.getParameter("answer");
		String optionId = httpServletRequest.getParameter("optionId");
		String type = httpServletRequest.getParameter("type");
		

		QuestionAnswerVo questionAnswer = new QuestionAnswerVo();
		questionAnswer.setMyAnswer(answer);
		questionAnswer.setOptionId(optionId);
		questionAnswer.setRoomId(roomId);
		questionAnswer.setUserId(userInfo.getId());
		questionAnswer.setUserImg(userInfo.getHeadimgurl());
		questionAnswer.setUserName(userInfo.getNickname());
		questionAnswer.setType(Integer.parseInt(type));
		

		if(Integer.parseInt(type)==Question.SELECT_TYPE){
			QuestionOption questionOption = questionOptionService.findOne(optionId);
			if(questionOption!=null){
				questionAnswer.setMyAnswer(questionOption.getContent());
			}
		}
		battleRoomConnector.answerQuestion(questionAnswer);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	
	@RequestMapping(value="signOut")
	@ResponseBody
	@Transactional
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
	
	
	@RequestMapping(value="subjectReady")
	@ResponseBody
	@Transactional
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
	
	
	@RequestMapping(value="subjectSelect")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo subjectSelect(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String roomId = httpServletRequest.getParameter("roomId");
		
		String subjectId = httpServletRequest.getParameter("subjectId");
		
		battleRoomConnector.subjectSelect(roomId,subjectId,userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	
	
	@RequestMapping(value="doDouble")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo doDouble(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String roomId = httpServletRequest.getParameter("roomId");
		
		battleRoomConnector.doDouble(roomId, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	
	@RequestMapping(value="doNotDouble")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo doNotDouble(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String roomId = httpServletRequest.getParameter("roomId");
		
		battleRoomConnector.doNotDouble(roomId, userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
}
