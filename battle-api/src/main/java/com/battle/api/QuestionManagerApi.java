package com.battle.api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.Battle;
import com.battle.domain.BattleFactory;
import com.battle.domain.BattlePeriod;
import com.battle.domain.BattlePeriodStage;
import com.battle.domain.BattleQuestion;
import com.battle.domain.BattleSelectSubject;
import com.battle.domain.BattleSubject;
import com.battle.domain.Context;
import com.battle.domain.Question;
import com.battle.domain.QuestionOption;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleFactoryService;
import com.battle.service.BattlePeriodService;
import com.battle.service.BattlePeriodStageService;
import com.battle.service.BattleQuestionService;
import com.battle.service.BattleSelectSubjectService;
import com.battle.service.BattleService;
import com.battle.service.BattleSubjectService;
import com.battle.service.ContextService;
import com.battle.service.QuestionOptionService;
import com.battle.service.QuestionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Controller
@RequestMapping("/api/battle/manager/question")
public class QuestionManagerApi {

	@Autowired
	private BattlePeriodStageService battlePeriodStageService;
	
	@Autowired
	private BattleSubjectService battleSubjectService;
	
	@Autowired
	private BattlePeriodService battlePeriodService;
	
	@Autowired
	private BattleQuestionService battleQuestionService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionOptionService questionOptionService;
	
	@Autowired
	private ContextService contextService;
	
	@Autowired
	private BattleFactoryService battleFactoryService;
	
	@Autowired
	private BattleService battleService;
	
	@Autowired
	private BattleSelectSubjectService battleSelectSubjectService;
	
	
	@RequestMapping("/battleFactorys")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object battleFactorys(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		List<BattleFactory> battleFactories = battleFactoryService.findAllByUserId(userInfo.getId());
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleFactories);
		
