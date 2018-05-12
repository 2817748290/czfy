package party.artemis.czfy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IInformationService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        int page = req.getIntValue("page");
        int finalPage = page*16;
        System.out.println("!!!!!!!!!!!:   " + inforTitle);

        List<Map<String,Object>> inforList = informationService.queryInfor(inforTitle, classId, finalPage);

        retjson.put("data",inforList);
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

    @ResponseBody
    @RequestMapping(value = "/getInfoSumPage")
    public JSONObject getInfoSumPage(@RequestBody String body){
        JSONObject retjson = new JSONObject();
        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);
        String inforTitle = req.getString("inforTitle");
        int classId = req.getIntValue("classId");

        int sumPage = informationService.getSumPage(inforTitle,classId);

        int finalPage = 0;

        if(sumPage%16>0){
            finalPage = (sumPage/16)+1;
        }else{
            finalPage = (sumPage/16);
        }
        retjson.put("finalPage", finalPage);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;


    }

}
