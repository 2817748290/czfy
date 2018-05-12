package party.artemis.czfy.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IInformationService {
    //获得总共新闻数据
    List<Map<String, Object>> getInformation();


    //多条件查询新闻
    List<Map<String, Object>> queryInfor(String inforTitle, int classId,int page);

    //根据Id查看新闻
    Map<String, Object> getInforById(int inforId);

    //	分页
    Integer getSumPage(String inforTitle,int classId);

}
