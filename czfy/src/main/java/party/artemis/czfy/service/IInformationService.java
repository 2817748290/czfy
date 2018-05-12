package party.artemis.czfy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IInformationService {
    //获得总共新闻数据
    List<Map<String, Object>> getInformation();

    //发布新闻
    int addInfor(String inforTitle, int classId, String inforAuthor, String inforContent, Date inferDate);

    //多条件查询新闻
    List<Map<String, Object>> queryInfor(String inforTitle, int classId);

    //根据Id查看新闻
    Map<String,Object> getInforById(int inforId);
}
