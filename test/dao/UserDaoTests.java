package dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserDaoTests {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		
		ApplicationContext context =
			new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("whiteship");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println("###"+user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		
		System.out.println("###"+user2.getName());
	}

}
