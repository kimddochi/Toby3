package dao;

import java.sql.SQLException;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserDaoTests {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDao dao = new UserDao(connectionMaker);
		
		UserDao dao = new DaoFactory().userDao();
		
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
