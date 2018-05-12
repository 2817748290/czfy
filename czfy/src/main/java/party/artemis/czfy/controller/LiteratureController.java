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
import party.artemis.czfy.service.ILiteratureService;

@RequestMapping(value="/admin")
@Controller
public class LiteratureController {
	private static Logger log = Logger.getLogger(LiteratureController.class);
	
	@Resource(name="literatureService")
	private ILiteratureService literatureService;
	
	@RequestMapping(value="/addcontributors",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addContributors(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String contributorsName = req.getString("contributorsName");
		String literatureName = req.getString("literatureName");
		
		int literatureId = literatureService.addContributors(contributorsName, literatureName);
		
		retjson.put("literatureId", literatureId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/literatureupload",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject uploadLiterature(MultipartHttpServletRequest request) throws IllegalStateException, IOException{
		int literatureId =  Integer.valueOf(request.getParameter("literatureId"));
		
		JSONObject retjson = new JSONObject();
		
		String imageStoreName = "";
		String literatureStoreName = "";
		
		Iterator<String> it = request.getFileNames();
		while(it.hasNext()){
			//一次遍历所有文件
            MultipartFile file =request.getFile(it.next().toString());
            if(file!=null){
            	File picturedir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
            	File documentDir = new File(Constants.RESOURCE_PATH + Constants.DOCUMENT_DIR);
            	if(!picturedir.exists()) {
            		picturedir.mkdirs();
            	}
            	if(!documentDir.exists()){
            		documentDir.mkdirs();
            	}
            	log.debug(file.getContentType());
            	if(file.getContentType().equals("application/pdf")){
            		literatureStoreName = System.currentTimeMillis() + file.getOriginalFilename();
            		String filePath = documentDir.getPath() + "/" + literatureStoreName;
            		file.transferTo(new File(filePath));
            		literatureService.uploadLiterature(literatureId, literatureStoreName);
            	}else if(file.getContentType().startsWith("image/")){
            		imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
                	String filePath = picturedir.getPath() + "/" + imageStoreName;
                    file.transferTo(new File(filePath));
                    literatureService.uploadContributorsCover(literatureId, imageStoreName);
            	}
            }
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/contributorslist",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject contributorsList(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String, Object>> contributorsList = literatureService.contributorsList();
		
		retjson.put("literatures", contributorsList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/querycontributorsname",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject queryContributors(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String contributorsName = req.getString("contributorsName");
		
		List<Map<String, Object>> contributorsList = literatureService.queryContributors(contributorsName);
		
		retjson.put("literatures", contributorsList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/literatureinfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject literatureInfo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String contributorsName = req.getString("contributorsName");
		
		List<Map<String, Object>> literatureList = literatureService.literatureInfo(contributorsName);
		
		retjson.put("literature", literatureList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/updatecontributors",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject updateContributors(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String contributorsNewName = req.getString("contributorsNewName");
		String contributorsOldName = req.getString("contributorsOldName");
		
		literatureService.updateContributors(contributorsNewName, contributorsOldName);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/delcontributors",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delContributors(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String contributorsName = req.getString("contributorsName");
		
		literatureService.delContributors(contributorsName);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/delliterature",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delLiterature(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int literatureId = req.getIntValue("literatureId");
		
		literatureService.delLiterature(literatureId);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
}
