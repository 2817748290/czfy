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
import party.artemis.czfy.service.IProjectService;

@Controller
@RequestMapping(value="/client")
public class ProjectController {
	private static Logger log = Logger.getLogger(ProjectController.class);
	
	@Resource(name="projectService")
	private IProjectService projectService;
	
	@RequestMapping(value="/getclassification")
	@ResponseBody
	public JSONObject getClassification(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String, Object>> classification = projectService.getClaasification();
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("classifications", classification);
		
		return retjson;
		
	}
	
	@RequestMapping(value="/getprojectlist")
	@ResponseBody
	public JSONObject getProjectList(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String, Object>> projectList = projectService.getProjectList();

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("projects", projectList);
		
		return retjson;
	}
	
	@RequestMapping(value="/getprojectbyid")
	@ResponseBody
	public JSONObject getProjectById(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int projectId = req.getIntValue("projectId");
		
		JSONObject project = projectService.getProjectById(projectId);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("project", project.get("project"));
		
		return retjson;
	}
	
	@RequestMapping(value="/queryproject")
	@ResponseBody
	public JSONObject queryProject(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		
		String projectName = req.getString("projectName");
		int classificationId = req.getIntValue("classificationId");
		int levelCode = req.getIntValue("levelCode");
		
		List<Map<String, Object>>  projectList = projectService.queryProject(projectName, classificationId, levelCode);
		

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("projects", projectList);
		
		return retjson;
	}

	@RequestMapping(value="/pageProject")
	@ResponseBody
	public JSONObject pageproject(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);


		String projectName = req.getString("projectName");
		int classificationId = req.getIntValue("classificationId");
		int levelCode = req.getIntValue("levelCode");
		int page = req.getIntValue("page");
		List<Map<String, Object>>
				projectList = projectService.pageProject(projectName, classificationId, levelCode,page);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("projects", projectList);

		return retjson;
	}
	//获取总的页数
	@RequestMapping(value = "/getProjectSumPage")
	@ResponseBody
	public JSONObject getSumPage(@RequestBody String body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:"+ body);

		JSONObject req = JSONObject.parseObject(body);

		String projectName = req.getString("projectName");
		int classificationId = req.getIntValue("classificationId");
		int levelCode = req.getIntValue("levelCode");
		Integer sumActor = projectService.getSumPage(projectName, classificationId, levelCode);
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
