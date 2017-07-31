package ssh.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import ssh.entities.Employee;
import ssh.service.DepartmentService;
import ssh.service.EmployeeService;

/**
 * ��ӦJSPҳ�涯����JavaBean���������Action��ҵ���߼���
 * @author Brisk
 *
 */
public class EmployeeAction extends ActionSupport implements RequestAware, 
Preparable, ModelDriven<Employee>{
	
	private static final long serialVersionUID = 1L;
	private EmployeeService employeeService;
	private Map<String, Object> request;
	private DepartmentService departmentService;
	
	public void setEmployeeService(EmployeeService employeeService){
		this.employeeService = employeeService;
	}
	
	public void setDepartmentService(DepartmentService departmentService){
		this.departmentService = departmentService;
	}
	
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}
	
	public String list(){
		request.put("employees", employeeService.getAll());  //������佫serviceȡ�����ݷ��뵽Request���emloyee�У�ʵ�ֽ�������ʾ��ҳ���ϡ�Ҫ�����ݷ��뵽Request���У�����Ҫʵ��RequestAware�ӿڡ�
		return "list";
	}

	private Integer id;
	
	public void setId(Integer id){
		this.id = id;
	}
	
	private InputStream inputStream;  //ʹ��Ajax��ʽ��������ֶΡ�
	
	public InputStream getInputStream(){
		return inputStream;
	}
	
	public String delete(){
		/*
		 * ֱ��ɾ����ʽ��
		 */
//		employeeService.delete(id); 
//		return "deleteSuccess";  //���÷�AJAX��ʽɾ��ʱ����Ҫˢ��ҳ�����ʵʱ��ʾ���������������struts.xml�����ö�Ӧ��result��typeΪredirectֵ�������ض���ʵ���Զ�ˢ�²�����ע�����з�ʽʵ���ϴ����ǱȽϸߵģ���Ϊ��Ҫ�������󣬲����²�ѯ��

		/*
		 * ʹ��AJAX��ʽɾ����
		 * ʹ��AJAX��ʽɾ��ʱ����Ҫ��Action���ж���һ��InputStream���͵��ֶΣ�ͬʱ��ҪΪ������ʵ�ֶ�Ӧ��get������Ȼ���ڴ˷�����Ҳ��Ҫʹ��AJAX��ʽ�ķ�������ʹ�ø�������
		 */
		try {
			employeeService.delete(id);
			inputStream = new ByteArrayInputStream("1".getBytes("UTF-8"));  //��ͻ��˷���ֵΪ��1���ı�־������ʾɾ���ɹ���ֻ��Ҫ����һ����������Struts��ӹ������������Ȼ�󴴽���Ӧ��������������Ӧ�Ľ�������ǲ��ù����������struts������ǹ���á�
		} catch(Exception e) {
			e.printStackTrace();
			try {
				inputStream = new ByteArrayInputStream("0".getBytes("UTF-8"));  //��ͻ��˷���ֵΪ��0���ı�־������ʾɾ��ʧ�ܡ�
			} catch (UnsupportedEncodingException e1) {  //����쳣��ʾ�Ƿ�֧��UTF-8���룬����쳣����ûʲô�ã����ž��С�
				e1.printStackTrace();
			}
		}
		
		return "deleteAjax";  //����AJAX�󣬲�Ҫˢ�¾Ϳ��Ը��½��,�������ﷵ��ʲôֵ������Ҫ��ֻ��Ҫ��struts.xml�ж�Ӧresult��ǩ��name���Ա���һ�¼��ɡ����²���������jsp�ж�Ӧ��js�ű�����AJAX���صı�־��ʵ�ֶ�̬ˢ�µġ�
	}
	
	/**
	 * �����������������������ӵĶ������Ȳ�ѯ��������ѯ����ŵ�request���У�Ȼ��ת������ҳ�档
	 * @return
	 */
	public String addEmp(){
		request.put("departments", departmentService.getAll());  //����ѯ����department���뵽request���У��Ӷ���JSPҳ����ʾ������
		return "addEmp";
	}

	/**
	 * ����������ڱ༭������
	 * ����ʹ����paramsPrepareParamsStack������ջ��������ҳ���ϵ����Ӧ��¼�ı༭�����Ӻ�struts���ȵ��������������ִ�У�Ȼ����ִ��addEmp������
	 * ����������У���ɱ༭�����е����ݻ��Բ�����
	 */
	public void prepareAddEmp(){
		if(id != null){
			model = employeeService.get(id);  //����prepareAddEmp����������addEmp����ִ�У�������ִ��addEmp����֮ǰʱ��model���󱣴��Ŵ����ݿ��в�ѯ����ֵ��Struts����ֱ���Զ�ʹ�����ֵ��ʾ��ҳ���ϡ�
		}
	}
	 
	private Employee model;  //���ڽ��ձ��ύ���ݡ�

	public String save(){
		//System.out.println(model);
		if(id == null){  //����ÿ�ζ��Ǵ���һ���µ�model�����ڱ༭��¼����ʱ�����createTimeʱ����ɱ༭�����ύ��ʱ�䡣����������Ҫ���ж�һ���ٸ�����Ҫȷ��Ҫ��Ҫ�½�һ��ʱ�䡣
			model.setCreateTime(new Date());  //����ÿ�ζ��Ǵ���һ���µ�model�����ڱ༭��¼����ʱ�����createTimeʱ����ɱ༭�����ύ��ʱ��
		}
		employeeService.saveOrUpdate(model);
		return "saveSuccess";
	}
	
	/**
	 * �����������Preparable��������Ҫ�ɵ��£�������ִ��save����֮ǰ��Ҫ�������prepareSave����������ɶ�save������Ԥ����
	 * 
	 */
	public void prepareSave(){
		if( id == null){
			model = new Employee();  //����ÿ�ζ���saveʱ�����½�һ��model����ص����ڱ༭�����ύ�������Щ�ֶε�ֵΪ�ա�Ҫ���������������ַ�ʽ��1. ��ҳ����������ر���2. ��model�����ݿ��в�ѯ�����������½�������
		}
		else{
			model = employeeService.get(id);  //�˴��ǽ��ĳЩ�ֶ�ֵΪ�յĵ�2�ַ�ʽ�������ݿ��л�ȡһ����¼�ٽ��б༭��struts�����������Զ����ݱ����е��ֶε�ֵ������model��Ӧ�ֶε�ֵ��������û�е��ֶ���ʹ�ô����ݿ���ȡ����������ֵ��
		}
	}
	
	/**
	 * Preparable�ӿڵķ�����
	 * @throws Exception
	 */
	@Override
	public void prepare() throws Exception {
	// ����Ŀ�÷���Ϊ�ա�prepare������Ҫ��������;��1. ִ��action��һЩ��ͬ��Ԥ����ҵ��2. ��ʹ��������ʵ�ֲ�ͬ��������ͬһ��SQL����Ч����
	}
	
	/**
	 * ModelDriven�ӿڵķ��������ڻ�ȡ��Ҫ�Ķ���
	 * @return
	 */
	@Override
	public Employee getModel() {
		return model;
	}
	
	private String lastName;  //�������մӿͻ��˴�������lastNameֵ��
	
	public void setLastName(String lastName){  //��������ɿ���ڽ���ҳ�����ʱ���á�
		this.lastName = lastName;
	}
	
	/**
	 * ��֤�����Ƿ��Ѿ����ڵ�action������
	 * ��֤�������ǣ����ô�ҳ���õ�lastNameȥ���ݿ��в�ѯ������ҵ�����������Ѿ����ڡ�
	 * @return
	 */
	public String validateLastName(){
		if(employeeService.lastNameIsValid(lastName)){
			try {
				inputStream = new ByteArrayInputStream("1".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				inputStream = new ByteArrayInputStream("0".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return "lastNameValid";  //������ʵ������delete��action����ֵ��ͬ����Ϊ���Ƕ���Ajax�ķ��ء�������Ϊ�����𣬻���ѡ��һ���ķ���ֵ��
	}
}
