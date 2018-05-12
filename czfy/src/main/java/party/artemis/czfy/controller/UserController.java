package party.artemis.czfy.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IUserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping(value="/user")
@Controller
public class UserController {
    private static Logger log = Logger.getLogger(AdminLoginController.class);

    @Resource(name="UserService")
    private IUserService userService;

    @RequestMapping(value="/userLogin",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject Login(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        String nextPage = "/login.html";

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        String username = req.getString("username");
        String password = req.getString("password");

        List<Map<String,Object>> userList = userService.userLogin(username, password);

        if(!userList.isEmpty()){
            nextPage = "/home.html";
            retjson.put("user", userList.get(0));
        }

        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        retjson.put("nextPage", nextPage);
        return retjson;
    }

    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject register(@RequestBody String body){
        JSONObject retjson = new JSONObject();
        String nextPage = "/reg.html";

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        String username = req.getString("username");
        String password = req.getString("password");
        String nickname = req.getString("nickname");

        if(nickname==null){
            nickname = username + UUID.randomUUID();
        }

        int userId = userService.userRegister(username, password, nickname);

        if(userId!=0){
            nextPage = "/login.html";
        }
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        retjson.put("nextPage", nextPage);
        return retjson;
    }
}
