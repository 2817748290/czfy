package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.dao.ICommonDAO;

@Service(value="projectService")
public class ProjectService implements party.artemis.czfy.service.IProjectService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override
	public List<Map<String, Object>> getClaasification() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from classification a");
		
		List<Map<String, Object>> classificationList = commonDAO.find(sql.toString());
		
		return classificationList;
	}

	@Override
	public List<Map<String, Object>> getProjectList() {
		StringBuilder projectSql = new StringBuilder();
		projectSql.append("select a.project_id,a.project_name,a.publish_time from project a");
		
		List<Map<String, Object>> projectList = commonDAO.find(projectSql.toString());
		
		return projectList;
	}

	@Override
	public List<Map<String, Object>> queryProject(String projectName, int classificationId, int levelCode) {
		int i = 1;
		if(classificationId!=-1){
			i++;
		}
		if(levelCode!=-1){
			i++;
		}
		Object[] params = new Object[i];
		params[0] = "%" + projectName + "%";
		StringBuilder sql = new StringBuilder();
		sql.append("select a.project_id,a.project_name,a.publish_time from project a where a.project_name like ?");
		if(levelCode!=-1){
			params[--i] = levelCode;
			sql.append(" and a.level_code=?");
	}
		if(classificationId!=-1){
			params[--i] = classificationId;
			sql.append(" and a.classification_id=?");
		}

		for(int j = 0; j < params.length; j++){
			System.out.println(params[j]);
		}
		List<Map<String,Object>> projectList = commonDAO.find(sql.toString(),params);

		return projectList;
	}

	@Override
	public JSONObject getProjectById(int projectId) {
		JSONObject project = new JSONObject();
		Object [] projectParams = new Object[1];
		projectParams[0] = projectId;
		StringBuilder projectSql = new StringBuilder();
		
		projectSql.append("select a.project_content from project a where a.project_id = ?");
		
		List<Map<String, Object>> projectList = commonDAO.find(projectSql.toString(), projectParams);
		
		project.put("project", projectList.get(0));
		
		return project;
	}

	@Override
	public List<Map<String, Object>> pageProject(String projectName, int classificationId, int levelCode , int page) {
		int i = 2;
		if(classificationId!=-1){
			i++;
		}
		if(levelCode!=-1){
			i++;
		}
		Object[] params = new Object[i];
		params[0] = "%" + projectName + "%";
		StringBuilder sql = new StringBuilder();
		sql.append("select a.project_id,a.project_name,a.publish_time from project a where a.project_name like ?");
        params[--i] = page;
		if(levelCode!=-1){
			params[--i] = levelCode;
			sql.append(" and a.level_code=?");
		}
		if(classificationId!=-1){
			params[--i] = classificationId;
			sql.append(" and a.classification_id=?");
		}
		sql.append(" limit ?,8");


		for(int j = 0; j < params.length; j++){
			System.out.println(params[j]);
		}
		//videoSql.append(" limit ?,4");
		List<Map<String,Object>> projectList1 = commonDAO.find(sql.toString(),params);
		return  projectList1;
	}

    @Override
    public Integer getSumPage(String projectName, int classificationId, int levelCode) {
        int i = 1;
        if(classificationId!=-1){
            i++;
        }
        if(levelCode!=-1){
            i++;
        }
        Object[] params = new Object[i];
        params[0] = "%" + projectName + "%";
        StringBuilder sql = new StringBuilder();
        sql.append("select a.project_id,a.project_name,a.publish_time from project a where a.project_name like ?");
        if(levelCode!=-1){
            params[--i] = levelCode;
            sql.append(" and a.level_code=?");
        }
        if(classificationId!=-1){
            params[--i] = classificationId;
            sql.append(" and a.classification_id=?");
        }

        for(int j = 0; j < params.length; j++){
            System.out.println(params[j]);
        }
        List<Map<String,Object>> projectList = commonDAO.find(sql.toString(),params);

        return projectList.size();
    }

//	@Override
//	public List<Map<String, Object>> getProjectListByLevelCode(String projectName, String classificationId, String levelCode) {
//		StringBuilder sql = new StringBuilder();
//		if(projectName!=null && classificationId!=null)
//		sql.append("select a.* from classification a");
//
//		List<Map<String, Object>> classificationList = commonDAO.find(sql.toString());
//
//		return classificationList;
//	}


}
