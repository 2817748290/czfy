package party.artemis.czfy.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IInformationService;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping(value = "/infor")
public class informationController {

    private static Logger log = Logger.getLogger(ActorController.class);

    @Resource(name="informationService")
    private IInformationService informationService;

    @RequestMapping(value = "/getInfor")
    @ResponseBody
    public JSONObject getInfor(){
        JSONObject retjson = new JSONObject();

        List<Map<String,Object>> inforList = informationService.getInformation();

        retjson.put("inforList",inforList);

        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

        return retjson;
    }

    @ResponseBody
    @RequestMapping(value = "/queryInfor")
    public JSONObject queryInfor(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        String inforTitle = req.getString("inforTitle");
        int classId = req.getIntValue("classId");
        System.out.println("!!!!!!!!!!!:   " + inforTitle);

        List<Map<String,Object>> inforList = informationService.queryInfor(inforTitle, classId);

        retjson.put("data",inforList);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    @ResponseBody
    @RequestMapping(value = "/addInfor")
    public JSONObject addInfor(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        String inforTitle = req.getString("inforTitle");
        int classId = req.getIntValue("classId");
        String inforAuthor = req.getString("inforAuthor");
        String inforContent = req.getString("inforContent");
        Date inforDate = new Date();

        int inforId = informationService.addInfor(inforTitle,classId,inforAuthor,inforContent,inforDate);

        retjson.put("inforId", inforId);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    @ResponseBody
    @RequestMapping(value = "/getInforById")
    public JSONObject getInforById(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        int inforId = req.getIntValue("inforId");

        Map<String,Object> infor = informationService.getInforById(inforId);

        retjson.put("infor", infor);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

}
