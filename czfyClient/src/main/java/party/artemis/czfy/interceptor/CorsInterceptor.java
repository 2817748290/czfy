package party.artemis.czfy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CorsInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		
		response.addHeader("Content-Type", "application/json; charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "http://localhost");
		response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type,Access-Control");
		System.out.println("======处理请求之前======");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		response.addHeader("Content-Type", "application/json;; charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "http://localhost");
		response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type,Access-Control");
		
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		response.addHeader("Content-Type", "application/json;; charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "http://localhost");
		response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type,Access-Control");
	}

}
