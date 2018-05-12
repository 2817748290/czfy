package party.aretmis.czfy.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.servlet.ServletContext;




@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(value = { "classpath:spring-web.xml","classpath:spring-druid.xml" })
public class ControllerTest {
	private static Logger log = Logger.getLogger(ControllerTest.class);
			
	//@Autowired
	//private AppGoodsController appGoodsController;
	
	//@Autowired
	ServletContext context;
	
	MockMvc mock;
	
	@Before  
	public void setup(){
		mock = MockMvcBuilders.standaloneSetup().build();
	}
   
	//@Test
    public void testGetVerifyCode() throws Exception {
		//准备参数 
        String jsonData = "{\"searchName\":\"a\"}";
        
        //发送请求
        RequestBuilder reqBuilder = MockMvcRequestBuilders.post("/app/goodsQuery")
        		.characterEncoding("UTF-8")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(jsonData)
        		.accept(MediaType.APPLICATION_JSON);
        
        ResultActions resultActions = this.mock.perform(reqBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        
        String result = mvcResult.getResponse().getContentAsString();
        log.debug("Mock客户端获得反馈数据:" + result);
    }
	
   
	
    
}  