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
import party.artemis.czfy.service.IActorService;

@Controller
@RequestMapping(value="/client")
public class ActorController {
	private static Logger log = Logger.getLogger(ActorController.class);

	@Resource(name="actorService")
	private IActorService actorService;

	@RequestMapping(value="/getactorlist")
	@ResponseBody
	public JSONObject getActorList(){
		JSONObject retjson = new JSONObject();

		List<Map<String, Object>> actorList = actorService.getActorList();

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("actors", actorList);

		return retjson;

	}

	@RequestMapping(value="/getactorbyid")
	@ResponseBody
	public JSONObject getActorById(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int actorId = req.getIntValue("actorId");

		JSONObject actor = actorService.getActorById(actorId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("actorInfo", actor);

		return retjson;
	}

	@RequestMapping(value="/queryactor")
	@ResponseBody
	public JSONObject queryActor(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int classificationId = req.getIntValue("classificationId");
		int actorLevel = req.getIntValue("actorLevel");
		int page  = req.getIntValue("page");

		List<Map<String, Object>>  actorList = actorService.queryActor(classificationId,actorLevel,page);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

		retjson.put("actors", actorList);

		return retjson;
	}

	//获取总的页数
	@RequestMapping(value = "/getActorSumPage")
	@ResponseBody
	public JSONObject getSumPage(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:"+ body);

		JSONObject req = JSONObject.parseObject(body);

		int classificationId = req.getIntValue("classificationId");
		int actorLevel = req.getIntValue("actorLevel");

		Integer sumActor = actorService.getSumPage(classificationId, actorLevel);
		Integer sumActorPage = 0;

		if(sumActor%4>0){
			sumActorPage = (sumActor/4) + 1;
		}else {
			sumActorPage = sumActor/4;
		}

		System.out.println("总页数为：   " + sumActorPage);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

		retjson.put("sumPage", sumActorPage);

		return retjson;
	}
}
