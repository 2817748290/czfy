package party.artemis.czfy.service.impl;

import org.springframework.stereotype.Service;
import party.artemis.czfy.dao.impl.CommonDAO;
import party.artemis.czfy.service.IInformationService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "informationService")
public class InformationService implements IInformationService {

    @Resource(name="commonDAO")
    private CommonDAO commonDAO;

    @Override
    public List<Map<String, Object>> getInformation() {

        StringBuffer sql = new StringBuffer();
        sql.append("select a.* from information a where isPass = 1");
        List<Map<String,Object>> inforList = commonDAO.find(sql.toString());
        System.out.println("!!!!!!   :" + sql.toString());
        return inforList;
    }

    @Override
    public int addInfor(String inforTitle, int classId, String inforAuthor, String inforContent, Date inforDate) {

        StringBuffer sql = new StringBuffer();

        Object[] params = new Object[5];
        params[0] = inforTitle;
        params[1] = classId;
        params[2] = inforAuthor;
        params[3] = inforContent;
        params[4] = inforDate;

        sql.append("insert into information(infor_title, class_id, infor_author, infor_content, infor_date) values(?,?,?,?,?)");

        int inforId = commonDAO.insert(sql.toString(),params);

        return inforId;
    }

    @Override
    public List<Map<String, Object>> queryInfor(String inforTitle, int classId) {
        int i = 1;
        if(classId!=0){
            i++;
        }

        StringBuffer sql = new StringBuffer();
        Object[] params = new Object[i];
        params[0] = "%" + inforTitle + "%";

        sql.append("select a.* from information a where infor_title like ? ");

        if(classId!=0){
            params[1] = classId;
            sql.append(" and class_id = ?");
        }

        sql.append(" and isPass =1");

        List<Map<String, Object>> inforList = commonDAO.find(sql.toString(), params);

        return inforList;
    }


    @Override
    public Map<String, Object> getInforById(int inforId) {

        StringBuffer sql = new StringBuffer();

        Object[] params = new Object[1];

        params[0] = inforId;

        sql.append("select infor_title, infor_author, infor_date from information where infor_id = ? and isPass = 1");

        List<Map<String, Object>> inforList = commonDAO.find(sql.toString(),params);

        if(!inforList.isEmpty()){
            return inforList.get(0);
        }
        return null;
    }

}
