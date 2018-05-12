package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public interface ILiteratureService {

	JSONObject getLiterature();

	List<Map<String, Object>> getLiteratureInfo();

	//模糊查询包含分页
	List<Map<String, Object>> queryInfo(String literatureName, int classId, int page);

	//获得分页总数
	Integer sumPage(String literatureName, int classId);

}
