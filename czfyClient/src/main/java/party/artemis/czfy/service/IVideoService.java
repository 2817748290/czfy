package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IVideoService {

	List<Map<String,Object>> getVideo();
	JSONObject getVideoById(int videoId);

	//根据视频姓名进行模糊查询
	Integer getSumPage(String videoName);

	List<Map<String,Object>> getVideoByName(String videoName, int page);
}
