package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public class UserDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};

	public void deleteAll(){
		this.jdbcTemplate.update("delete from users");
	}

	public void add(final User user){
		this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
				user.getId(),
				user.getName(),
				user.getPassword());
	}

	public int getCount() throws SQLException{
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public User get(String id){
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id}, this.userMapper);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users", this.userMapper);
	}
	
}
