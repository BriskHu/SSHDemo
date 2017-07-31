package ssh.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * 为了实现代码复用、提高效率，将所有XXDao类中的重复代码部分抽象为一个BaseDao，实现Dao的基本操作，然后由具体的Dao类继承这个BaseDao类。
 * 目前这个BaseDao很简单，实际的项目中，这个类会比较复杂。
 * @author Brisk
 *
 */
public class BaseDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 获取Session，即获取数据库链接。
	 * @return
	 */
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();  //因为本项目中使用的是事务，所以这里用getSession，而不用openSession()。
	}
}
