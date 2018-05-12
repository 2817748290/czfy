package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IActorService {
	List<Map<String,Object>> getActorList();
	JSONObject getActorById(int actorId);

	List<Map<String,Object>> queryActor(int classificationId,int actorLevel,int page);
	//	分页
	Integer getSumPage(int classificationId,int actorLevel);

}
