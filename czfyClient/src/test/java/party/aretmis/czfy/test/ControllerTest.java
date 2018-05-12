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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import party.artemis.czfy.controller.ActorController;
import party.artemis.czfy.controller.IndexController;
import party.artemis.czfy.controller.LiteratureController;
import party.artemis.czfy.controller.NewsController;
import party.artemis.czfy.controller.ProjectController;
import party.artemis.czfy.controller.VideoController;

import javax.servlet.ServletContext;




@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(value = { "classpath:spring-web.xml","classpath:spring-druid.xml" })
public class ControllerTest {
	private static Logger log = Logger.getLogger(ControllerTest.class);
			
	@Autowired
	private VideoController Controller;
	@Autowired
	private ActorController actorController;

	//@Autowired
	ServletContext context;
	
	MockMvc mock;
	 MockMvc mockActor;

	@Before  
	public void setup(){

		mock = MockMvcBuilders.standaloneSetup(Controller).build();
		mockActor = MockMvcBuilders.standaloneSetup(actorController).build();
	}
   
	@Test
    public void test() throws Exception {
		//准备参数 
        String jsonData = "{'videoId':22}";
        
        //发送请求
        RequestBuilder reqBuilder = MockMvcRequestBuilders.post("/client/getvideobyid")
        		.characterEncoding("UTF-8")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(jsonData)
        		.accept(MediaType.APPLICATION_JSON);
        
        ResultActions resultActions = this.mock.perform(reqBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        
        String result = mvcResult.getResponse().getContentAsString();
        log.debug("Mock客户端获得反馈数据:" + result);
    }

	@Test
	public void actorlist() throws Exception {
		//MvcResult result = mockActivity.perform(MockMvcRequestBuilders.get("/sponsorActivity/100007"))
		mockActor.perform(MockMvcRequestBuilders.post("/client/getactorlist"))
				//.andExpect(MockMvcResultMatchers.view().name("user/view"))
				//.andExpect(MockMvcResultMatchers.model().attributeExists("user"))
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
//	@Test
//	public void textvidio() throws Exception {
//		//MvcResult result = mockActivity.perform(MockMvcRequestBuilders.get("/sponsorActivity/100007"))
//		mock.perform(MockMvcRequestBuilders.post("/client/getVideoByName"))
//				.andExpect(MockMvcResultMatchers.view().name("user/view"))
//				.andExpect(MockMvcResultMatchers.model().attributeExists("user"))
//				.andDo(MockMvcResultHandlers.print())
//				.andReturn();
//	}
//
}