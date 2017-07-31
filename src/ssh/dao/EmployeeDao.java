package ssh.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ssh.entities.Employee;

public class EmployeeDao extends BaseDao{

//public class EmployeeDao {	
//	将这些通用代码，提取到父类中去。
//	private SessionFactory sessionFactory;
//
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//	
//	/**
//	 * 获取Session，即获取数据库链接。
//	 * @return
//	 */
//	public Session getSession() {
//		return this.sessionFactory.getCurrentSession();  //因为本项目中使用的是事务，所以这里用getSession，而不用openSession()。
//	}
//
	
	/**
	 * 利用Hibernate从数据库获取数据。
	 * 这个地方是最容易出现“could not initialize proxy-no session”异常。因为一般在Spring的事务配置中，事务管理器配置成拦截getXX方法。也就是事务管理器会在getXX方法
	 * 调用时获取Session，在getXX方法结束之时关闭Session。而由于有些时候还需要从其他表查询数据，如果在查询中不使用连接查询，则会导致在查询别的表时，因get方法结束而
	 * 关闭了Session。正如本处的"FROM Employee"。
	 * @return 
	 */
	public List<Employee> getAll(){
//		String hql = "FROM Employee";  //使用Hibernate的查询语句。只查询Employee表会导致Hibernate报no session的代理异常。
		String hql = "FROM Employee e LEFT JOIN FETCH e.department";  //使用迫切左外连接，同时查询department的数据。这里的e为指定的表的别名。注意这里FROM 后面是要查询的表所对应的类名，执行时Hibernate会自动根据hbm.xml文件替换为对应的表名。
		
		return getSession().createQuery(hql).list();
	}
	
	public void delete(Integer id){
		String hql = "DELETE FROM Employee e WHERE e.id = ?";
		getSession().createQuery(hql).setInteger(0, id).executeUpdate();  //注意HQL中的参数是从0开始编号的。
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
		String hql = "FROM Employee e LEFT JOIN FETCH e.department WHERE e.id = ?";  //由于这里需要同时用到部门表里的数据，所以这里需要使用左外连接来确保session在查询部门表时保持打开。否则将报Hibernate代理异常。
		Query query = getSession().createQuery(hql).setInteger(0, id);
		
		return (Employee) query.uniqueResult();
	}
}
