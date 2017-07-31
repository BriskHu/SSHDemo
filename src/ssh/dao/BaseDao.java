package ssh.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Ϊ��ʵ�ִ��븴�á����Ч�ʣ�������XXDao���е��ظ����벿�ֳ���Ϊһ��BaseDao��ʵ��Dao�Ļ���������Ȼ���ɾ����Dao��̳����BaseDao�ࡣ
 * Ŀǰ���BaseDao�ܼ򵥣�ʵ�ʵ���Ŀ�У�������Ƚϸ��ӡ�
 * @author Brisk
 *
 */
public class BaseDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * ��ȡSession������ȡ���ݿ����ӡ�
	 * @return
	 */
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();  //��Ϊ����Ŀ��ʹ�õ�����������������getSession��������openSession()��
	}
}
