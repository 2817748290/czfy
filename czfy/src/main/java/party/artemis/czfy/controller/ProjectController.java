package party.artemis.czfy.controller;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IProjectService;

@RequestMapping(value="/admin")
@Controller
public class ProjectController {
	private static Logger log = Logger.getLogger(ProjectController.class);
	
	@Resource(name="projectService")
	private IProjectService projectService;
	
	@RequestMapping(value="/uploadimage",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject uploadImage(MultipartHttpServletRequest request) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();
		
		String imageStoreName = "";
		
		Iterator<String> it = request.getFileNames();
		while(it.hasNext()){
			MultipartFile file = request.getFile(it.next().toString());
			if(file!=null){
				File picturedir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
				if(!picturedir.exists()) {
	        		picturedir.mkdirs();
	        	}
				imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
				System.out.println(picturedir.getPath());
				String filePath = picturedir.getPath() + "/" + imageStoreName;
				file.transferTo(new File(filePath));
				retjson.put("imageUrl",  Constants.IP_ADDRESS + Constants.PICTURE_DIR + imageStoreName);
			}
		}
		
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/publishproject",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject publishProject(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String projectName = req.getString("projectName");
		int projectLevel = req.getIntValue("projectLevel");
		int projectClassification = req.getIntValue("projectClassification");
		boolean isTop = req.getBooleanValue("isTop");
		String projectContent = req.getString("projectContent");
		
		int projectId = projectService.publishProject(projectName, projectLevel, 
				projectClassification, isTop, projectContent);
		
		retjson.put("projectId", projectId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/getprojecttype",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getProjectType(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String, Object>> typeList = projectService.getProjectType();
		
		retjson.put("typeList", typeList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/getProjectLevel",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getProjectLevel(){
		JSONObject retjson = new JSONObject();

		List<Map<String, Object>> levelList = projectService.getProjectLevel();

		retjson.put("levelList", levelList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/projectlist",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject projectList(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String, Object>> projectList = projectService.projectList();
		
		retjson.put("projectList", projectList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	/**
	 * 多条件查询项目
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/queryproject",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject queryProject(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String projectName = req.getString("projectName");
		int projectType = req.getIntValue("projectType");
		int projectLevel = req.getIntValue("projectLevel");
		
		List<Map<String, Object>> projectList = projectService.queryProject(projectName,
				projectType,projectLevel);
		
		retjson.put("projectList", projectList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/projectinfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject projectInfo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int projectId = req.getIntValue("projectId");
		
		Map<String, Object> project = projectService.projectInfo(projectId);
		
		retjson.put("project", project);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/updateproject",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject updateProject(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String projectName = req.getString("projectName");
		int projectLevel = req.getIntValue("projectLevel");
		int projectClassification = req.getIntValue("projectClassification");
		boolean isTop = req.getBooleanValue("isTop");
		String projectContent = req.getString("projectContent");
		int projectId = req.getIntValue("projectId");
		
		projectService.updateProject(projectName, projectLevel, projectId, projectClassification, isTop, projectContent);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
		
	}
	
	@RequestMapping(value="/delproject",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteProject(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int projectId = req.getIntValue("projectId");
		
		projectService.deleteProject(projectId);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/updateprojectcover",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject updateProjectCover(MultipartHttpServletRequest request) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();
		
		int projectId = Integer.valueOf(request.getParameter("projectId"));
		
		String imageStoreName = "";
		
		Iterator<String> it = request.getFileNames();
		while(it.hasNext()){
			MultipartFile file = request.getFile(it.next().toString());
			if(file!=null){
				File picturedir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
				if(!picturedir.exists()) {
	        		picturedir.mkdirs();
	        	}
				imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
				String filePath = picturedir.getPath() + "/" + imageStoreName;
				file.transferTo(new File(filePath));
				projectService.updateProjectCover(projectId,imageStoreName);
			}
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
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
}
