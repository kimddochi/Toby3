package springbook.user.dao;

public class DaoFactory {
	public UserDao userDao(){
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDao userDao = new UserDao(connectionMaker);
		return new UserDao(connectionMaker());
	}

	private DConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
