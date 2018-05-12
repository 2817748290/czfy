package party.artemis.czfy.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IProjectService {
	int publishProject(String projectName, int projectLevel,
			int projectClassification, boolean isTop, String projectContent);
	//获取项目类型
	List<Map<String,Object>> getProjectType();
	//获取项目等级
	List<Map<String,Object>> getProjectLevel();
	//获取项目列表
	List<Map<String,Object>> projectList();
	//多条件查询项目
	List<Map<String,Object>> queryProject(String projectName, int projectType, int projectLevel);
	//根据id获取项目信息
	Map<String,Object> projectInfo(int projectId);
	//修改项目信息
	void updateProject(String projectName, int projectLevel,int projectId,
			int projectClassification, boolean isTop, String projectContent);
	//删除项目
	void deleteProject(int projectId);
	//修改项目封面
	void updateProjectCover(int projectId, String imageStoreName);
	//根据id获取项目信息（包含图片路径）
	JSONObject getProjectById(int projectId);
}
