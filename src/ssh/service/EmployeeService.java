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
	 * ����ֻ��һ����ʾ�����������getAll��������ֻ��ֱ�ӵ���DAO��ȡ���ݣ�û����������ҵ���߼����ھ������Ŀ�У�����Щ���񷽷���ʵ�־���ҵ���߼���
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
	 * ע�⣬����ķ�����������get��ͷ�ģ�������Ƕ�����ҲӦ����ֻ�����Եģ�����Ҫ��Spring�����������е�������һ�±�����Ϊֻ�����Եġ�
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
