package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.IVideoService;

@Service(value="videoService")
public class VideoService implements IVideoService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override
	public int addVideo(String videoName, String videoIntroducation, String nickname, Boolean isTop) {
		Object[] params = new Object[4];
		params[0] = videoName;
		params[1] = videoIntroducation;
		params[2] = nickname;
		params[3] = isTop==true?1:0;
		StringBuilder sql = new StringBuilder();
		sql.append("insert into video(video_name,video_introducation,author_nickname,is_top) ")
		.append("values(?,?,?,?)");
		int videoId = commonDAO.insert(sql.toString(), params);
		return videoId;
	}

	@Override
	public List<Map<String, Object>> videoList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.video_id,a.video_name from video a");
		List<Map<String, Object>> videoList = commonDAO.find(sql.toString());
		return videoList;
	}

	/**
	 * 模糊查询视频
	 * @param videoName 视频名
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryVideo(String videoName) {
		int i=0;
		if(videoName!=null){
			i++;
		}
		Object[] params = new Object[i];
		StringBuilder sql = new StringBuilder();
		sql.append("select a.video_id,a.video_name from video a  ");
		if(videoName!=null){
			i++;
			params[0] = "%" + videoName + "%";
			sql.append("where a.video_name like ?");
		}

		List<Map<String,Object>> videoList = commonDAO.find(sql.toString(),params);

		System.out.println("!!!!!!!!!!!:   "+ sql);

		return videoList;
	}

	@Override
	public Map<String, Object> videoInfo(int videoId) {
		Object[] params = new Object[1];
		params[0] = videoId;
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from video a where a.video_id=?");

		List<Map<String, Object>> videoList = commonDAO.find(sql.toString(),params);

		if(!videoList.isEmpty()){
			return videoList.get(0);
		}
		return null;
	}

	@Override
	public void updateVideo(int videoId, String videoName, String videoIntroducation, Boolean isTop) {
		Object[] params = new Object[4];
		params[0] = videoName;
		params[1] = videoIntroducation;
		params[2] = isTop==true?1:0;
		params[3] = videoId;
		StringBuilder sql = new StringBuilder();
		sql.append("update video set video_name=?, video_introducation=?,is_top=? where video_id=?");
		
		commonDAO.update(sql.toString(), params);
	}

	@Override
	public void uploadVideo(int videoId, String videoUrl) {
		Object[] params = new Object[2];
		params[0] = videoUrl;
		params[1] = videoId;
		StringBuilder sql = new StringBuilder();
		sql.append("update video set video_path=? where video_id=?");

		commonDAO.update(sql.toString(), params);
	}

	@Override
	public void uploadVideoCover(int videoId, String imageUrl) {
		Object[] params = new Object[2];
		params[0] = imageUrl;
		params[1] = videoId;
		StringBuilder sql = new StringBuilder();
		sql.append("update video set video_cover_url=? where video_id=?");

		commonDAO.update(sql.toString(), params);
	}

	@Override
	public void delVideo(int videoId) {
		Object[] params = new Object[1];
		params[0] = videoId;
		StringBuilder sql = new StringBuilder();
		sql.append("delete from video where video_id=?");
		commonDAO.update(sql.toString(), params);
	}

}
