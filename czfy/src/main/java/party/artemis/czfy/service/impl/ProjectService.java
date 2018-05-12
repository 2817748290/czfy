package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.IProjectService;

@Service(value="projectService")
public class ProjectService implements IProjectService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override
	public int publishProject(String projectName, int projectLevel, int projectClassification, boolean isTop,
			String projectContent) {
		Object[] params = new Object[5];
		params[0] = projectName;
		params[1] = projectClassification;
		params[2] = projectLevel;
		params[3] = projectContent;
		params[4] = isTop==true?1:0;
		StringBuilder sql = new StringBuilder();
		sql.append("insert into project(project_name,classification_id,level_code,project_content,is_top) ")
		.append("values(?,?,?,?,?)");
		
		int projectId = commonDAO.insert(sql.toString(), params);
		return projectId;
	}

	@Override
	public List<Map<String, Object>> getProjectType() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from classification a");
		
		List<Map<String, Object>> typeList = commonDAO.find(sql.toString());
		
		return typeList;
	}

	@Override
	public List<Map<String, Object>> getProjectLevel() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from level a");

		List<Map<String, Object>> levelList = commonDAO.find(sql.toString());

		return levelList;
	}

	@Override
	public List<Map<String, Object>> projectList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.project_id,a.project_name from project a");
		
		List<Map<String, Object>> prjectList = commonDAO.find(sql.toString());
		
		return prjectList;
	}

	@Override
	public List<Map<String, Object>> queryProject(String projectName,
												  int projectType, int projectLevel) {
		int i = 1;
		if(projectType!=0){
			i++;
		}
		if(projectLevel!=0){
			i++;
		}
		Object[] params = new Object[i];
		params[0] = "%" + projectName + "%";
		StringBuilder sql = new StringBuilder();
		sql.append("select a.project_id,a.project_name from project a where a.project_name like ?");
		if(projectType!=0){
			params[1] = projectType;
			sql.append(" and a.classification_id=?");
			if(projectLevel!=0){
				params[--i] = projectLevel;
				sql.append(" and a.level_code=? ");
			}
		}else{
			if(projectLevel!=0){
				params[--i] = projectLevel;
				sql.append(" and a.level_code=? ");
			}
		}
		
		List<Map<String,Object>> projectList = commonDAO.find(sql.toString(),params);
		
		return projectList;
	}

	@Override
	public Map<String, Object> projectInfo(int projectId) {
		Object[] params = new Object[1];
		params[0] = projectId;
		StringBuilder sql = new StringBuilder();
		sql.append("select a.project_id,a.project_name,a.classification_id,a.level_code,a.project_content")
		.append(",a.is_top from project a where a.project_id=?");
		
		List<Map<String,Object>> projectList = commonDAO.find(sql.toString(),params);
		
		return projectList.get(0);
	}

	@Override
	public void updateProject(String projectName, int projectLevel, int projectId, int projectClassification, boolean isTop,
			String projectContent) {
		Object[] params = new Object[6];
		params[0] = projectName;
		params[1] = projectClassification;
		params[2] = projectLevel;
		params[3] = projectContent;
		params[4] = isTop==true?1:0;
		params[5] = projectId;
		StringBuilder sql = new StringBuilder();
		sql.append("update project set project_name=?,classification_id=?,level_code=?,")
		.append("project_content=?,is_top=? where project_id=?");
		
		commonDAO.update(sql.toString(), params);
		
	}

	@Override
	public void deleteProject(int projectId) {
		Object[] params = new Object[1];
		params[0] = projectId;
		StringBuilder sql = new StringBuilder();
		sql.append("delete from project where project_id=?");
		
		commonDAO.update(sql.toString(), params);
		
	}

	@Override
	public void updateProjectCover(int projectId, String imageStoreName) {
		Object[] params = new Object[2];
		params[0] = imageStoreName;
		params[1] = projectId;
		StringBuilder sql = new StringBuilder();
		sql.append("update project set project_cover_url=? where project_id=?");
		
		commonDAO.update(sql.toString(), params);
	}

	@Override
	public JSONObject getProjectById(int projectId) {
		JSONObject project = new JSONObject();
		Object [] projectParams = new Object[1];
		projectParams[0] = projectId;
		StringBuilder projectSql = new StringBuilder();

		projectSql.append("select a.* from project a where a.project_id = ?");

		List<Map<String, Object>> projectList = commonDAO.find(projectSql.toString(), projectParams);

		project.put("project", projectList.get(0));

		return project;
	}


}
