package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import party.artemis.czfy.common.Constants;
import party.artemis.czfy.dao.ICommonDAO;

@Service(value="videoService")
public class VideoService implements party.artemis.czfy.service.IVideoService {

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;

	@Override
	public List<Map<String, Object>> getVideo() {

		StringBuilder videoSql = new StringBuilder();

		videoSql.append("select a.video_id,a.video_name,a.video_cover_url from video a");

		List<Map<String,Object>> videoList = commonDAO.find(videoSql.toString());

		for(int i = 0; i < videoList.size(); i++){
			if(videoList.get(i).get("videoCoverUrl")!=null){
				String videoCoverUrl = (String) videoList.get(i).get("videoCoverUrl");
				videoCoverUrl = Constants.IP_ADDRESS + Constants.PICTURE_DIR + videoCoverUrl;
				videoList.get(i).put("videoCoverUrl", videoCoverUrl);
			}
		}

		return videoList;
	}

	@Override
	public JSONObject getVideoById(int videoId) {
		JSONObject retjson = new JSONObject();

		Object[] videoParams = new Object[1];
		videoParams[0] = videoId;
		StringBuilder videoSql = new StringBuilder();

		videoSql.append("select a.video_id,a.video_name,a.video_introducation,a.video_path,a.upload_time,a.author_nickname from video a where a.video_id = ?");

		List<Map<String, Object>> video =  commonDAO.find(videoSql.toString(), videoParams);


		if(video.size()>0){
			String videoPath = (String) video.get(0).get("videoPath");
			videoPath = Constants.IP_ADDRESS + Constants.VIDEO_DIR + videoPath;
			video.get(0).put("videoPath", videoPath);
			retjson.put("video", video.get(0));
		}

		return retjson;
	}

	//获取总的页数
	@Override
	public Integer getSumPage(String videoName) {
		StringBuilder videoSql = new StringBuilder();

		int n = 0;

		if(videoName!=null) {
			n = 1;
		}

		Object[] params = new Object[n];

		if(videoName==null){
			videoSql.append("select a.video_id,a.video_name,a.video_cover_url from video a");

		}
		else {

			params[0] = "%" + videoName + "%";


			videoSql.append("select a.video_id,a.video_name,video_cover_url,a.video_introducation,a.video_path,a.upload_time,a.author_nickname from video a where a.video_name like ?");
		}
		List<Map<String,Object>> Sum = commonDAO.find(videoSql.toString(),params);

		int SumPage = Sum.size();

		return SumPage;
	}

	//根据视频名字模糊查询
	@Override
	public List<Map<String, Object>> getVideoByName(String videoName, int page) {

		StringBuilder videoSql = new StringBuilder();

		int n = 1;

		if(videoName!=null) {
			n = 2;
		}

		Object[] params = new Object[n];

		if(videoName==null){
			videoSql.append("select a.video_id,a.video_name,a.video_cover_url from video a");

			params[0] = page;
		}
		else {

			params[0] = "%" + videoName + "%";

			params[1] = page;

			videoSql.append("select a.video_id,a.video_name,video_cover_url,a.video_introducation,a.video_path,a.upload_time,a.author_nickname from video a where a.video_name like ?");
		}

		videoSql.append(" limit ?,4");

		List<Map<String,Object>> videoList = commonDAO.find(videoSql.toString(),params);

		for(int i = 0; i < videoList.size(); i++){
			if(videoList.get(i).get("videoCoverUrl")!=null){
				String videoCoverUrl = (String) videoList.get(i).get("videoCoverUrl");
				videoCoverUrl = Constants.IP_ADDRESS + Constants.PICTURE_DIR + videoCoverUrl;
				videoList.get(i).put("videoCoverUrl", videoCoverUrl);
			}
		}

		return videoList;
	}



}
