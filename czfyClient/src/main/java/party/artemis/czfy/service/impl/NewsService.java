package party.artemis.czfy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;

@Service(value="newsService")
public class NewsService implements party.artemis.czfy.service.INewsService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	
	@Override
	public List<Map<String, Object>> getNewsList() {
		
		StringBuilder newsSql = new StringBuilder();
		
		newsSql.append("select a.news_id,a.news_title,a.news_type,a.image_parent_id,b.image_url as cover from news a "
				+ " join image b on a.image_parent_id = b.image_id ");
		
		
		List<Map<String,Object>> newsList = commonDAO.find(newsSql.toString());
		
		for(int i = 0; i < newsList.size(); i++){
			if(newsList.get(i).get("cover")!=null){
				String cover = (String) newsList.get(i).get("cover");
				cover = Constants.IP_ADDRESS + Constants.PICTURE_DIR + cover;
				newsList.get(i).put("cover", cover);
			}
			
			Object[] imageParams = new Object[1];
			imageParams[0] = newsList.get(i).get("newsId");
			
			StringBuilder imageSql = new StringBuilder();
			
			imageSql.append("select b.image_url from news a join image b on a.image_parent_id = b.image_parent_id where a.news_id = ?");
			
			
			List<Map<String,Object>> imageList = commonDAO.find(imageSql.toString(),imageParams);
			
			for(int j = 0; j < imageList.size(); j++){
				if(imageList.get(j).get("imageUrl")!=null){
					String imageUrl = (String) imageList.get(j).get("imageUrl");
					imageUrl = Constants.IP_ADDRESS + Constants.PICTURE_DIR + imageUrl;
					imageList.get(j).put("imageUrl", imageUrl);
				}
			}
			
			newsList.get(i).put("images", imageList);
			newsList.get(i).put("imageNumber", imageList.size());
		}
		
		
		return newsList;
	}


	@Override
	public List<Map<String, Object>> getNewsById(int newsId) {
		Object [] newsParams = new Object[1];
		newsParams[0] = newsId;
		StringBuilder newsSql = new StringBuilder();
		newsSql.append("select b.image_url from news a join image b on a.image_parent_id = b.image_parent_id where a.news_id = ?");
		
		List<Map<String,Object>> newsList = commonDAO.find(newsSql.toString(), newsParams);
		
		for(int i = 0; i < newsList.size(); i++){
			if(newsList.get(i).get("imageUrl")!=null){
				String imageUrl = (String) newsList.get(i).get("imageUrl");
				imageUrl = Constants.IP_ADDRESS + Constants.PICTURE_DIR + imageUrl;
				newsList.get(i).put("imageUrl", imageUrl);
			}
		}
		
		return newsList;
	}


	//多条件查询新闻（标题和风格）
	@Override
	public List<Map<String, Object>> getNewsByMore(String newsTitle,String newsStyle) {


		int i = 1;

		if(newsTitle!=null&&newsStyle!=null){
			i++;
		}

		Object[] params = new Object[i];

		params[0] = "%"+ newsTitle +"%";

		StringBuffer sql = new StringBuffer();

		sql.append("select b.image_url from news a join image b on a.image_parent_id = b.image_parent_id");

		if(newsTitle!=null){
			sql.append("where a.news_title like ?");
			params[0] = newsTitle;
			if(newsStyle!=null){
				params[--i]=newsStyle;
				sql.append("and a.new_style like ?");
			}else{
				sql.append("where a.new_stylelike ?");
				params[0] = newsStyle;
			}

		}

		for (int j = 0; j < params.length; j++) {
			System.out.println(params[j]);
		}

		List<Map<String, Object>> newsList = commonDAO.find(sql.toString(), params);



		return newsList;

	}

}
