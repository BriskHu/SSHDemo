package ssh.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ssh.entities.Employee;

public class EmployeeDao extends BaseDao{

//public class EmployeeDao {	
//	����Щͨ�ô��룬��ȡ��������ȥ��
//	private SessionFactory sessionFactory;
//
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//	
//	/**
//	 * ��ȡSession������ȡ���ݿ����ӡ�
//	 * @return
//	 */
//	public Session getSession() {
//		return this.sessionFactory.getCurrentSession();  //��Ϊ����Ŀ��ʹ�õ�����������������getSession��������openSession()��
//	}
//
	
	/**
	 * ����Hibernate�����ݿ��ȡ���ݡ�
	 * ����ط��������׳��֡�could not initialize proxy-no session���쳣����Ϊһ����Spring�����������У�������������ó�����getXX������Ҳ�����������������getXX����
	 * ����ʱ��ȡSession����getXX��������֮ʱ�ر�Session����������Щʱ����Ҫ���������ѯ���ݣ�����ڲ�ѯ�в�ʹ�����Ӳ�ѯ����ᵼ���ڲ�ѯ��ı�ʱ����get����������
	 * �ر���Session�����籾����"FROM Employee"��
	 * @return 
	 */
	public List<Employee> getAll(){
//		String hql = "FROM Employee";  //ʹ��Hibernate�Ĳ�ѯ��䡣ֻ��ѯEmployee��ᵼ��Hibernate��no session�Ĵ����쳣��
		String hql = "FROM Employee e LEFT JOIN FETCH e.department";  //ʹ�������������ӣ�ͬʱ��ѯdepartment�����ݡ������eΪָ���ı�ı�����ע������FROM ������Ҫ��ѯ�ı�����Ӧ��������ִ��ʱHibernate���Զ�����hbm.xml�ļ��滻Ϊ��Ӧ�ı�����
		
		return getSession().createQuery(hql).list();
	}
	
	public void delete(Integer id){
		String hql = "DELETE FROM Employee e WHERE e.id = ?";
		getSession().createQuery(hql).setInteger(0, id).executeUpdate();  //ע��HQL�еĲ����Ǵ�0��ʼ��ŵġ�
	}

	public void saveOrUpdate(Employee employee){
		getSession().saveOrUpdate(employee);
	}
	
	public Employee getEmployeeByLastName(String lastName){
		String hql = "FROM Employee e LEFT JOIN FETCH e.department WHERE e.lastName = ?";
		Query query = getSession().createQuery(hql).setString(0, lastName);
		
		return (Employee) query.uniqueResult();
	}
	
	public Employee get(Integer id){
		String hql = "FROM Employee e LEFT JOIN FETCH e.department WHERE e.id = ?";  //����������Ҫͬʱ�õ����ű�������ݣ�����������Ҫʹ������������ȷ��session�ڲ�ѯ���ű�ʱ���ִ򿪡����򽫱�Hibernate�����쳣��
		Query query = getSession().createQuery(hql).setInteger(0, id);
		
		return (Employee) query.uniqueResult();
	}
}