		return resultVo;
	}
	
	@RequestMapping("/battleFactory")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object battleFactory(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		String factoryId = httpServletRequest.getParameter("factoryId");
		
		BattleFactory battleFactory = null;
		if(CommonUtil.isNotEmpty(factoryId)){
			battleFactory = battleFactoryService.findOne(factoryId);
		}else{
			List<BattleFactory> battleFactories = battleFactoryService.findAllByUserId(userInfo.getId());
			
			if(battleFactories.size()>0){
				battleFactory = battleFactories.get(0);
			}
		}
		
		if(battleFactory==null){
			/*
			battleFactory = new BattleFactory();
			Battle battle = new Battle();
			battle.setHeadImg("");
			battle.setIndex(0);
			battle.setName(userInfo.getNickname()+"创建的");
			battle.setStatus(Battle.IN_STATUS);
			battle.setIsDisplay(0);
			battleService.add(battle);;
			
			BattlePeriod battlePeriod = new BattlePeriod();
			battlePeriod.setAuthorBattleUserId(userInfo.getId());
			battlePeriod.setBattleId(battle.getId());
			battlePeriod.setIndex(0);
			battlePeriod.setIsDefault(1);
			battlePeriod.setIsPublic(0);
			battlePeriod.setMaxMembers(0);
			battlePeriod.setMinMembers(0);
			battlePeriod.setOwnerImg(userInfo.getHeadimgurl());
			battlePeriod.setOwnerNickname(userInfo.getNickname());
			battlePeriod.setRightCount(0);
			battlePeriod.setStageCount(0);
			battlePeriod.setStatus(BattlePeriod.IN_STATUS);
			battlePeriod.setTakepartCount(0);
			battlePeriod.setTotalDistance(0);
			battlePeriod.setUnit(0);
			battlePeriod.setWrongCount(0);
			
			battlePeriodService.add(battlePeriod);
			
			battleFactory.setBattleId(battle.getId());
			battleFactory.setPeriodId(battlePeriod.getId());
			battleFactory.setUserId(userInfo.getId());
			battleFactory.setImgUrl(userInfo.getHeadimgurl());
			battleFactoryService.add(battleFactory);*/
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleFactory);
		return resultVo;
	}

	@RequestMapping("/addSubject")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public Object addSubject(HttpServletRequest httpServletRequest)throws Exception{
		
		String factoryId = httpServletRequest.getParameter("factoryId");
		BattleFactory battleFactory = battleFactoryService.findOne(factoryId);
	
		String imgUrl = httpServletRequest.getParameter("imgUrl");
		String name = httpServletRequest.getParameter("name");
		
		BattleSubject battleSubject = new BattleSubject();
		battleSubject.setBattleId(battleFactory.getBattleId());
		battleSubject.setImgUrl(imgUrl);
		battleSubject.setIsDel(0);
		battleSubject.setName(name);
		battleSubject.setSeq(1);
		
		battleSubjectService.add(battleSubject);
		
		BattleSelectSubject battleSelectSubject = new BattleSelectSubject();
		battleSelectSubject.setBattleId(battleFactory.getBattleId());
		battleSelectSubject.setFactoryId(battleFactory.getId());
		battleSelectSubject.setImgUrl(imgUrl);
		battleSelectSubject.setIndex(1);
		battleSelectSubject.setIsDel(0);
		battleSelectSubject.setName(name);
		battleSelectSubject.setNum(100);
		battleSelectSubject.setPeriodId(battleFactory.getPeriodId());
		battleSelectSubject.setSubjectId(battleSubject.getId());
		battleSelectSubjectService.add(battleSelectSubject);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	@RequestMapping("/subjects")
	@ResponseBody
	public Object subjects(HttpServletRequest httpServletRequest){
		
		String battleId = httpServletRequest.getParameter("battleId");
		List<BattleSubject> battleSubjects = battleSubjectService.findAllByBattleIdAndIsDelOrderBySeqAsc(battleId, 0);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleSubjects);
		return resultVo;
	}
	
	@RequestMapping("/periods")
	@ResponseBody
	public Object periods(HttpServletRequest httpServletRequest){
		
		String battleId = httpServletRequest.getParameter("battleId");
		List<BattlePeriod> battlePeriods = battlePeriodService.findAllByBattleIdOrderByIndexAsc(battleId);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battlePeriods);
		return resultVo;
	}
	
	@RequestMapping("/delSubject")
	@ResponseBody
	@Transactional
	public Object delSubject(HttpServletRequest httpServletRequest){
		String subjectId = httpServletRequest.getParameter("subjectId");
		
		List<String> subjectIds = new ArrayList<>();
		subjectIds.add(subjectId);
		
		List<Object[]> questionNums = battleQuestionService.getQuestionNumBySubjectIds(subjectIds);
		
		if(questionNums!=null&&questionNums.size()>0){
			Object[] questionNum = questionNums.get(0);
			if(Integer.parseInt(questionNum[0].toString())>0){
				ResultVo resultVo = new ResultVo();
				resultVo.setSuccess(false);
				resultVo.setErrorCode(0);
				return resultVo;
			}
		}
		
		BattleSubject battleSubject = battleSubjectService.findOne(subjectId);
		
		BattleSelectSubject battleSelectSubject = battleSelectSubjectService.findOneBySubjectId(subjectId);
		
		battleSelectSubject.setIsDel(1);
		battleSubject.setIsDel(1);
		
		battleSelectSubjectService.update(battleSelectSubject);
		battleSubjectService.update(battleSubject);;
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
		
		
	}
	
	@RequestMapping("/delQuestion")
	@ResponseBody
	@Transactional
	public Object delQuestion(HttpServletRequest httpServletRequest){
		
		String questionId = httpServletRequest.getParameter("questionId");
		
		BattleQuestion battleQuestion = battleQuestionService.findOne(questionId);
		
		Question question = questionService.findOne(battleQuestion.getQuestionId());
		
		if(question.getType().intValue()==Question.SELECT_TYPE){
			List<QuestionOption> questionOptions = questionOptionService.findAllByQuestionId(question.getId());
			for(QuestionOption questionOption:questionOptions){
				questionOption.setIsDel(1);
				questionOptionService.update(questionOption);
			}
			question.setIsDel(1);
			questionService.update(question);
		}
		
		battleQuestion.setIsDel(1);
		battleQuestionService.update(battleQuestion);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	
	@RequestMapping("/questions")
	@ResponseBody
	public Object questions(HttpServletRequest httpServletRequest){
		
		String battleId = httpServletRequest.getParameter("battleId");
		
		
		String subjectId = httpServletRequest.getParameter("subjectId");
		
		String[] subjectIds = new String[]{
				subjectId
		};
		List<BattleQuestion> battleQuestions = new ArrayList<>();
		
		if(!CommonUtil.isEmpty(battleId)&&!CommonUtil.isEmpty(battleId)&&!CommonUtil.isEmpty(subjectId)){
			battleQuestions = battleQuestionService.findAllByBattleIdAndSubjectIdInAndIsDel(battleId, subjectIds,0);
		}
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleQuestions);
		return resultVo;
	}
	
	
	@RequestMapping(value="info")
	@ResponseBody
	@Transactional
	public Object info(HttpServletRequest httpServletRequest)throws Exception{
		String id = httpServletRequest.getParameter("id");
		
		BattleQuestion battleQuestion = battleQuestionService.findOne(id);
		
		Question question = questionService.findOne(battleQuestion.getQuestionId());
		
		List<QuestionOption> questionOptions = questionOptionService.findAllByQuestionId(question.getId());
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("id", battleQuestion.getId());
		data.put("answer", battleQuestion.getAnswer());
		data.put("battleId", battleQuestion.getBattleId());
		data.put("battlePeriodId", battleQuestion.getPeriodId());
		data.put("battleSubjectId", battleQuestion.getSubjectId());
		data.put("imgUrl", battleQuestion.getImgUrl());
		data.put("name", battleQuestion.getName());
		
		data.put("periodStageId", battleQuestion.getPeriodId());
		data.put("question", battleQuestion.getQuestion());
		data.put("questionId", battleQuestion.getQuestionId());
		data.put("seq", battleQuestion.getSeq());
		data.put("type", battleQuestion.getType());
		
		data.put("options", questionOptions);
		if(question!=null){
			data.put("fillWords", question.getFillWords());
		}
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
	}
	
	
	
	
	@RequestMapping(value="addQuestion")
	@ResponseBody
	@Transactional
	public Object addQuestion(HttpServletRequest httpServletRequest)throws Exception{
		String subjectId = httpServletRequest.getParameter("subjectId");
		
		String questionType = httpServletRequest.getParameter("questionType");
		
		String question = httpServletRequest.getParameter("question");
		
		String imgUrl = httpServletRequest.getParameter("imgUrl");
		
		String answer = httpServletRequest.getParameter("answer");
		
		String fillWords = httpServletRequest.getParameter("fillWords");
		
		String battleId = httpServletRequest.getParameter("battleId");
		
		String periodId =httpServletRequest.getParameter("periodId");
		
		Context context = contextService.findOneByCodeBySync(Context.QUESTION_MAX_INDEX_CODE);
		if(context==null){
			context = new Context();
			context.setCode(Context.QUESTION_MAX_INDEX_CODE);
			context.setValue("1");
			contextService.add(context);
		}else{
			String value = context.getValue();
			Integer index = Integer.parseInt(value);
			index++;
			context.setValue(index+"");
			contextService.update(context);
		}
		
		Question questionTarget = new Question();
		questionTarget.setQuestion(question);
		questionTarget.setImgUrl(imgUrl);
		if(!CommonUtil.isEmpty(imgUrl)){
			questionTarget.setIsImg(1);
		}else{
			questionTarget.setIsImg(0);
		}
		
		questionTarget.setIndex(Integer.parseInt(context.getValue()));
		questionTarget.setAnswer(answer);
		questionTarget.setFillWords(fillWords);
		questionTarget.setIsDel(0);
		questionTarget.setSource(Question.MANAGER_SOURCE);
		
		questionService.add(questionTarget);
		
		BattleQuestion battleQuestion = new BattleQuestion();
		battleQuestion.setBattleId(battleId);
		battleQuestion.setPeriodId(periodId);
		battleQuestion.setSubjectId(subjectId);
		battleQuestion.setImgUrl(imgUrl);
		battleQuestion.setName("");
		battleQuestion.setAnswer(answer);
		battleQuestion.setQuestion(question);
		battleQuestion.setIsDel(0);
		
		battleQuestion.setQuestionId(questionTarget.getId());
		battleQuestionService.add(battleQuestion);
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<List<Map<String, String>>> typeReference = new TypeReference<List<Map<String,String>>>() {
		};
		
		//选择题
		if(questionType.equals("0")){
			
			questionTarget.setType(0);
			battleQuestion.setType(0);
			
			String options = httpServletRequest.getParameter("options");
			List<Map<String, String>> questionOptions = objectMapper.readValue(options, typeReference);
			
			StringBuffer sbOptions = new StringBuffer();
			
			for(Map<String, String> questionOptionMap:questionOptions){
				QuestionOption questionOption = new QuestionOption();
				questionOption.setQuestionId(questionTarget.getId());
				questionOption.setSeq(Integer.parseInt(questionOptionMap.get("seq")));
				questionOption.setIsDel(0);
				questionOption.setContent(questionOptionMap.get("content"));
				questionOptionService.add(questionOption);
				String isRight = questionOptionMap.get("isRight");
				
				sbOptions.append(questionOption.getContent()+",");
				
				if(!CommonUtil.isEmpty(isRight)&&isRight.equals("1")){
					questionTarget.setRightOptionId(questionOption.getId());
					questionTarget.setAnswer(questionOption.getContent());
					
					battleQuestion.setAnswer(questionOption.getContent());
				}
			}
			
			if(questionOptions!=null&&questionOptions.size()>0){
				sbOptions.deleteCharAt(sbOptions.lastIndexOf(","));
			}
			battleQuestion.setOptions(sbOptions.toString());
		}else if(questionType.equals("1")){
			questionTarget.setType(1);
			battleQuestion.setType(1);
		}else if(questionType.equals("2")){
			questionTarget.setType(2);
			battleQuestion.setType(2);
		}
		battleQuestionService.update(battleQuestion);
		
		questionService.update(questionTarget);
		
		
	
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
	@RequestMapping(value="updateQuestion")
	@ResponseBody
	@Transactional
	public Object updateQuestion(HttpServletRequest httpServletRequest)throws Exception{
		String battleQuestionId = httpServletRequest.getParameter("battleQuestionId");
		
		String subjectId = httpServletRequest.getParameter("subjectId");
		
		String questionType = httpServletRequest.getParameter("questionType");
		
		String question = httpServletRequest.getParameter("question");
		
		String imgUrl = httpServletRequest.getParameter("imgUrl");
		
		String answer = httpServletRequest.getParameter("answer");
		
		String fillWords = httpServletRequest.getParameter("fillWords");
	
		
		
		
		BattleQuestion battleQuestion = battleQuestionService.findOne(battleQuestionId);
		
		
		Question questionTarget = questionService.findOne(battleQuestion.getQuestionId());
		
		questionTarget.setQuestion(question);
		questionTarget.setImgUrl(imgUrl);
		questionTarget.setIsImg(1);
		questionTarget.setAnswer(answer);
		questionTarget.setFillWords(fillWords);
		
		battleQuestion.setSubjectId(subjectId);
		battleQuestion.setImgUrl(imgUrl);
		battleQuestion.setName("");
		battleQuestion.setAnswer(answer);
		battleQuestion.setQuestion(question);
		
		battleQuestion.setQuestionId(questionTarget.getId());
		
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<List<Map<String, String>>> typeReference = new TypeReference<List<Map<String,String>>>() {
		};
		
		//选择题
		if(questionType.equals("0")){
			
			questionTarget.setType(0);
			battleQuestion.setType(0);
			
			String options = httpServletRequest.getParameter("options");
			List<Map<String, String>> questionOptions = objectMapper.readValue(options, typeReference);
			
			StringBuffer sbOptions = new StringBuffer();
			
			for(Map<String, String> questionOptionMap:questionOptions){
				String id = questionOptionMap.get("id");
				QuestionOption questionOption = questionOptionService.findOne(id);
				questionOption.setQuestionId(questionTarget.getId());
				questionOption.setSeq(Integer.parseInt(questionOptionMap.get("seq")));
				questionOption.setIsDel(0);
				questionOption.setContent(questionOptionMap.get("content"));
				questionOptionService.update(questionOption);
				String isRight = questionOptionMap.get("isRight");
				
				sbOptions.append(questionOption.getContent()+",");
				
				if(!CommonUtil.isEmpty(isRight)&&isRight.equals("1")){
					questionTarget.setRightOptionId(questionOption.getId());
					questionTarget.setAnswer(questionOption.getContent());
					battleQuestion.setAnswer(questionOption.getContent());
				}
			}
			
			if(questionOptions!=null&&questionOptions.size()>0){
				sbOptions.deleteCharAt(sbOptions.lastIndexOf(","));
			}
			battleQuestion.setOptions(sbOptions.toString());
		}else if(questionType.equals("1")){
			questionTarget.setType(1);
			battleQuestion.setType(1);
		}else if(questionType.equals("2")){
			questionTarget.setType(2);
			battleQuestion.setType(2);
		}
		battleQuestionService.update(battleQuestion);
		
		questionService.update(questionTarget);

		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
	}
	
}
