package party.artemis.czfy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.service.IAdminLoginService;

@Service(value="adminLoginService")
public class AdminLoginService implements IAdminLoginService{

	@Resource(name="commonDAO")
	private ICommonDAO commonDAO;
	
	@Override//管理员登录
	public List<Map<String,Object>> adminLogin(String username, String password) {
		Object[] params = new Object[2];
		params[0] = username;
		params[1] = password;
		
		StringBuilder sql = new StringBuilder();
		sql.append("select admin_id,nickname from admin where username=? and password=?");
		
		List<Map<String,Object>> adminList = commonDAO.find(sql.toString(), params);
		
		return adminList;
	}
	
}
