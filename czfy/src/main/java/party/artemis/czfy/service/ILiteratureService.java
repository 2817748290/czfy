package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;


public interface ILiteratureService {
	int addContributors(String contributorsName, String literatureName);
	List<Map<String,Object>> contributorsList();
	List<Map<String,Object>> queryContributors(String contributorsName);
	List<Map<String, Object>> literatureInfo(String contributorsName);
	void updateContributors(String contributorsNewName, String contributorsOldName);
	void uploadLiterature(int literatureId, String literatureUrl);
	void uploadContributorsCover(int literatureId, String imageUrl);
	void delContributors(String contributorsName);
	void delLiterature(int literatureId);
}
