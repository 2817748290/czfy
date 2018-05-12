package party.artemis.czfy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import party.artemis.czfy.dao.ICommonDAO;
import party.artemis.czfy.pool.DruidPool;

@Repository(value="commonDAO")
public class CommonDAO implements ICommonDAO {
	private static Logger log = Logger.getLogger(CommonDAO.class);
	
	@Resource(name="druidPool")
	private DruidPool druidPool;
	
	public void setDruidPool(DruidPool druidPool) {
		this.druidPool = druidPool;
	}

	public int insert(String sql) {
        return insert(sql,null);
	}
	
	public int insert(String sql,Object[] params){
		int result = 0;
        int id = -1;
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = druidPool.getConnection();
            ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);  
            
            if(params!=null && params.length>0){
		    	for(int i=0;i<params.length;i++){
		    		ps.setObject(i+1, params[i]);
			    }
		    }

            result = ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();  
            if (rs != null&&rs.next()) {  
                id=rs.getInt(1);  
            } 
            
            if (result >= 0){  
            	log.debug("语句: " + sql + " 执行成功，影响了"  + result + "行数据。");
            } else if (result == Statement.SUCCESS_NO_INFO){
            	log.debug("语句: " + sql + " 执行成功，影响的行数未知。");
            } else if (result == Statement.EXECUTE_FAILED){
            	log.debug("语句: " + sql + " 执行失败。");
            }
        } catch (SQLException e) {
			log.error("SQL异常，错误代码：",e);
		} catch (ClassNotFoundException e) {
			log.error("无法找到类：",e);
		}finally {  
        	druidPool.cleanUp(conn, ps, rs);
        }
        return id;
	}
	
	public void update(String sql) {
		update(sql,null);
	}
	
	public void update(String sql,Object[] params){
		int result = 0;
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = druidPool.getConnection();
            ps = conn.prepareStatement(sql);  
            
            if(params!=null && params.length>0){
		    	for(int i=0;i<params.length;i++){
		    		ps.setObject(i+1, params[i]);
			    }
		    }

            result = ps.executeUpdate();
            
            if (result >= 0){  
            	log.debug("语句: " + sql + " 执行成功，影响了"  + result + "行数据。");
            } else if (result == Statement.SUCCESS_NO_INFO){
            	log.debug("语句: " + sql + " 执行成功，影响的行数未知。");
            } else if (result == Statement.EXECUTE_FAILED){
            	log.debug("语句: " + sql + " 执行失败。");
            }
        } catch (SQLException e) {
			log.error("SQL异常，错误代码：",e);
		} catch (ClassNotFoundException e) {
			log.error("无法找到类：",e);
		}finally {  
        	druidPool.cleanUp(conn, ps, rs);
        }
	}

	public int[] batchInsert(List<String> sqlList) {
		return this.batchInsert(sqlList, null);
	}

	public int[] batchInsert(List<String> sqlList, List<Object[]> paramList) {
		int[] ids = new int[sqlList.size()];
		
		Connection conn = null;
		PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = druidPool.getConnection();
        	conn.setAutoCommit(false);
        	
        	for(int i=0;i<sqlList.size();i++){
        		Object[] params = paramList.get(i);
        		ps = conn.prepareStatement(sqlList.get(i),Statement.RETURN_GENERATED_KEYS); 
        		
    			if(params!=null && params.length>0){
    		    	for(int j=0;j<params.length;j++){
    		    		ps.setObject(j+1, params[j]);
    			    }
    		    }
    			
    			int result = ps.executeUpdate();
                
                rs = ps.getGeneratedKeys();  
                if (rs != null&&rs.next()) {  
                    ids[i]=rs.getInt(1);  
                } 
                
                if (result >= 0){
                	log.debug("语句: " + sqlList.get(i) + " 执行成功，影响了"  + result + "行数据。");
                } else if (result == Statement.SUCCESS_NO_INFO){
                	log.debug("语句: " + sqlList.get(i) + " 执行成功，影响的行数未知。");
                } else if (result == Statement.EXECUTE_FAILED){
                	log.debug("语句: " + sqlList.get(i) + " 执行失败。");
                }
    		}
            
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
			log.error("SQL异常，错误代码：",e);
		} catch (ClassNotFoundException e) {
			log.error("无法找到类：",e);
		}finally {  
        	druidPool.cleanUp(conn, ps, rs);
        }
		return ids;
	}

	public void batchUpdate(List<String> sqlList) {
		this.batchUpdate(sqlList,null);
	}

	public void batchUpdate(List<String> sqlList, List<Object[]> paramList) {
		Connection conn = null;
		PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = druidPool.getConnection();
        	conn.setAutoCommit(false);
        	
        	for(int i=0;i<sqlList.size();i++){
        		Object[] params = paramList.get(i);
        		ps = conn.prepareStatement(sqlList.get(i)); 
        		
    			if(params!=null && params.length>0){
    		    	for(int j=0;j<params.length;j++){
    		    		ps.setObject(j+1, params[j]);
    			    }
    		    }
    			
    			int result = ps.executeUpdate();
                
                if (result >= 0){
                	log.debug("语句: " + sqlList.get(i) + " 执行成功，影响了"  + result + "行数据。");
                } else if (result == Statement.SUCCESS_NO_INFO){
                	log.debug("语句: " + sqlList.get(i) + " 执行成功，影响的行数未知。");
                } else if (result == Statement.EXECUTE_FAILED){
                	log.debug("语句: " + sqlList.get(i) + " 执行失败。");
                }
    		}
            
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
			log.error("SQL异常，错误代码：",e);
		} catch (ClassNotFoundException e) {
			log.error("无法找到类：",e);
		}finally {  
        	druidPool.cleanUp(conn, ps, rs);
        }
	}

	public int count(String sql) {
		return this.count(sql,null);
	}

	public int count(String sql, Object[] params) {
		int total = 0;
		
		ResultSet rs = null;
	    Connection conn = null;
	    PreparedStatement ps = null;
	    
		try{
			log.debug("countRecordsByCond(sql="+sql.toString()+")");
		    
		    conn = druidPool.getConnection();
		    ps = conn.prepareStatement(sql.toString());
		    
		    if(params!=null && params.length>0){
		    	for(int i=0;i<params.length;i++){
		    		ps.setObject(i+1, params[i]);
			    }
		    }
		    
		    rs = ps.executeQuery();
		    if(rs.next()){
		    	total = rs.getInt(1);
		    }
		} catch (SQLException e) {
			log.error("SQL异常，错误代码：",e);
		} catch (ClassNotFoundException e) {
			log.error("无法找到类：",e);
		}finally {  
        	druidPool.cleanUp(conn, ps, rs);
        }
		return total;
	}

	public List<Map<String, Object>> find(String sql) {
		return this.find(sql, null);
	}

	public List<Map<String, Object>> find(String sql, Object[] params) {
		log.debug("queryRecordsByCond(sql="+sql+")");
		
		List<Map<String, Object>> rsList = new ArrayList<Map<String,Object>>();
		
		ResultSet rs = null;
	    Connection conn = null;
	    PreparedStatement ps = null;

	    try{
		    conn = druidPool.getConnection();
		    ps=conn.prepareStatement(sql.toString());
		    
		    if(params!=null && params.length>0){
		    	for(int i=0;i<params.length;i++){
		    		ps.setObject(i+1, params[i]);
			    }
		    }
		    
		    rs = ps.executeQuery();
		    ResultSetMetaData rsmd = rs.getMetaData();
		    int colCount = rsmd.getColumnCount();
		    
		    while(rs.next()){
		    	Map<String,Object> rsMap = new HashMap<String,Object>();
		    	
		    	for(int i=1;i<=colCount;i++){
		    		
		    		String fieldName = this.replaceUnderlineAndfirstToUpper(rsmd.getColumnLabel(i));
		    		//log.debug("字段"+fieldName+"的类型为:"+rsmd.getColumnType(i));
		    		switch(rsmd.getColumnType(i)){
		    			case java.sql.Types.VARCHAR:
		    				rsMap.put(fieldName, rs.getString(i));
		    				break;
		    			case java.sql.Types.LONGVARCHAR:
		    				rsMap.put(fieldName, rs.getString(i));
		    				break;
		    			case java.sql.Types.TINYINT:
		    				rsMap.put(fieldName, rs.getByte(i));
		    				break;
		    			case java.sql.Types.BIT:
		    				rsMap.put(fieldName, rs.getByte(i));
		    				break;
		    			case java.sql.Types.BIGINT:
		    				rsMap.put(fieldName, rs.getLong(i));
		    				break;
		    			case java.sql.Types.INTEGER:
		    				rsMap.put(fieldName, rs.getInt(i));
		    				break;
		    			case java.sql.Types.DECIMAL:
		    				rsMap.put(fieldName, rs.getFloat(i));
		    				break;
		    			case java.sql.Types.TIMESTAMP:
		    				rsMap.put(fieldName, rs.getTimestamp(i)==null?"":rs.getTimestamp(i).toString());
		    				break;
		    			case java.sql.Types.DATE:
		    				rsMap.put(fieldName,  rs.getDate(i)==null?"":rs.getDate(i).toString());
		    				break;
		    			case java.sql.Types.CHAR:
		    				rsMap.put(fieldName, rs.getString(i));
		    				break;
		    			case java.sql.Types.FLOAT:
		    				rsMap.put(fieldName, rs.getFloat(i));
		    				break;
		    			case java.sql.Types.DOUBLE:
		    				rsMap.put(fieldName, rs.getDouble(i));
		    				break;
		    			case java.sql.Types.REAL:
		    				rsMap.put(fieldName, rs.getDouble(i));
		    				break;
		    		} 
		    	}
		    	rsList.add(rsMap);
		    }
	    } catch (SQLException e) {
			log.error("SQL异常，错误代码：",e);
		} catch (ClassNotFoundException e) {
			log.error("无法找到类：",e);
		}finally {  
        	druidPool.cleanUp(conn, ps, rs);
        }
		return rsList;
	}
	
	//替换字符串并让它的下一个字母为大写 
	private String replaceUnderlineAndfirstToUpper(String srcStr){
		String org = "_";
		String ob = "";
		String newString = "";
		int first=0;  
		while(srcStr.indexOf(org)!=-1){  
			first=srcStr.indexOf(org);  
			if(first!=srcStr.length()){  
				newString=newString+srcStr.substring(0,first)+ob;  
				srcStr=srcStr.substring(first+org.length(),srcStr.length());  
				srcStr=firstCharacterToUpper(srcStr);  
			}  
		}  
		newString=newString+srcStr;  
		return newString;  
	}
	
	//首字母大写 
	private String firstCharacterToUpper(String srcStr) {  
		return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);  
	}
}
