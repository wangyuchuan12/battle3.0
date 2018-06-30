//package com.battle.api;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//
//import org.jboss.jandex.Main;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.battle.domain.BattleQuestionDistribution;
//import com.battle.executer.BattleRoomConnector;
//import com.battle.executer.BattleRoomDataManager;
//import com.battle.executer.imp.BattleRoomDataManagerImp;
//import com.battle.executer.vo.BattlePaperVo;
//import com.battle.service.other.BattleQuestionDistributionHandleService;
//
//import io.reactivex.Observable;
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//
//@Controller
//@RequestMapping(value="/api/test")
//public class TestApi {
//	
//	public static void main(String[]args){
//		Observer<String> observer = new Observer<String>() {
//
//			@Override
//			public void onSubscribe(Disposable d) {
//				System.out.println(".....onSubscribe");
//				
//			}
//
//			@Override
//			public void onNext(String t) {
//				System.out.println(".....onNext:"+t);
//				
//			}
//
//			@Override
//			public void onError(Throwable e) {
//				System.out.println(".....onError");
//				
//			}
//
//			@Override
//			public void onComplete() {
//				System.out.println(".....onComplete");
//				
//			}  
//		};  
//		
//		Observable<String> observable = Observable.just("Hello", "Hi", "Aloha").map(new Function<String, String>() {
//
//			@Override
//			public String apply(String t) throws Exception {
//				System.out.print("......t:"+t);
//				return "1";
//			}	
//		}).map(new Function<String, String>() {
//
//			@Override
//			public String apply(String t) throws Exception {
//				System.out.print("......t2:"+t);
//				return "2";
//			}
//			
//		});
//		
//		observable.subscribe(new Consumer<String>() {
//
//			@Override
//			public void accept(String k) throws Exception {
//				System.out.println(".........k:"+k);
//				
//			}
//		});
//		
//		observable.subscribe(observer);
//	}
//
//	@Autowired
//	private BattleQuestionDistributionHandleService battleQuestionDistributionHandleService;
//	
//	@Autowired
//    private AutowireCapableBeanFactory factory;
//	
//	@Autowired
//	private BattleRoomConnector battleRoomConnector;
//	
//	@RequestMapping(value="flushDistribution")
//	@ResponseBody
//	@Transactional
//	public void flushDistribution(HttpServletRequest httpServletRequest){
//		String id = httpServletRequest.getParameter("id");
//		BattleQuestionDistribution battleQuestionDistribution = battleQuestionDistributionHandleService.findOne(id);
//		battleQuestionDistributionHandleService.flushDistribution(battleQuestionDistribution);
//	}
//	
//	
//	@RequestMapping(value="removeRoom")
//	@ResponseBody
//	@Transactional
//	public void removeRoom(HttpServletRequest httpServletRequest){
//		String id = httpServletRequest.getParameter("id");
//		battleRoomConnector.removeExecuter(id);
//	}
//	
//	@RequestMapping(value="dataManagerTest")
//	@ResponseBody
//	@Transactional
//	public Object dataManagerTest(HttpServletRequest httpServletRequest){
//		String battleId = httpServletRequest.getParameter("battleId");
//		String periodId = httpServletRequest.getParameter("periodId");
//		String userIds = httpServletRequest.getParameter("userIds");
//		
//		List<String> userArray = new ArrayList<>();
//		
//		for(String userId:userIds.split(",")){
//			userArray.add(userId);
//		}
//		BattleRoomDataManager battleRoomDataManager = new BattleRoomDataManagerImp();
//		factory.autowireBean(battleRoomDataManager);
//		battleRoomDataManager.init(battleId, periodId, userArray);
//		
//		
//		BattlePaperVo battlePaperVo = battleRoomDataManager.getBattlePaper();
//		
//		return battlePaperVo;
//	}
//	
//}
