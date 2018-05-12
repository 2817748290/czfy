package party.artemis.czfy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 管理员 新闻发布平台
 */
public interface IManagerInforService {

    //获得新闻数据
    List<Map<String,Object>> getInformation();

    //发布新闻
    int addInfor(String inforTitle, int classId, String inforAuthor, String inforContent, Date inforDate, int isPass);

    //多条件查询新闻
    List<Map<String,Object>> queryInfor(String inforTitle, int classId, int isPass);

    //修改新闻
    void updateInfor(int inforId, String inforTitle, int classId, String inforAuthor, String content, Date inforDate, int isPass);

    void delInfor(int inforId);

    //审核新闻
    void isPassInfor(int isPass, int inforId);

    //获取审核值对应项
    List<Map<String,Object>> getPassinfo();

    //获取之前审核的情况
    Map<String,Object> getPassState(int inforId);

    //根据Id查看新闻
    Map<String,Object> getInforById(int inforId);

}
