package party.artemis.czfy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;


import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.IActorService;

@Service(value="actorService")
public class ActorService implements IActorService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;

	@Override
	public JSONObject getActorById(int actorId) {

		JSONObject retjson = new JSONObject();

		Object [] params = new Object[1];
		params[0] = actorId;
		StringBuilder sql = new StringBuilder();

		sql.append("select a.actor_id,a.actor_introducation,a.actor_name,a.actor_photo,b.image_url as photo from actor a "
				+ " join image b on a.actor_photo = b.image_id where a.actor_id = ?");

		List<Map<String, Object>> actorList = commonDAO.find(sql.toString(),params);

		if(actorList.get(0).get("photo")!=null){
			String photo  = (String) actorList.get(0).get("photo");
			photo = Constants.IP_ADDRESS + Constants.PICTURE_DIR + photo;
			actorList.get(0).put("photo", photo);
		}

		Long actorPhoto = (Long) actorList.get(0).get("actorPhoto");

		Object[] imageParams = new Object[1];
		imageParams[0] = actorPhoto;
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

		retjson.put("actor", actorList.get(0));
		retjson.put("images", imageList);

		return retjson;

	}

	/**
	 *添加艺人
	 */
	@Override
	public int addActor(String actorName, String actorIntroducation, int actorClassificationId, int actorLevel, Boolean isTop, long actorPhoto) {
		Object[] params = new Object[6];
		params[0] = actorName;
		params[1] = actorIntroducation;
		params[2] = actorClassificationId;
		params[3] = isTop==true?1:0;
		params[4] = actorLevel;
		params[5] = actorPhoto;

 		StringBuilder sql = new StringBuilder();
		sql.append("insert into actor(actor_name,actor_introducation,classification_id,is_top,actor_level,actor_photo) values(?,?,?,?,?,?)");
		
		int actorId = commonDAO.insert(sql.toString(), params);
		return actorId;
	}

	/**
	 *查询所有的艺人
	 */
	@Override
	public List<Map<String, Object>> actorList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.actor_id,a.actor_name from actor a where isPass = 1");
		List<Map<String, Object>> actorList = commonDAO.find(sql.toString());
		return actorList;
	}

	/**
	 *根据id查询艺人的信息
	 * @param actorId 艺人id
	 */
	@Override
	public Map<String, Object> actorInfo(int actorId) {
		Object[] params = new Object[1];
		params[0] = actorId;
		StringBuilder sql = new StringBuilder();
		sql.append("select a.actor_id,a.actor_name,a.actor_introducation,a.classification_id,a.is_top ,a.actor_level from actor a where a.actor_id=?");
		
		List<Map<String, Object>> actorList = commonDAO.find(sql.toString(),params);
		
		if(!actorList.isEmpty()){
			return actorList.get(0);
		}
		return null;
	}

	/**
	 *删除艺人信息
	 * @param actorId 艺人id
	 */
	@Override
	public void delActor(int actorId) {
		List<String> sqlList = new ArrayList<String>();
		List<Object[]> paramList = new ArrayList<Object[]>();
		
		Object[] queryParams = new Object[1];
		queryParams[0] = actorId;
		StringBuilder querySql = new StringBuilder();
		querySql.append("select a.actor_photo from actor a where a.actor_id=?");
		
		List<Map<String, Object>> photoList = commonDAO.find(querySql.toString(), queryParams);
		
		long photoId = (long)photoList.get(0).get("actorPhoto");
		
		Object[] actorParams = new Object[1];
		actorParams[0] = actorId;
		StringBuilder actorSql = new StringBuilder();
		actorSql.append("delete from actor where actor_id=?");
		
		if(photoId!=0){
			Object[] imageParams = new Object[1];
			imageParams[0] = photoId;
			StringBuilder imageSql = new StringBuilder();
			imageSql.append("delete from image where image_parent_id=?");
			sqlList.add(imageSql.toString());
			paramList.add(imageParams);
			
		}
		sqlList.add(actorSql.toString());
		paramList.add(actorParams);
		
		commonDAO.batchUpdate(sqlList, paramList);
	}

	/**
	 * 修改艺人信息
	 * @param actorId 艺人id
	 * @param actorName 艺人姓名
	 * @param actorIntroducation 艺人简介
	 * @param actorClassificationId 艺人类型
	 * @param isTop 是否置顶
	 * @param actorLevel 艺人等级
	 */
	@Override
	public void updateActor(int actorId, String actorName, String actorIntroducation, int actorClassificationId, Boolean isTop, int actorLevel) {
		Object[] params = new Object[6];
		params[0] = actorName;
		params[1] = actorIntroducation;
		params[2] = actorClassificationId;
		params[3] = isTop==true?1:0;
		params[4] = actorLevel;
		params[5] = actorId;
 		StringBuilder sql = new StringBuilder();
		sql.append("update actor set actor_name=?, actor_introducation=?, classification_id=?, is_top=?, actor_level=? where actor_id=?");
		
		commonDAO.update(sql.toString(), params);
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
		actorUpdateSql.append("update actor set actor_photo=(select image_id from image where image_url=?)");

		sqlList.add(imageSql.toString());
		sqlList.add(imageUpdateSql.toString());
		sqlList.add(actorUpdateSql.toString());

		paramList.add(imageParams);
		paramList.add(imageUpdateParams);
		paramList.add(actorUpdateParams);

		commonDAO.batchUpdate(sqlList, paramList);
	}

	@Override
	public void updateActorPhoto(int actorId, String imageUrl) {
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
		actorUpdateParams[1] = actorId;
		StringBuilder actorUpdateSql = new StringBuilder();
		actorUpdateSql.append("update actor set actor_photo=(select image_id from image where image_url=?)")
		.append(" where actor_id=?");
		
		sqlList.add(imageSql.toString());
		sqlList.add(imageUpdateSql.toString());
		sqlList.add(actorUpdateSql.toString());
		
		paramList.add(imageParams);
		paramList.add(imageUpdateParams);
		paramList.add(actorUpdateParams);
		
		commonDAO.batchUpdate(sqlList, paramList);
	}

	@Override
	public void updateActorWork(int actorId, List<String> imageUrlList) {
		Object[] queryParams = new Object[1];
		queryParams[0] = actorId;
		StringBuilder querySql = new StringBuilder();
		querySql.append("select a.actor_photo as image_parent_id from actor a where a.actor_id=?");
		
		List<Map<String,Object>> ids = commonDAO.find(querySql.toString(), queryParams);
		
		if(ids.size()>0){
			Long imageParentId = (Long) ids.get(0).get("imageParentId");
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

	/**
	 * 多条件查询艺人
	 * @param actorName
	 * @param actorClassificationId
	 * @param actorLevel
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryActor(String actorName,
											   int actorClassificationId,
											   int actorLevel) {
		int i = 1;
		if(actorClassificationId!=0){
			i++;
		}
		if(actorLevel!=0){
			i++;
		}
		Object[] params = new Object[i];
		params[0] = "%" + actorName + "%";
		StringBuilder sql = new StringBuilder();
		sql.append("select a.actor_id,a.actor_name from actor a where a.actor_name like ?");
		if(actorClassificationId!=0){
			params[1] = actorClassificationId;
			sql.append(" and classification_id=?");
			if(actorLevel!=0){
				params[i-1] = actorLevel;
				sql.append(" and actor_Level=?");
			}
		}else{
			if(actorLevel!=0){
				params[1] = actorLevel;
				sql.append(" and actor_Level=?");
			}
		}
		
		List<Map<String,Object>> actorList = commonDAO.find(sql.toString(),params);
		
		return actorList;
	}

	@Override
	public List<Map<String, Object>> getActorType() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from classification a");
		
		List<Map<String, Object>> typeList = commonDAO.find(sql.toString());
		
		return typeList;
	}

	@Override
	public List<Map<String, Object>> getActorLevelName() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from level a");

		List<Map<String, Object>> levelList = commonDAO.find(sql.toString());

		System.out.println(levelList);

		return levelList;
	}
	@Override
	public void updateIsPass(int actorId) {
		Object[] params = new Object[1];
		params[0] = actorId;
		StringBuffer sql = new StringBuffer();
		sql.append("update actor set isPass=1 where actor_id= ? ");

		commonDAO.update(sql.toString(),params);
}

}
