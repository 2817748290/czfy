package party.artemis.czfy.controller;


import java.io.File;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IActorService;



@RequestMapping(value="/admin")
@Controller
public class ActorController {
	private static Logger log = Logger.getLogger(ActorController.class);
	
	@Resource(name="actorService")
	private IActorService actorService;
	
	
	@RequestMapping(value="/getactortype",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getActorType(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String, Object>> typeList = actorService.getActorType();
		
		retjson.put("typeList", typeList);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	//获取等级名称
	@RequestMapping(value="/getActorLevelName",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getActorLevel(){
		JSONObject retjson = new JSONObject();
		List<Map<String, Object>> levelList = actorService.getActorLevelName();
		retjson.put("levelList", levelList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/addactor",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addActor (HttpServletRequest request,@RequestBody String body){

		JSONObject retjson = new JSONObject();
		log.debug("请求JSON:" + body);
		JSONObject req = JSONObject.parseObject(body);
		String actorName = req.getString("actorName");
		String actorIntroducation = req.getString("actorIntroducation");
		int actorClassification = req.getIntValue("classificationId");
		Boolean isTop = req.getBoolean("isTop");
		int actorLevel = req.getIntValue("actorLevel");
		Long actorPhoto = new Date().getTime();
		int actorId = actorService.addActor(actorName, actorIntroducation,actorClassification,actorLevel,isTop,actorPhoto);
		System.out.println("！！！！！：" + actorId + actorPhoto);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		request.getSession().setAttribute("actorId", actorId);
		retjson.put("actorId", actorId);
		retjson.put("actorPhoto", actorPhoto);
		return retjson;
	}
	
	@RequestMapping(value="/actorlist",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorList(){
		JSONObject retjson = new JSONObject();
		
		List<Map<String,Object>> actorList =  actorService.actorList();
		
		retjson.put("actorList", actorList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		
		return retjson;
	}
	
	@RequestMapping(value="/actorinfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorInfo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int actorId = req.getIntValue("actorId");
		
		Map<String,Object> actor = actorService.actorInfo(actorId);
		
		retjson.put("actor", actor);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/delactor",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delActor(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);

		int actorId = req.getIntValue("actorId");
		
		actorService.delActor(actorId);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/updateactor",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject updateActor(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String actorName = req.getString("actorName");
		String actorIntroducation = req.getString("actorIntroducation");
		int actorClassification = req.getIntValue("classificationId");
		Boolean isTop = req.getBoolean("isTop");
		int actorId = req.getIntValue("actorId");
		int actorLevel = req.getIntValue("actorLevel");
		
		actorService.updateActor(actorId, actorName, actorIntroducation,actorClassification,isTop,actorLevel);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/actorphotoupload",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorPhotoUploadOne(MultipartHttpServletRequest request) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();

		String imageStoreName = "";

		Iterator<String> it = request.getFileNames();
		while(it.hasNext()){
			//一次遍历所有文件
			MultipartFile file =request.getFile(it.next().toString());
			if(file!=null)
			{
				File pictureDir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
				if(!pictureDir.exists()) {
					pictureDir.mkdirs();
				}
				imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
				String path = pictureDir.getPath() + "/" + imageStoreName;
				//上传
				file.transferTo(new File(path));
				System.out.println("zlzlzlzlzlzl:   " + path);
				actorService.uploadActorPhoto(imageStoreName);
			}
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	/**
	 * 根据actorId上传封面(单张)
	 * @param request
	 * @param actorId
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value="/actorphotoupload/{actorId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorPhotoUpload(MultipartHttpServletRequest request,
			@PathVariable(value="actorId") int actorId) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();

		String imageStoreName = "";

		Iterator<String> it = request.getFileNames();
		System.out.println("yjy:"+actorId);
		while(it.hasNext()){
			//一次遍历所有文件
            MultipartFile file =request.getFile(it.next().toString());
            if(file!=null)
            {
            	File pictureDir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
            	if(!pictureDir.exists()) {
            		pictureDir.mkdirs();
            	}
            	imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
            	String path = pictureDir.getPath() + "/" + imageStoreName;
                //上传
                file.transferTo(new File(path));
                actorService.updateActorPhoto(actorId, imageStoreName);
            }
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	/**
	 * 根据actorId插入艺人作品图片(多张)
	 * @param request
	 * @param actorId
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value="/actorworkupload/{actorId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorWorkUpload(MultipartHttpServletRequest request,
			@PathVariable(value="actorId") int actorId) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();
		System.out.println("zlzlzl:"+actorId);
		List<String> imageUrlList = new ArrayList<String>();
		
		Iterator<String> it = request.getFileNames();
		while(it.hasNext()){
			//一次遍历所有文件
            MultipartFile file =request.getFile(it.next().toString());
            if(file!=null)
            {
            	File pictureDir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
            	if(!pictureDir.exists()) {
            		pictureDir.mkdirs();
            	}
            	String imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
            	String path = pictureDir.getPath() + "/" + imageStoreName;
                //上传
                file.transferTo(new File(path));
                imageUrlList.add(imageStoreName);
            }
		}
		actorService.updateActorWork(actorId, imageUrlList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/queryactor",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject queryActor(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String actorName = req.getString("actorName");
		int actorClassificationId = req.getIntValue("actorClassification");
		int actorLevel = req.getIntValue("actorLevel");
		
		List<Map<String, Object>> actorList = actorService.queryActor(actorName,
				actorClassificationId,actorLevel);
		
		retjson.put("actorList", actorList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@ResponseBody
	@RequestMapping(value = "/getActorById",method = RequestMethod.POST)
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
	@ResponseBody
	@RequestMapping(value = "/updateIsPass")
	public JSONObject updateIsPass(HttpServletRequest request,@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		int actorId = (int)request.getSession().getAttribute("actorId");
		System.out.println("!!!!!! :  " + actorId  );
		actorService.updateIsPass(actorId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

		return retjson;
	}
}
