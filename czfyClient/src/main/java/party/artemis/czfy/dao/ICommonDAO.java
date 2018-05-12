package party.artemis.czfy.dao;

import java.util.List;
import java.util.Map;

public interface ICommonDAO {
	public List<Map<String, Object>> find(String sql);
	public List<Map<String, Object>> find(String sql,Object[] params);
	
	public int insert(String sql);
	public int insert(String sql, Object[] params);
	
	public void update(String sql);
	public void update(String sql, Object[] params);
	
	public int[] batchInsert(List<String> sqlList);
	public int[] batchInsert(List<String> sqlList,List<Object[]> paramList);
	
	public void batchUpdate(List<String> sqlList);
	public void batchUpdate(List<String> sqlList,List<Object[]> paramList);
	
	public int count(String sql);
	public int count(String sql,Object[] params);
}
