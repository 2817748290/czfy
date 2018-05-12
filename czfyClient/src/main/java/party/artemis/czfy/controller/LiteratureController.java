package party.artemis.czfy.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.ILiteratureService;

@Controller
@RequestMapping(value = "/client")
public class LiteratureController {
	private static Logger log = Logger.getLogger(LiteratureController.class);
	
	@Resource(name="literatureService")
	private ILiteratureService literatureService;
	
	@RequestMapping(value="/getliterature")
	@ResponseBody
	public JSONObject getLiterature(){
		JSONObject retjson = new JSONObject();
		
		JSONObject literatureList = literatureService.getLiterature();
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("literatures", literatureList);
		
		return retjson;
	}

	@RequestMapping(value = "/getLiterature")
	@ResponseBody
	public JSONObject getInfo(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		List<Map<String,Object>> list = literatureService.getLiteratureInfo();

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("list", list);

		return retjson;
	}

	@RequestMapping(value = "/queryLiterature")
	@ResponseBody
	public JSONObject queryLiterature(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSON.parseObject(body);

		String literatureName = req.getString("literatureName");
		int classId = req.getIntValue("classId");
		int page = req.getIntValue("page");
		int finalPage = page*4;
		List<Map<String,Object>> list = literatureService.queryInfo(literatureName,classId,finalPage);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("list", list);

		return retjson;
	}

	@RequestMapping(value = "/getSumPage")
	@ResponseBody
	public JSONObject getSumPage(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSON.parseObject(body);

		String literatureName = req.getString("literatureName");
		int classId = req.getIntValue("classId");

		Integer sumPage = literatureService.sumPage(literatureName,classId);

		Integer page = 0;

		if(sumPage%4>0){
			 page = (sumPage/4) + 1;
		}else{
			 page = sumPage/4;
		}

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("page", page);

		return retjson;
	}
}
