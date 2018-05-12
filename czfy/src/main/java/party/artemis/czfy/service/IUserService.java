package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;

public interface IUserService {
    //用户登录
    List<Map<String,Object>> userLogin(String username, String password);

    //用户注册
    int userRegister(String username, String password, String nickname);

}
