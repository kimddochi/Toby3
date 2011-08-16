package dao;

import java.sql.SQLException;

import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserDaoTests {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		UserDao dao = new UserDao(connectionMaker);
		
		User user = new User();
		user.setId("whiteship");
		user.setName("��⼱");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println("###"+user.getId() + " ��� ����");
		
		User user2 = dao.get(user.getId());
		
		System.out.println("###"+user2.getName());
	}

}
