package party.artemis.czfy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import party.artemis.czfy.service.INewsService;

@RequestMapping(value="/admin")
@Controller
public class NewsController {
	private static Logger log = Logger.getLogger(NewsController.class);

	@Resource(name="newsService")
	private INewsService newsService;

	@RequestMapping(value="/addnews",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addNews(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		String newsTitle = req.getString("newsTitle");
		String newsType = req.getString("newsType");
		Boolean isTop = req.getBoolean("isTop");
		int isPass = req.getIntValue("isPass");

		int newsId = newsService.addNews(newsTitle, newsType, isTop, isPass);

		retjson.put("newsId", newsId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/newslist",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject newsList(){
		JSONObject retjson = new JSONObject();
		List<Map<String, Object>>  newsList = newsService.newsList();

		retjson.put("newsList", newsList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/querynews",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject queryNews(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		String newsTitle = req.getString("newsTitle");
		String newsType = req.getString("newsType");
		int isPass = req.getIntValue("isPass");

		List<Map<String, Object>> newsList = newsService.queryNews(newsTitle,newsType,isPass);

		retjson.put("newsList", newsList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/newsinfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject newsInfo(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int newsId = req.getIntValue("newsId");

		Map<String,Object> news = newsService.newsInfo(newsId);

		Map<String,Object> pass = newsService.getPassState(newsId);

		retjson.put("pass", pass);
		retjson.put("news", news);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	/**
	 * 修改新闻信息
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/updatenews",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject updateNews(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		String newsTitle = req.getString("newsTitle");
		String newsType = req.getString("newsType");
		int newsId = req.getIntValue("newsId");
		Boolean isTop = req.getBoolean("isTop");
		int isPass = req.getIntValue("isPass");
		System.out.println("111111111111111111:      " + isPass);
		newsService.updateNews(newsId, newsTitle, newsType, isTop, isPass);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/newsimageupload/{newsId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject videoUpload(MultipartHttpServletRequest request,
								  @PathVariable(value="newsId") int newsId) throws IllegalStateException, IOException{
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
				file.transferTo(new File(path));
				newsService.uploadNewsImage(newsId, imageStoreName);
			}
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/delnews",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delNews(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int newsId = req.getIntValue("newsId");

		newsService.delNews(newsId);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	/**
	 * 审核新闻
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/isPass", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject isPassNews(@RequestBody String  body){

		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);


		int newsId = req.getIntValue("newsId");

		int isPass = req.getIntValue("isPass");
		System.out.println("111111111111111111:      " + isPass + newsId);
		newsService.isPassNews(isPass, newsId);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	/**
	 * 获取审核值对应的信息
	 * @return
	 */
	@RequestMapping(value="/getPassInfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getPassInfo(){
		JSONObject retjson = new JSONObject();

		List<Map<String, Object>> passList = newsService.getPassinfo();
		retjson.put("passList", passList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@ResponseBody
	@RequestMapping(value="/getnewsbyid")
	public JSONObject getNewsById(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int newsId = req.getIntValue("newsId");

		List<Map<String, Object>>  news = newsService.getNewsById(newsId);

		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("news",news);

		return retjson;
	}

	@RequestMapping(value="/newsA/actorphotoupload/{newsId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorPhotoUpload(MultipartHttpServletRequest request,
									   @PathVariable(value="newsId") int newsId) throws IllegalStateException, IOException{
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
				newsService.updateActorPhoto(newsId, imageStoreName);
			}
		}
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@RequestMapping(value="/newsA/actorworkupload/{newsId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject actorWorkUpload(MultipartHttpServletRequest request,
									  @PathVariable(value="newsId") int newsId) throws IllegalStateException, IOException{
		JSONObject retjson = new JSONObject();

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
		newsService.updateActorWork(newsId, imageUrlList);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		return retjson;
	}

	@ResponseBody
	@RequestMapping(value = "/getNewsInfoById")
	public JSONObject getNewsInfoById(@RequestBody String body){
		JSONObject retjson = new JSONObject();

		log.debug("请求JSON:" + body);

		JSONObject req = JSONObject.parseObject(body);

		int newsId = req.getIntValue("newsId");

		JSONObject news = newsService.getNewsInfoById(newsId);
		retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
		retjson.put("newsInfo", news);

		return retjson;
	}

}
