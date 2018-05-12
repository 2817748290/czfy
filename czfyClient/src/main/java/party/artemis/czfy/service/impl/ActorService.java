package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.dao.impl.CommonDAO;
import party.artemis.czfy.service.IActorService;

@Service(value="actorService")
public class ActorService implements IActorService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;

	@Override
	public List<Map<String, Object>> getActorList() {
		StringBuilder actorSql = new StringBuilder();
		actorSql.append("select a.actor_id,a.actor_introducation,a.actor_name,b.image_url as photo from actor a " + " join image b on a.actor_photo = b.image_id ");


		List<Map<String, Object>> actorList = commonDAO.find(actorSql.toString());

		for(int i = 0; i < actorList.size(); i++){
			if(actorList.get(i).get("photo")!=null){
				String photo = (String) actorList.get(i).get("photo");
				photo = Constants.IP_ADDRESS + Constants.PICTURE_DIR + photo;
				actorList.get(i).put("photo", photo);
			}
		}

		return actorList;
	}

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

		int actorPhoto = (int) actorList.get(0).get("actorPhoto");

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

	@Override
	public List<Map<String, Object>> queryActor(int classificationId, int actorLevel, int page) {

		Logger log = Logger.getLogger(CommonDAO.class);
		int i = 0;
		int v = 0;
		if (actorLevel != 0||classificationId!=0) {
			i=0;
			i++;
			v=i;
		}
		if (actorLevel != 0&&classificationId!=0) {
			i=1;
			i++;
			v=i;
		}
		if(page!=-1){
			i++;
		}
		Object[] params = new Object[i];

		StringBuilder sql = new StringBuilder();

		sql.append("select a.actor_id,a.actor_introducation,a.actor_name,b.image_url as photo from actor a "
				+ " join image b on a.actor_photo = b.image_id ");
		if (classificationId != 0) {
			params[0] = classificationId;
			sql.append("where a.classification_id = ?");

			if (actorLevel != 0) {
				params[--v] = actorLevel;
				sql.append(" and a.actor_level=?");
			}
			params[--i] = page;
			sql.append(" limit ?,4");

		} else {
			if(actorLevel != 0) {
				params[0] = actorLevel;
				sql.append("where a.actor_level = ?");
				params[1] = page;
				sql.append(" limit ?,4");
			}else{

				params[0] = page;
				sql.append(" limit ?,4");
			}
		}

		for (int j = 0; j < params.length; j++) {
			System.out.println(params[j]);
		}

		List<Map<String, Object>> actorList = commonDAO.find(sql.toString(), params);
		System.out.println(sql.toString());
		log.debug(sql.toString());

		for (int x = 0; x < actorList.size(); x++){
			if (actorList.get(x).get("photo") != null) {
				String photo = (String) actorList.get(x).get("photo");
				photo = Constants.IP_ADDRESS + Constants.PICTURE_DIR + photo;
				actorList.get(x).put("photo", photo);
			}
		}

		return actorList;
	}

	@Override
	public Integer getSumPage(int classificationId, int actorLevel) {

		Logger log = Logger.getLogger(CommonDAO.class);
		int i = 0;
		if (actorLevel != 0||classificationId!=0) {
			i=0;
			i++;
		}
		if (actorLevel != 0&&classificationId!=0) {
			i=1;
			i++;
		}
		Object[] params = new Object[i];

		StringBuilder sql = new StringBuilder();

		sql.append("select a.actor_id,a.actor_introducation,a.actor_name,b.image_url as photo from actor a "
				+ " join image b on a.actor_photo = b.image_id ");
		if (classificationId != 0) {
			params[0] = classificationId;
			sql.append("where a.classification_id = ?");

			if (actorLevel != 0) {
				params[--i] = actorLevel;
				sql.append(" and a.actor_level=?");
			}


		} else {
			if(actorLevel != 0) {
				params[0] = actorLevel;
				sql.append("where a.actor_level = ?");

			}else{
				sql.append(" ");
			}
		}

		for (int j = 0; j < params.length; j++) {
			System.out.println(params[j]);
		}

		List<Map<String, Object>> actorList = commonDAO.find(sql.toString(), params);
		System.out.println(sql.toString());
		log.debug(sql.toString());

		Integer SumPag = actorList.size();

		System.out.println("Yeshu::::" + SumPag);

		return SumPag;
	}



}
