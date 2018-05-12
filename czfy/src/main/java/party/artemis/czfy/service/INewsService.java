package party.artemis.czfy.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface INewsService {
	int addNews(String newsTitle, String newType, Boolean isTop,int isPass);
	List<Map<String,Object>> newsList();
	//多条件查询新闻
	List<Map<String,Object>> queryNews(String newsTitle, String newType, int isPass);
	Map<String,Object> newsInfo(int newsId);
	//修改新闻
	void updateNews(int newsId, String newsTitle, String newType, Boolean isTop, int isPass);
	void uploadNewsImage(int newsId, String imageUrl);
	void delNews(int newsId);
	//审核新闻
	void isPassNews(int isPass, int newsId);
	//获取审核值对应项
	List<Map<String,Object>> getPassinfo();
	//获取之前审核的情况
	Map<String,Object> getPassState(int newsId);
	//根据id获取新闻信息
	List<Map<String, Object>> getNewsById(int newsId);

	//111
	void uploadActorPhoto(String imageUrl);
	void updateActorPhoto(int newsId, String imageUrl);
	void updateActorWork(int newsId, List<String> imageUrlList);
	//根据id获取艺人信息(图片路径)
	JSONObject getNewsInfoById(int newsId);
}
