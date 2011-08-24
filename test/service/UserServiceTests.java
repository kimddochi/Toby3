package service;

import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTests {
	
	@Autowired private UserService userService;
	@Autowired private UserDao userDao;
	List<User> users = null;

	@Before
	public void setUp(){
		users = Arrays.asList(
					new User("bumjin", "", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
					new User("joytouch", "", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
					new User("erwins", "", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
					new User("madnite1", "", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
					new User("green", "", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
				);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), false);
		checkLevel(users.get(1), true);
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true);
		checkLevel(users.get(4), false);
	}

	private void checkLevel(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded){
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else{
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void add() throws Exception {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
		
	}
	
}
