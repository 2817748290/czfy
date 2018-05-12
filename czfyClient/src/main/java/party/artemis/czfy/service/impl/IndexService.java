package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.IIndexService;

@Service(value="indexService")
public class IndexService implements IIndexService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override
	public JSONObject getIndexInfo() {
		JSONObject retjson = new JSONObject();
		
		StringBuilder actorSql = new StringBuilder();
		StringBuilder projectSql = new StringBuilder();
		StringBuilder literatureSql = new StringBuilder();
		StringBuilder newsSql = new StringBuilder();
		StringBuilder videoSql = new StringBuilder();
		
		actorSql.append("select a.actor_id,a.actor_name,b.image_url as photo from actor a "
				+ " join image b on a.actor_photo = b.image_id where a.is_top = 1");
		projectSql.append("select a.project_id,a.project_name,a.project_cover_url from project a ")
		.append("where a.is_top = 1");
		literatureSql.append("select a.literature_id,a.literature_name,a.literature_url from literature a limit 6");
		newsSql.append("select a.news_id,a.news_title,b.image_url as news_image from news a ")
		.append(" join image b on a.image_parent_id = b.image_id limit 4");
		videoSql.append("select a.video_id,a.video_name,a.video_cover_url from video a ")
		.append(" where a.is_top = 1");
		
		List<Map<String, Object>> actorList = commonDAO.find(actorSql.toString());
		List<Map<String, Object>> projectList = commonDAO.find(projectSql.toString());
		List<Map<String, Object>> literatureList = commonDAO.find(literatureSql.toString());
		List<Map<String, Object>> newsList = commonDAO.find(newsSql.toString());
		List<Map<String, Object>> videoList = commonDAO.find(videoSql.toString());
		
		for(int i = 0; i < actorList.size(); i++){
			String photo = (String) actorList.get(i).get("photo");
			photo =  Constants.IP_ADDRESS + Constants.PICTURE_DIR + photo;
			actorList.get(i).put("photo", photo);
		}
		
		for(int i = 0; i < projectList.size(); i++){
			String cover = (String) projectList.get(i).get("projectCoverUrl");
			cover =  Constants.IP_ADDRESS + Constants.PICTURE_DIR + cover;
			projectList.get(i).put("projectCoverUrl", cover);
		}
		
		for(int i = 0; i < literatureList.size(); i++){
			String literatureUrl = (String) literatureList.get(i).get("literatureUrl");
			literatureUrl =  Constants.IP_ADDRESS + Constants.DOCUMENT_DIR + literatureUrl;
			literatureList.get(i).put("literatureUrl", literatureUrl);
		}
		
		for(int i = 0; i < newsList.size(); i++){
			String newsImage = (String) newsList.get(i).get("newsImage");
			newsImage =  Constants.IP_ADDRESS + Constants.PICTURE_DIR + newsImage;
			newsList.get(i).put("newsImage", newsImage);
			
			Object[] imageParams = new Object[1];
			imageParams[0] = newsList.get(i).get("newsId");
			
			StringBuilder countSql = new StringBuilder();
			
			countSql.append("SELECT count(*) as image_number FROM image a where a.image_parent_id = (select c.image_parent_id from news c where c.news_id = ?)");
			
			List<Map<String,Object>> count = commonDAO.find(countSql.toString(), imageParams);
			
			newsList.get(i).put("imageNumber", count.get(0).get("imageNumber"));
			
		}
		
		for(int i = 0; i < videoList.size(); i++){
			String videoCoverUrl = (String) videoList.get(i).get("videoCoverUrl");
			videoCoverUrl =  Constants.IP_ADDRESS + Constants.PICTURE_DIR + videoCoverUrl;
			videoList.get(i).put("videoCoverUrl", videoCoverUrl);
		}

		
		
		
		retjson.put("actors", actorList);
		retjson.put("projects", projectList);
		retjson.put("literatures", literatureList);
		retjson.put("news", newsList);
		retjson.put("videos", videoList);
		return retjson;
	}

}
