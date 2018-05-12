package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

public interface IVideoService {
	int addVideo(String videoName, String videoIntroducation, String nickname, Boolean isTop);
	List<Map<String,Object>> videoList();
	//模糊查询视频
	List<Map<String,Object>> queryVideo(String videoName);
	Map<String,Object> videoInfo(int videoId);
	void updateVideo(int videoId, String videoName, String videoIntroducation, Boolean isTop);
	void uploadVideo(int videoId, String videoUrl);
	void uploadVideoCover(int videoId, String imageUrl);
	void delVideo(int videoId);
}
