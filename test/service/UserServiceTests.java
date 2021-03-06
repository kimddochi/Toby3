package service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.MailSender;
import springbook.user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTests {
	
	@Autowired private UserService userService;
	@Autowired private UserDao userDao;
	@Autowired private PlatformTransactionManager transactionManager;
	@Autowired private MailSender mailSender;
	
	List<User> users = null;

	@Before
	public void setUp(){
		users = Arrays.asList(
					new User("bumjin", "", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "1@test.com"),
					new User("joytouch", "", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "2@test.com"),
					new User("erwins", "", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "3@test.com"),
					new User("madnite1", "", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "4@test.com"),
					new User("green", "", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "5@test.com")
				);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
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
	
	static class TestUserService extends UserService{
		private String id;
		
		private TestUserService(String id){
			this.id = id;
		}
		
		@Override
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException{}
	
	@Test
	public void upgraeAllOrNothing() throws Exception {
		
		UserService testUserService = new TestUserService(users.get(3).getId());  //스태틱클래스인 TestUserService클래스의 생성자에 exception을 발생할 id를 셋팅한다.
		testUserService.setUserDao(this.userDao); //TestUserService클래스에서 상속하는 UserService의 수정자 메소드에 DI를 한다.
		testUserService.setTransactionManager(transactionManager);
		testUserService.setMailSender(mailSender);
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
}
