package party.artemis.czfy.service;

import java.util.List;
import java.util.Map;


public interface IAdminLoginService {
	List<Map<String,Object>> adminLogin(String username, String password);
}
