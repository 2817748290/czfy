package party.artemis.czfy.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IIndexService;

@Controller
@RequestMapping(value="/client")
public class IndexController {
	private static Logger log = Logger.getLogger(IndexController.class);
	
	@Resource(name="indexService")
	private IIndexService indexService;
	
	@RequestMapping(value = "/getindexinfo")
	@ResponseBody
	public JSONObject getIndexInfo(HttpServletRequest req,HttpServletResponse res){
		JSONObject retjson = new JSONObject();
		
		JSONObject info = indexService.getIndexInfo();
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("info", info);
		
		
		return retjson;
	}
}
