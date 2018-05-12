package party.artemis.czfy.service.impl;

import org.springframework.stereotype.Service;
import party.artemis.czfy.dao.impl.CommonDAO;
import party.artemis.czfy.service.IManagerInforService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value="managerInforService")
public class ManagerInforService implements IManagerInforService {

    @Resource(name="commonDAO")
    private CommonDAO commonDAO;

    @Override
    public List<Map<String, Object>> getInformation() {

        StringBuffer sql = new StringBuffer();
        sql.append("select a.* from information a ");
        List<Map<String,Object>> inforList = commonDAO.find(sql.toString());

        return inforList;
    }

    @Override
    public int addInfor(String inforTitle, int classId, String inforAuthor, String inforContent, Date inforDate, int isPass) {
        StringBuffer sql = new StringBuffer();

        Object[] params = new Object[6];
        params[0] = inforTitle;
        params[1] = classId;
        params[2] = inforAuthor;
        params[3] = inforContent;
        params[4] = inforDate;
        params[5] = isPass;

        sql.append("insert into information(infor_title, class_id, infor_author, infor_content, infor_date,isPass) values(?,?,?,?,?,?)");

        int inforId = commonDAO.insert(sql.toString(),params);

        return inforId;
    }

    @Override
    public List<Map<String, Object>> queryInfor(String inforTitle, int classId, int isPass) {
        int i = 1;
        if(classId!=0){
            i++;
        }
        if(isPass!=-1){
            i++;
        }
        StringBuffer sql = new StringBuffer();
        Object[] params = new Object[i];
        params[0] = "%" + inforTitle + "%";

        sql.append("select a.* from information a where infor_title like ? ");

        if(classId!=0){
            params[1] = classId;
            sql.append(" and class_id = ?");
            if(isPass!=-1){
                params[--i] = isPass;
                sql.append(" and isPass = ?");
            }
        }else{
            if(isPass!=-1){
                params[--i] = isPass;
                sql.append(" and isPass = ?");
            }
        }

        List<Map<String, Object>> inforList = commonDAO.find(sql.toString(), params);

        return inforList;
    }

    @Override
    public void updateInfor(int inforId, String inforTitle, int classId, String inforAuthor, String content, Date inforDate, int isPass) {
        Object[] params = new Object[7];
        params[0] = inforTitle;
        params[1] = classId;
        params[2] = inforAuthor;
        params[3] = content;
        params[4] = inforDate;
        params[5] = isPass;
        params[6] = inforId;
        StringBuilder sql = new StringBuilder();
        sql.append("update information set infor_title=?, class_id=?, infor_author=?, infor_content=?, infor_date=?, isPass=? where infor_id=?");

        commonDAO.update(sql.toString(), params);
    }

    @Override
    public void delInfor(int inforId) {
        Object[] params = new Object[1];
        params[0] = inforId;
        StringBuilder sql = new StringBuilder();
        sql.append("delete from information where infor_id=?");

        commonDAO.update(sql.toString(), params);
    }

    @Override
    public void isPassInfor(int isPass, int inforId) {

        StringBuffer sql = new StringBuffer();
        Object[]  params = new Object[2];
        params[0] = isPass;
        params[1] = inforId;
        sql.append("update information set isPass=? where infor_id=?");
        System.out.println("2222222222     :    " +  sql );

        commonDAO.update(sql.toString(), params);

    }

    @Override
    public List<Map<String, Object>> getPassinfo() {
        StringBuffer sql = new StringBuffer();

        sql.append("select a.* from pass a");

        List<Map<String, Object>> passList = commonDAO.find(sql.toString());

        System.out.println(passList);

        return passList;
    }

    @Override
    public Map<String, Object> getPassState(int inforId) {
        StringBuffer sql = new StringBuffer();

        Object[] params = new Object[1];

        params[0] = inforId;

        sql.append("select a.isPass from information a where a.infor_id = ?");

        List<Map<String, Object>> getPasslist = commonDAO.find(sql.toString(),params);

        return getPasslist.get(0);
    }

    @Override
    public Map<String, Object> getInforById(int inforId) {
        StringBuffer sql = new StringBuffer();

        Object[] params = new Object[1];

        params[0] = inforId;

        sql.append("select a.* from information a where infor_id = ?");

        List<Map<String, Object>> inforList = commonDAO.find(sql.toString(),params);

        if(!inforList.isEmpty()){
            return inforList.get(0);
        }
        return null;
    }
}
