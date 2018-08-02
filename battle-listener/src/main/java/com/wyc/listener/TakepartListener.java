package com.wyc.listener;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import com.battle.domain.PersonalSpace;
import com.battle.event.vo.TakepartEvent;
import com.battle.event.vo.TakepartVo;
import com.battle.service.PersonalSpaceService;

@Service
public class TakepartListener  implements ApplicationListener<TakepartEvent>{

	@Autowired
	private PersonalSpaceService personalSpaceService;
	
	@Override
	public void onApplicationEvent(TakepartEvent event) {
		TakepartVo takepartVo = event.getSource();

		PersonalSpace personalSpace = personalSpaceService.findOneByUserIdAndRankIdAndType(takepartVo.getUserId(),takepartVo.getRankId(),PersonalSpace.RANK_TYPE);
		
		if(personalSpace==null){
			PersonalSpace rootPersonalSpace = personalSpaceService.findOneByRankIdAndIsRoot(takepartVo.getRankId(),1);
			
			String img1 = rootPersonalSpace.getImg1();
			String img2 = rootPersonalSpace.getImg2();
			String img3 = rootPersonalSpace.getImg3();
			String img4 = rootPersonalSpace.getImg4();
			String img5 = rootPersonalSpace.getImg5();
			String img6 = rootPersonalSpace.getImg6();
			String img7 = rootPersonalSpace.getImg7();
			String img8 = rootPersonalSpace.getImg8();
			String img9 = rootPersonalSpace.getImg9();

			personalSpace = new PersonalSpace();
			personalSpace.setActivityTime(new DateTime());
			personalSpace.setDetail(rootPersonalSpace.getDetail());
			personalSpace.setIsPublic(0);
			personalSpace.setName(rootPersonalSpace.getName());
			personalSpace.setRankId(takepartVo.getRankId());
			personalSpace.setType(PersonalSpace.RANK_TYPE);
			personalSpace.setIsDel(0);
			personalSpace.setIsRoot(0);
			personalSpace.setImgNum(rootPersonalSpace.getImgNum());
			personalSpace.setImg1(img1);
			personalSpace.setImg2(img2);
			personalSpace.setImg3(img3);
			personalSpace.setImg4(img4);
			personalSpace.setImg5(img5);
			personalSpace.setImg6(img6);
			personalSpace.setImg7(img7);
			personalSpace.setImg8(img8);
			personalSpace.setImg9(img9);
			personalSpaceService.add(personalSpace);

		}
	}

}
