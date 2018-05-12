package party.artemis.czfy.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import party.artemis.czfy.common.Constants;
import party.artemis.czfy.service.IManagerInforService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资讯平台 管理员端
 */
@Controller
@RequestMapping(value = "/admin/infomation")
public class ManagerInforController {

    private static Logger log = Logger.getLogger(ActorController.class);

    @Resource(name="managerInforService")
    private IManagerInforService managerInforService;

    /**
     * 获取所有资讯记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getInformation" , method = {RequestMethod.GET,RequestMethod.POST})
    public JSONObject getInfor(){
        JSONObject retjson = new JSONObject();

        List<Map<String,Object>> inforList = managerInforService.getInformation();

        retjson.put("inforList",inforList);

        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);

        return retjson;
    }

    /**
     * 多条件查询
     * @inforTitle 资讯标题
     * @classId 资讯类型
     * @isPass 审核情况
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInfor")
    public JSONObject queryInfor(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        String inforTitle = req.getString("inforTitle");
        int classId = req.getIntValue("classId");
        int isPass = req.getIntValue("isPass");

        List<Map<String,Object>> inforList = managerInforService.queryInfor(inforTitle,classId,isPass);

        retjson.put("inforList",inforList);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    /**
     * 添加资讯
     * @param body
     * @return
     */
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
        int isPass = req.getIntValue("isPass");

        int inforId = managerInforService.addInfor(inforTitle,classId,inforAuthor,inforContent,inforDate,isPass);

        retjson.put("inforId", inforId);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    /**
     * 获取审核值对应的信息
     * @return
     */
    @RequestMapping(value="/getPassInfo",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject getPassInfo(){
        JSONObject retjson = new JSONObject();

        List<Map<String, Object>> passList = managerInforService.getPassinfo();
        retjson.put("passList", passList);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    /**
     * 审核新闻
     * @param body
     * @return
     */
    @RequestMapping(value = "/isPass", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject isPassNews(@RequestBody String  body){

        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        int inforId = req.getIntValue("inforId");

        int isPass = req.getIntValue("isPass");
        System.out.println("111111111111111111:      " + isPass + inforId);
        managerInforService.isPassInfor (isPass, inforId);

        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    /**
     * 查看资讯详情
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getInforById")
    public JSONObject getInforById(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        int inforId = req.getIntValue("inforId");

        Map<String,Object> infor = managerInforService.getInforById(inforId);
        retjson.put("infor", infor);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    /**
     * 修改资讯
     * @param body
     * @return
     */
    @RequestMapping(value="/updateInfor",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject updateInfor(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        int inforId = req.getIntValue("inforId");
        String inforTitle = req.getString("inforTitle");
        int classId = req.getIntValue("classId");
        String inforAuthor = req.getString("inforAuthor");
        String inforContent = req.getString("inforContent");
        Date inforDate = new Date();
        int isPass = req.getIntValue("isPass");
        System.out.println("111111111111111111:      " + isPass);
        managerInforService.updateInfor(inforId, inforTitle, classId, inforAuthor, inforContent, inforDate, isPass);
        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }

    /**
     * 删除资讯
     * @param body
     * @return
     */
    @RequestMapping(value="/delInfor",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject delNews(@RequestBody String body){
        JSONObject retjson = new JSONObject();

        log.debug("请求JSON:" + body);

        JSONObject req = JSONObject.parseObject(body);

        int inforId = req.getIntValue("inforId");

        managerInforService.delInfor(inforId);

        retjson.put(Constants.STATUS, Constants.SUCCESS_STATUS);
        return retjson;
    }
}
