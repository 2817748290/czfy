package party.artemis.czfy.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.INewsService;

@RequestMapping(value="/client")
@Controller
public class NewsController {
	private static Logger log = Logger.getLogger(NewsController.class);
	
	@Resource(name="newsService")
	private INewsService newsService;
	
	@RequestMapping(value="/getnewslist")
	@ResponseBody
	public JSONObject getNewsList(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String,Object>> newsList =  newsService.getNewsList();
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("newsList", newsList);
		
		return retjson;
	}
	
	@ResponseBody
	@RequestMapping(value="/getnewsbyid")
	public JSONObject getNewsById(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int newsId = req.getIntValue("newsId");
		
		List<Map<String, Object>>  news = newsService.getNewsById(newsId);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("news",news);
		
		return retjson;
	}

	//多条件查询新闻（标题和风格）
	@ResponseBody
	@RequestMapping(value = "/getNewsInfo")
	public JSONObject getNewsInfo(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		String newsTitle = req.getString("newsTitle");
		String newsStyle = req.getString("newsStyle");

		List<Map<String, Object>>  news = newsService.getNewsByMore(newsTitle,newsStyle);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("news",news);

		return retjson;
	}
}
