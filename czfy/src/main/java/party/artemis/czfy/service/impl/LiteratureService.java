package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.ILiteratureService;

@Service(value="literatureService")
public class LiteratureService implements ILiteratureService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override
	public int addContributors(String contributorsName, String literatureName) {
		Object[] params = new Object[2];
		params[0] = literatureName;
		params[1] = contributorsName;
		StringBuilder sql = new StringBuilder();
		sql.append("insert into literature(literature_name,contributors_name) values(?,?)");
		
		int literatureId = commonDAO.insert(sql.toString(), params);
		return literatureId;
	}

	@Override
	public List<Map<String, Object>> contributorsList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select DISTINCT a.contributors_name from literature a");
		List<Map<String, Object>> contributorsList = commonDAO.find(sql.toString());
		return contributorsList;
	}

	@Override
	public List<Map<String, Object>> queryContributors(String contributorsName) {
		Object[] params = new Object[1];
		params[0] = "%" + contributorsName + "%";
		StringBuilder sql = new StringBuilder();
		sql.append("select DISTINCT a.contributors_name from literature a where a.contributors_name like ?");
		
		List<Map<String,Object>> contributorsList = commonDAO.find(sql.toString(),params);
		
		return contributorsList;
	}

	@Override
	public List<Map<String, Object>> literatureInfo(String contributorsName) {
		Object[] params = new Object[1];
		params[0] = contributorsName;
		StringBuilder sql = new StringBuilder();
		sql.append("select DISTINCT a.literature_name,a.literature_id,")
		.append("a.contributors_name,ISNULL(a.literature_url) literature_url_is_null")
		.append(" from literature a where a.contributors_name=?");
		
		List<Map<String,Object>> literatureList = commonDAO.find(sql.toString(),params);
		return literatureList;
	}

	@Override
	public void updateContributors(String contributorsNewName, String contributorsOldName) {
		Object[] params = new Object[2];
		params[0] = contributorsNewName;
		params[1] = contributorsOldName;
		StringBuilder sql = new StringBuilder();
		sql.append("update literature set contributors_name=? where contributors_name=?");
		
		commonDAO.update(sql.toString(), params);

	}

	@Override
	public void delContributors(String contributorsName) {
		Object[] params = new Object[1];
		params[0] = contributorsName;
		StringBuilder sql = new StringBuilder();
		sql.append("delete from literature where contributors_name=?");
		
		commonDAO.update(sql.toString(), params);
		
	}

	

	@Override
	public void uploadLiterature(int literatureId, String literatureUrl) {
		Object[] params = new Object[2];
		params[0] = literatureUrl;
		params[1] = literatureId;
		StringBuilder sql = new StringBuilder();
		sql.append("update literature set literature_url=? where literature_id=?");
		
		commonDAO.update(sql.toString(), params);
		
	}

	@Override
	public void uploadContributorsCover(int literatureId, String imageUrl) {
		Object[] queryParams = new Object[1];
		queryParams[0] = literatureId;
		StringBuilder querySql = new StringBuilder();
		querySql.append("select a.contributors_name from literature a where a.literature_id=?");
		
		List<Map<String,Object>> contributorsNameList = commonDAO.find(querySql.toString(), queryParams);
		
		Object[] params = new Object[2];
		params[0] = imageUrl;
		params[1] = contributorsNameList.get(0).get("contributorsName");
		StringBuilder sql = new StringBuilder();
		sql.append("update literature set image_url=? where contributors_name=?");
		
		commonDAO.update(sql.toString(), params);
	}

	@Override
	public void delLiterature(int literatureId) {
		Object[] params = new Object[1];
		params[0] = literatureId;
		StringBuilder sql = new StringBuilder();
		sql.append("delete from literature where literature_id=?");
		
		commonDAO.update(sql.toString(), params);
		
	}

}
