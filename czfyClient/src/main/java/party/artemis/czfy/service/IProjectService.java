package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IProjectService {
	List<Map<String,Object>> getClaasification();
	List<Map<String,Object>> getProjectList();
	List<Map<String,Object>> queryProject(String projectName,int classificationId,
			int levelCode);
	JSONObject getProjectById(int projectId);
	List<Map<String, Object>> pageProject(String projectName,int classificationId,
						   int levelCode,int page);
//	List<Map<String,Object>> getProjectListByLevelCode();
	Integer getSumPage(String projectName,int classificationId,
					   int levelCode);
}
