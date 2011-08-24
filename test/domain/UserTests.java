package domain;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserTests {
	User user;
	
	@Before
	public void setUp(){
		user = new User();
	}
	
	@Test
	public void upgradeLevel() throws Exception {
		Level[] levels = Level.values();
		for(Level level : levels){
			if(level.nextLevel() == null) continue;
			user.setLevel(level);
			user.upgradeLevel();
			assertThat(user.getLevel(), is(level.nextLevel()));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cannotUpgradeLevel() throws Exception {
		Level[] levels = Level.values();
		for(Level level : levels){
			if(level.nextLevel() != null) continue;
			user.setLevel(level);
			user.upgradeLevel();
		}
	}
}
