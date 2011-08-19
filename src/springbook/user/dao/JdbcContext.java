package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import springbook.user.domain.User;

public class JdbcContext {
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void executeSql(final String query) throws SQLException {
		workWithStatementStrategy(
				new StatementStrategy() {
					@Override
					public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
						PreparedStatement ps = c.prepareStatement(query);
						return ps;
					}
				}
		);
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			
			ps = stmt.makePreparedStatement(c);
			
			ps.execute();
		} catch (SQLException e) {
			throw e;
		}finally{
			if(ps!=null) try{ ps.close(); }catch(SQLException e){}
			if(c!=null)  try{  c.close(); }catch(SQLException e){}
		}
	}
}
