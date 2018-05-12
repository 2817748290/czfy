package party.artemis.czfy.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.alibaba.druid.pool.DruidDataSource;

@Repository(value="druidPool")
public class DruidPool {
	@Resource(name="druidDataSource")
	private DruidDataSource dataSource;
	
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		return dataSource.getConnection();
	}
	
	public void cleanUp(Connection conn, Statement sm,
			ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (sm != null) {
				sm.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			conn = null;
			sm = null;
			rs = null;
		}
	}

	public void setDataSource(DruidDataSource dataSource) {
		this.dataSource = dataSource;
	}
}
