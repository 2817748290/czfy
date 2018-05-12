package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;

@Service(value="literatureService")
public class LiteratureService implements party.artemis.czfy.service.ILiteratureService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override
	public JSONObject getLiterature() {
		JSONObject retjson = new JSONObject();
		
		StringBuilder contrbutorSql = new StringBuilder();
		
		contrbutorSql.append("select distinct a.contributors_name from literature a ");
		
		List<Map<String,Object>> contrbutorList = commonDAO.find(contrbutorSql.toString());
		
		for(int i = 0; i < contrbutorList.size(); i++){
			Object[] literatureParams = new Object[1];
			literatureParams[0] = contrbutorList.get(i).get("contributorsName");
			
			StringBuilder literatureSql = new StringBuilder();
			
			literatureSql.append("select a.literature_id,a.literature_name,a.literature_url,a.contributors_name,a.image_url as photo "
					+ " from literature a where a.contributors_name = ?");
			
			List<Map<String,Object>> literatureList = commonDAO.find(literatureSql.toString(),literatureParams);
			
			for(int j = 0; j < literatureList.size(); j++){
				if(literatureList.get(j).get("photo")!=null){
					String photo = (String) literatureList.get(j).get("photo");
					photo = Constants.IP_ADDRESS + Constants.PICTURE_DIR + photo;
					literatureList.get(j).put("photo", photo);
				}
				if(literatureList.get(j).get("literatureUrl")!=null){
					String literatureUrl = (String) literatureList.get(j).get("literatureUrl");
					literatureUrl = Constants.IP_ADDRESS + Constants.DOCUMENT_DIR + literatureUrl;
					literatureList.get(j).put("literatureUrl", literatureUrl);
				}
			}
			
			retjson.put(Integer.toString(i), literatureList);
		}
		
		return retjson;
	}

	@Override
	public List<Map<String, Object>> getLiteratureInfo() {

		StringBuffer sql = new StringBuffer();
		sql.append("select a.literature_name from literature a");
		List<Map<String,Object>> list = commonDAO.find(sql.toString());

		return list;
	}

	@Override
	public List<Map<String, Object>> queryInfo(String literatureName, int classId, int page) {

		int i = 2;
		if(classId!=0){
			i++;
		}
		StringBuffer sql = new StringBuffer();
		Object[] params = new Object[i];
		params[0] = "%" + literatureName + "%";
		sql.append("select a.* from literature a where a.literature_name like ? ");
		if(classId!=0){
			params[1] = classId;
			sql.append(" and class_id = ? ");
		}
		params[--i] = page;
		sql.append(" limit ?,4");

		List<Map<String,Object>> list = commonDAO.find(sql.toString(), params);

		return list;
	}

	@Override
	public Integer sumPage(String literatureName, int classId) {

		int i = 1;
		if(classId!=0){
			i++;
		}
		StringBuffer sql = new StringBuffer();
		Object[] params = new Object[i];
		params[0] = "%" + literatureName + "%";
		sql.append("select a.* from literature a where a.literature_name like ? ");
		if(classId!=0){
			params[--i] = classId;
			sql.append(" and class_id = ? ");
		}

		List<Map<String,Object>> list = commonDAO.find(sql.toString(), params);

		Integer sumPage = list.size();

		return sumPage;
	}

}
