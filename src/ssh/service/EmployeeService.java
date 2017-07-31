package ssh.service;

import java.util.List;

import ssh.dao.EmployeeDao;
import ssh.entities.Employee;

public class EmployeeService {
	private EmployeeDao employeeDao;

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
	/**
	 * 本处只是一个演示，所以这里的getAll服务里面只是直接调用DAO获取数据，没有有其他的业务逻辑。在具体的项目中，在这些服务方法中实现具体业务逻辑。
	 * @return
	 */
	public List<Employee> getAll(){
		return employeeDao.getAll();
	}
	
	public void delete(Integer id){
		employeeDao.delete(id);
	}
	
	public void saveOrUpdate(Employee employee){
		employeeDao.saveOrUpdate(employee);
	}
	
	/**
	 * 注意，这里的方法名不是以get开头的，而这个是读操作也应该是只读属性的，所以要在Spring的事务属性中单独设置一下本方法为只读属性的。
	 * @param lastName
	 * @return
	 */
	public boolean lastNameIsValid(String lastName){
		return employeeDao.getEmployeeByLastName(lastName) == null;
	}

	public Employee get(Integer id) {
		return employeeDao.get(id);
	}
}
