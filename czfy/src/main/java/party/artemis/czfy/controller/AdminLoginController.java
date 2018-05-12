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
import party.artemis.czfy.service.IAdminLoginService;

@RequestMapping(value="/admin")
@Controller
public class AdminLoginController {
	private static Logger log = Logger.getLogger(AdminLoginController.class);
	
	@Resource(name="adminLoginService")
	private IAdminLoginService adminLoginService;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject Login(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		String nextPage = "/login.html";
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String username = req.getString("username");
		String password = req.getString("password");
		
		List<Map<String,Object>> adminList = adminLoginService.adminLogin(username, password);
		
		if(!adminList.isEmpty()){
			nextPage = "/home.html";
			retjson.put("admin", adminList.get(0));
		}
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("nextPage", nextPage);
		return retjson;
	}
	
	
}
