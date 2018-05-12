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
import party.artemis.czfy.service.IVideoService;

@Controller
@RequestMapping(value="/client")
public class VideoController {
	private static Logger log = Logger.getLogger(VideoController.class);

	@Resource(name="videoService")
	private IVideoService videoService;

	@RequestMapping(value="/getvideo")
	@ResponseBody
	public JSONObject getVideo(){
		JSONObject retjson = new JSONObject();

		List<Map<String,Object>> videoList = videoService.getVideo();

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("videos", videoList);

		return retjson;
	}

	@RequestMapping(value="/getvideobyid")
	@ResponseBody
	public JSONObject getVideoById(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int videoId = req.getIntValue("videoId");

		JSONObject video = videoService.getVideoById(videoId);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("video", video.get("video"));

		return retjson;
	}

	//根据视频名字模糊查询
	@RequestMapping(value = "/getVideoByName")
	@ResponseBody
	public JSONObject getVideoByName(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:"+ body);

		JSONObject req = JSONObject.parseObject(body);

		String videoName = req.getString("videoName");

		int page =req.getInteger("page");

		List<Map<String, Object>> videopage = videoService.getVideoByName(videoName, page);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

		retjson.put("videopage", videopage);

		return retjson;

	}


	//获取总的页数
	@RequestMapping(value = "/getVideoSumPage")
	@ResponseBody
	public JSONObject getSumPage(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:"+ body);

		JSONObject req = JSONObject.parseObject(body);

		String videoName = req.getString("videoName");

		Integer sumVideo = videoService.getSumPage(videoName);
		Integer sumVideoPage = 0;

		if(sumVideo%4>0){
			sumVideoPage = (sumVideo/4) + 1;
		}else {
			sumVideoPage = sumVideo/4;
		}

		System.out.println("总页数为：   " + sumVideoPage);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

		retjson.put("sumPage", sumVideoPage);

		return retjson;
	}

}
