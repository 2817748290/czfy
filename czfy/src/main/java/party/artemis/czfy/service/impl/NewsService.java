package party.artemis.czfy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.INewsService;

@Service(value="newsService")
public class NewsService implements INewsService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;

	@Override
	public int addNews(String newsTitle, String newType, Boolean isTop, int isPass) {
		Object[] params = new Object[4];
		params[0] = newsTitle;
		params[1] = newType;
		params[2] = isTop==true?1:0;
		params[3] = isPass;
		StringBuilder sql = new StringBuilder();
		sql.append("insert into news(news_title,news_type,is_top,isPass) values(?,?,?,?)");

		int newsId = commonDAO.insert(sql.toString(), params);
		return newsId;
	}

	@Override
	public List<Map<String, Object>> newsList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.news_id,a.news_title from news a");
		List<Map<String, Object>> newsList = commonDAO.find(sql.toString());
		return newsList;
	}

	@Override
	public List<Map<String, Object>> queryNews(String newsTitle, String newType, int isPass) {
		int i = 1;
		if(newType!=null){
			i++;
		}
		if(isPass!=-1){
			i++;
		}
		Object[] params = new Object[i];
		params[0] = "%" + newsTitle + "%";
		StringBuilder sql = new StringBuilder();
		sql.append("select a.news_id,a.news_title from news a where a.news_title like ?");
		if(newType!=null){
			params[1] = newType;
			sql.append(" and a.news_type=?");
			if(isPass!=-1){
				params[--i] = isPass;
				sql.append(" and a.isPass = ?");
			}
		}else {
			if(isPass!=-1){
				params[--i] = isPass;
				sql.append(" and a.isPass = ?");
			}
		}

		List<Map<String,Object>> newsList = commonDAO.find(sql.toString(),params);

		return newsList;
	}

	@Override
	public Map<String, Object> newsInfo(int newsId) {
		Object[] params = new Object[1];
		params[0] = newsId;
		StringBuilder sql = new StringBuilder();
		sql.append("select a.news_id,a.news_title,a.news_type,a.is_top,a.isPass from news a where a.news_id=?");

		List<Map<String, Object>> newsList = commonDAO.find(sql.toString(),params);

		if(!newsList.isEmpty()){
			return newsList.get(0);
		}
		return null;
	}

	@Override
	public void updateNews(int newsId, String newsTitle, String newType, Boolean isTop, int isPass) {
		Object[] params = new Object[5];
		params[0] = newsTitle;
		params[1] = newType;
		params[2] = isTop==true?1:0;
		params[3] = isPass;
		params[4] = newsId;
		StringBuilder sql = new StringBuilder();
		sql.append("update news set news_title=?, news_type=?, is_top=? , isPass=? where news_id=?");

		commonDAO.update(sql.toString(), params);
	}

	@Override
	public void uploadNewsImage(int newsId, String imageUrl) {
		List<String> sqlList = new ArrayList<String>();
		List<Object[]> paramList = new ArrayList<Object[]>();

		Object[] queryParams = new Object[1];
		queryParams[0] = newsId;
		StringBuilder querySql = new StringBuilder();
		querySql.append("select a.image_parent_id from news a where a.news_id=?");

		List<Map<String,Object>> idList = commonDAO.find(querySql.toString(), queryParams);

		if((int)idList.get(0).get("imageParentId")==0){
			Object[] imageParams = new Object[2];
			imageParams[0] = Constants.IMAGE_TYPE_NEWS;
			imageParams[1] = imageUrl;
			StringBuilder imageSql = new StringBuilder();
			imageSql.append("insert into image(image_type,image_url) values(?,?)");


			Object[] imageUpdateParams = null;
			StringBuilder imageUpdateSql = new StringBuilder();
			imageUpdateSql.append("update image set image_parent_id=(select a.id from (select max(image_id) id from image) a) where image_id=(select a.id from (select max(image_id) id from image) a)");

			Object[] newsUpdateParams = new Object[1];
			newsUpdateParams[0] = newsId;
			StringBuilder newsUpdateSql = new StringBuilder();
			newsUpdateSql.append("update news set image_parent_id=(select a.id from (select max(image_id) id from image) a) where news_id=?");

			sqlList.add(imageSql.toString());
			sqlList.add(imageUpdateSql.toString());
			sqlList.add(newsUpdateSql.toString());

			paramList.add(imageParams);
			paramList.add(imageUpdateParams);
			paramList.add(newsUpdateParams);

			commonDAO.batchUpdate(sqlList, paramList);
			return;
		}

		int imageParentId = (int)idList.get(0).get("imageParentId");

		Object[] imageParams = new Object[3];
		imageParams[0] = imageParentId;
		imageParams[1] = Constants.IMAGE_TYPE_NEWS;
		imageParams[2] = imageUrl;

		StringBuilder imageSql = new StringBuilder();
		imageSql.append("insert into image(image_parent_id,image_type,image_url) values(?,?,?)");

		commonDAO.insert(imageSql.toString(), imageParams);
	}

	@Override
	public void delNews(int newsId) {
		List<String> sqlList = new ArrayList<String>();
		List<Object[]> paramList = new ArrayList<Object[]>();

		Object[] queryParams = new Object[1];
		queryParams[0] = newsId;
		StringBuilder querySql = new StringBuilder();
		querySql.append("select a.image_parent_id from news a where a.news_id=?");

		List<Map<String,Object>> ipidList = commonDAO.find(querySql.toString(), queryParams);

		int imageParentId = (int) ipidList.get(0).get("imageParentId");

		Object[] newsParams = new Object[1];
		newsParams[0] = newsId;
		StringBuilder newsSql = new StringBuilder();
		newsSql.append("delete from news where news_id=?");

		sqlList.add(newsSql.toString());
		paramList.add(newsParams);

		if(imageParentId!=0){
			Object[] imageParams = new Object[1];
			imageParams[0] = imageParentId;
			StringBuilder imageSql = new StringBuilder();
			imageSql.append("delete from image where image_parent_id=?");

			sqlList.add(imageSql.toString());
			paramList.add(imageParams);
		}

		commonDAO.batchUpdate(sqlList, paramList);
	}

	@Override
	public void isPassNews(int isPass, int newsId) {

		StringBuffer newssql = new StringBuffer();
		Object[]  params = new Object[2];
		params[0] = isPass;
		params[1] = newsId;
		newssql.append("update news set isPass=? where news_id=?");
		System.out.println("2222222222     :    " + newssql );

		commonDAO.update(newssql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> getPassinfo() {

		StringBuffer sql = new StringBuffer();

		sql.append("select a.* from pass a");

		List<Map<String, Object>> passList = commonDAO.find(sql.toString());

		System.out.println(passList);

		return passList;
	}

	@Override
	public Map<String,Object> getPassState(int newsId) {

		StringBuffer sql = new StringBuffer();

		Object[] params = new Object[1];

		params[0] = newsId;

		sql.append("select a.isPass from news a where a.news_id = ?");

		List<Map<String, Object>> getPasslist = commonDAO.find(sql.toString(),params);

		return getPasslist.get(0);
	}

	@Override
	public List<Map<String, Object>> getNewsById(int newsId) {
		Object [] newsParams = new Object[1];
		newsParams[0] = newsId;
		StringBuilder newsSql = new StringBuilder();
		newsSql.append("select a.*, b.image_url from news a join image b on a.image_parent_id = b.image_parent_id where a.news_id = ?");

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


	@Override
	public void uploadActorPhoto(String imageUrl) {
		List<String> sqlList = new ArrayList<String>();
		List<Object[]> paramList = new ArrayList<Object[]>();

		Object[] imageParams = new Object[2];
		imageParams[0] = Constants.IMAGE_TYPE_PHOTO;
		imageParams[1] = imageUrl;
		StringBuilder imageSql = new StringBuilder();
		imageSql.append("insert into image(image_type,image_url) values(?,?)");

		Object[] imageUpdateParams = null;
		StringBuilder imageUpdateSql = new StringBuilder();
		imageUpdateSql.append("update image set image_parent_id=(SELECT LAST_INSERT_ID()) ")
				.append("where image_id=(SELECT LAST_INSERT_ID())");

		Object[] actorUpdateParams = new Object[1];
		actorUpdateParams[0] = imageUrl;
		StringBuilder actorUpdateSql = new StringBuilder();
		actorUpdateSql.append("update news set image_parent_id=(select image_id from image where image_url=?)");

		sqlList.add(imageSql.toString());
		sqlList.add(imageUpdateSql.toString());
		sqlList.add(actorUpdateSql.toString());

		paramList.add(imageParams);
		paramList.add(imageUpdateParams);
		paramList.add(actorUpdateParams);

		commonDAO.batchUpdate(sqlList, paramList);
	}

	@Override
	public void updateActorPhoto(int newsId, String imageUrl) {
		List<String> sqlList = new ArrayList<String>();
		List<Object[]> paramList = new ArrayList<Object[]>();

		Object[] imageParams = new Object[2];
		imageParams[0] = Constants.IMAGE_TYPE_PHOTO;
		imageParams[1] = imageUrl;
		StringBuilder imageSql = new StringBuilder();
		imageSql.append("insert into image(image_type,image_url) values(?,?)");

		Object[] imageUpdateParams = null;
		StringBuilder imageUpdateSql = new StringBuilder();
		imageUpdateSql.append("update image set image_parent_id=(SELECT LAST_INSERT_ID()) ")
				.append("where image_id=(SELECT LAST_INSERT_ID())");

		Object[] actorUpdateParams = new Object[2];
		actorUpdateParams[0] = imageUrl;
		actorUpdateParams[1] = newsId;
		StringBuilder actorUpdateSql = new StringBuilder();
		actorUpdateSql.append("update news set image_parent_id=(select image_id from image where image_url=?)")
				.append(" where news_id=?");

		sqlList.add(imageSql.toString());
		sqlList.add(imageUpdateSql.toString());
		sqlList.add(actorUpdateSql.toString());

		paramList.add(imageParams);
		paramList.add(imageUpdateParams);
		paramList.add(actorUpdateParams);

		commonDAO.batchUpdate(sqlList, paramList);
	}

	@Override
	public void updateActorWork(int newsId, List<String> imageUrlList) {
		Object[] queryParams = new Object[1];
		queryParams[0] = newsId;
		StringBuilder querySql = new StringBuilder();
		querySql.append("select a.image_parent_id as image_parent_id from news a where a.news_id=?");

		List<Map<String,Object>> ids = commonDAO.find(querySql.toString(), queryParams);

		if(ids.size()>0){
			int imageParentId = (int) ids.get(0).get("imageParentId");
			for(int i = 0; i < imageUrlList.size(); i++){
				Object[] imageParams = new Object[3];
				imageParams[0] = imageParentId;
				imageParams[1] = Constants.IMAGE_TYPE_WORK;
				imageParams[2] = imageUrlList.get(i);
				StringBuilder imageSql = new StringBuilder();
				imageSql.append("insert into image(image_parent_id,image_type,image_url) values(?,?,?)");
				commonDAO.insert(imageSql.toString(),imageParams);
			}
		}
	}
	@Override
	public JSONObject getNewsInfoById(int newsId) {
		JSONObject retjson = new JSONObject();

		Object [] params = new Object[1];
		params[0] = newsId;
		StringBuilder sql = new StringBuilder();

		sql.append("select a.news_id,a.news_title,a.image_parent_id,b.image_url as photo from news a "
				+ " join image b on a.image_parent_id = b.image_id where a.news_id = ?");

		List<Map<String, Object>> actorList = commonDAO.find(sql.toString(),params);

		if(actorList.get(0).get("photo")!=null){
			String photo  = (String) actorList.get(0).get("photo");
			photo = Constants.IP_ADDRESS + Constants.PICTURE_DIR + photo;
			actorList.get(0).put("photo", photo);
		}

		int imageParentId = (int) actorList.get(0).get("imageParentId");

		Object[] imageParams = new Object[1];
		imageParams[0] = imageParentId;
		StringBuilder imageSql = new StringBuilder();
		imageSql.append("select a.image_url from image a where a.image_parent_id = ?");

		List<Map<String, Object>> imageList = commonDAO.find(imageSql.toString(),imageParams);

		for(int i = 0; imageList.size() > i; i++){
			if(imageList.get(i).get("imageUrl")!=null){
				String imageUrl = (String) imageList.get(i).get("imageUrl");
				imageUrl = Constants.IP_ADDRESS + Constants.PICTURE_DIR + imageUrl;
				imageList.get(i).put("imageUrl", imageUrl);
			}
		}

		retjson.put("news", actorList.get(0));
		retjson.put("images", imageList);

		return retjson;
	}
}
