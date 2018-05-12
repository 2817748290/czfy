package party.artemis.czfy.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;


public interface IActorService {
	//根据id获取艺人信息(图片路径)
	JSONObject getActorById(int actorId);

	//查找艺人类型
	List<Map<String,Object>> getActorType();
	//查找艺人级别
	List<Map<String,Object>> getActorLevelName();
	//添加艺人
	int addActor(String actorName, String actorIntroducation, int actorClassificationId, int actorLevel, Boolean isTop, long actorPhoto);
	//获取艺人总数
	List<Map<String,Object>> actorList();
	//根据id查询艺人的信息
	Map<String,Object> actorInfo(int actorId);
	//删除艺人
	void delActor(int actorId);
	//修改艺人信息
	void updateActor(int actorId, String actorName, String actorIntroducation, int actorClassificationId, Boolean isTop, int actorLevel);
	void uploadActorPhoto(String imageUrl);
	void updateActorPhoto(int actorId, String imageUrl);
	void updateActorWork(int actorId, List<String> imageUrlList);
	//多条件查询艺人
	List<Map<String,Object>> queryActor(String actorName, int actorClassificationId, int actorLevel);
	//是否显示
	void updateIsPass(int actorId);
}
