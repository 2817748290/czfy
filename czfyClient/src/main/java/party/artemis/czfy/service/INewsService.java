package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

public interface INewsService {
	List<Map<String,Object>> getNewsList();
	List<Map<String, Object>> getNewsById(int newsId);
	//根据视频姓名进行模糊查询
	List<Map<String,Object>> getNewsByMore(String newsTitle,String newsStyle);
}
