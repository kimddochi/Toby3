package dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTests {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired private UserDao dao;
	
	@Autowired private DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;
	
	@org.junit.Before
	public void setUp(){
		
		this.dao = context.getBean("userDao", UserDao.class);
		
		this.user1 = new User("gyumee", "�ڼ�ö", "springno1", Level.BASIC, 1, 0, "1@test.com");
		this.user2 = new User("leegw700", "�̱��", "springno2", Level.SILVER, 55, 10, "2@test.com");
		this.user3 = new User("bumjin", "�ڹ���", "springno3", Level.GOLD, 100, 40, "3@test.com");
		
	}
	
	@Test
	public void addAndGet() throws Exception {
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(user1, userget1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(user2, userget2);
	}
	
	@Test
	public void count() throws Exception {
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void testname() throws Exception {
		
		ApplicationContext context =
			new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
		
	}
	
	@Test
	public void getAll() throws Exception {
		dao.deleteAll();
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user1, users3.get(1));
		checkSameUser(user2, users3.get(2));
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(3));
	}

	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey() throws Exception {
		dao.deleteAll();
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void update() throws Exception {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("���α�");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1Update = dao.get(user1.getId());
		checkSameUser(user1, user1Update);
		User user2Same = dao.get(user2.getId());
		checkSameUser(user2, user2Same);
	}
}
