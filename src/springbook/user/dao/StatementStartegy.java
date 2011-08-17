package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStartegy {
	PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
