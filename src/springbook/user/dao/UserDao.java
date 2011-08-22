package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public interface UserDao {

	void deleteAll();

	void add(final User user);

	int getCount() throws SQLException;
	
	User get(String id);

	List<User> getAll();

	void update(User user);
	
}
