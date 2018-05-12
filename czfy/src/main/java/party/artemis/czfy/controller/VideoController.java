package party.artemis.czfy.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IVideoService;

@RequestMapping(value="/admin")
@Controller
public class VideoController {
	private static Logger log = Logger.getLogger(VideoController.class);
	
	@Resource(name="videoService")
	private IVideoService videoService;
	
	@RequestMapping(value="/addvideo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addVideo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String videoName = req.getString("videoName");
		String videoIntroducation = req.getString("videoIntroducation");
		String nickname = req.getString("nickname");
		Boolean isTop = req.getBoolean("isTop");
		
		int videoId = videoService.addVideo(videoName, videoIntroducation, nickname,isTop);
		
		retjson.put("videoId", videoId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/videolist",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject videoList(){
		JSONObject retjson = new JSONObject();
		List<Map<String, Object>>  videoList = videoService.videoList();
		
		retjson.put("videoList", videoList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/queryvideo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject queryVideo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String videoName = req.getString("videoName");
		
		List<Map<String, Object>> videoList = videoService.queryVideo(videoName);
		
		retjson.put("videoList", videoList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/videoinfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject videoInfo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int videoId = req.getIntValue("videoId");
		
		Map<String,Object> video = videoService.videoInfo(videoId);
		
		retjson.put("video", video);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/updatevideo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject updateVideo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		String videoName = req.getString("videoName");
		String videoIntroducation = req.getString("videoIntroducation");
		int videoId = req.getIntValue("videoId");
		Boolean isTop = req.getBoolean("isTop");
		
		videoService.updateVideo(videoId, videoName, videoIntroducation,isTop);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	
	@RequestMapping(value="/videoupload/{videoId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject videoUpload(MultipartHttpServletRequest request,
			@PathVariable(value="videoId") int videoId) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();
		
		String imageStoreName = "";
		String videoStoreName = "";
		
		Iterator<String> it = request.getFileNames();
		while(it.hasNext()){
			//一次遍历所有文件
            MultipartFile file =request.getFile(it.next().toString());
            if(file!=null)
            {
            	File pictureDir = new File(Constants.RESOURCE_PATH + Constants.PICTURE_DIR);
            	File videoDir = new File(Constants.RESOURCE_PATH + Constants.VIDEO_DIR);
            	if(!pictureDir.exists()) {
            		pictureDir.mkdirs();
            	}
            	if(!videoDir.exists()){
            		videoDir.mkdirs();
            	}
            	log.debug(file.getContentType());
            	if(file.getContentType().equals("video/avi")){
            		videoStoreName = System.currentTimeMillis() + file.getOriginalFilename();
            		String path = videoDir.getPath() + "/" + videoStoreName;
            		file.transferTo(new File(path));
            		videoService.uploadVideo(videoId, videoStoreName);
            	}else if(file.getContentType().startsWith("image/")){
            		imageStoreName = System.currentTimeMillis() + file.getOriginalFilename();
                	String path = pictureDir.getPath() + "/" + imageStoreName;
                    file.transferTo(new File(path));
                    videoService.uploadVideoCover(videoId, imageStoreName);
            	}
            	
            }
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
	
	@RequestMapping(value="/delvideo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delVideo(@RequestBody String body){
		JSONObject retjson = new JSONObject();
		
		log.debug("请求JSON:" + body);
		
		JSONObject req = JSONObject.parseObject(body);
		
		int videoId = req.getIntValue("videoId");
		
		videoService.delVideo(videoId);
		
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}
}
