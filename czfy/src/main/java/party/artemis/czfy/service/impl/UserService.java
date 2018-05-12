package party.artemis.czfy.service.impl;

import org.springframework.stereotype.Service;
import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.IUserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service(value="UserService")
public class UserService implements IUserService {
    @Resource(name="commonDAO")
    private ICommonDAO commonDAO;

    @Override//用户登录
    public List<Map<String,Object>> userLogin(String username, String password) {
        Object[] params = new Object[2];
        params[0] = username;
        params[1] = password;

        StringBuilder sql = new StringBuilder();
        sql.append("select user_id,nickname from user where username=? and password=?");

        List<Map<String,Object>> userList = commonDAO.find(sql.toString(), params);

        return userList;
    }

    @Override
    public int userRegister(String username, String password, String nickname) {
        Object[] params = new Object[3];
        params[0] = username;
        params[1] = password;
        params[2] = nickname;

        StringBuffer sql = new StringBuffer();
        sql.append("insert into user(username,password,nickname) values(?,?,?)");

        int userId = commonDAO.insert(sql.toString(),params);

        return userId;

    }
}
