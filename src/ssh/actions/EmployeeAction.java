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
 * 相应JSP页面动作的JavaBean，该类完成Action的业务逻辑。
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
		request.put("employees", employeeService.getAll());  //此条语句将service取得数据放入到Request域的emloyee中，实现将数据显示在页面上。要将数据放入到Request域中，就需要实现RequestAware接口。
		return "list";
	}

	private Integer id;
	
	public void setId(Integer id){
		this.id = id;
	}
	
	private InputStream inputStream;  //使用Ajax方式所必须的字段。
	
	public InputStream getInputStream(){
		return inputStream;
	}
	
	public String delete(){
		/*
		 * 直接删除方式。
		 */
//		employeeService.delete(id); 
//		return "deleteSuccess";  //采用非AJAX方式删除时，需要刷新页面才能实时显示结果。本处采用在struts.xml中配置对应的result的type为redirect值，进行重定向，实现自动刷新操作。注意这中方式实际上代价是比较高的，因为需要重新请求，并重新查询。

		/*
		 * 使用AJAX方式删除。
		 * 使用AJAX方式删除时，需要在Action类中定义一个InputStream类型的字段，同时需要为给属性实现对应的get方法。然后在此方法（也就要使用AJAX方式的方法）中使用该流对象
		 */
		try {
			employeeService.delete(id);
			inputStream = new ByteArrayInputStream("1".getBytes("UTF-8"));  //向客户端返回值为“1”的标志量，表示删除成功。只需要创建一个输入流，Struts会接管这个输入流，然后创建相应的输出流来输出相应的结果。我们不用关心输出流，struts会帮我们管理好。
		} catch(Exception e) {
			e.printStackTrace();
			try {
				inputStream = new ByteArrayInputStream("0".getBytes("UTF-8"));  //向客户端返回值为“0”的标志量，表示删除失败。
			} catch (UnsupportedEncodingException e1) {  //这个异常表示是否支持UTF-8编码，这个异常基本没什么用，放着就行。
				e1.printStackTrace();
			}
		}
		
		return "deleteAjax";  //采用AJAX后，不要刷新就可以更新结果,所以这里返回什么值并不重要，只需要与struts.xml中对应result标签的name属性保持一致即可。更新操作就是在jsp中对应的js脚本根据AJAX返回的标志量实现动态刷新的。
	}
	
	/**
	 * 这个方法用来处理单击添加链接的动作：先查询表，并将查询结果放到request域中，然后转向到新增页面。
	 * @return
	 */
	public String addEmp(){
		request.put("departments", departmentService.getAll());  //将查询到的department放入到request域中，从而在JSP页面显示出来。
		return "addEmp";
	}

	/**
	 * 这个方法用于编辑操作。
	 * 由于使用了paramsPrepareParamsStack拦截器栈，所以在页面上点击对应记录的编辑超链接后，struts会先调用这个方法进行执行，然后再执行addEmp方法。
	 * 在这个方法中，完成编辑功能中的数据回显操作。
	 */
	public void prepareAddEmp(){
		if(id != null){
			model = employeeService.get(id);  //由于prepareAddEmp方法会先于addEmp方法执行，所以在执行addEmp方法之前时，model对象保存着从数据库中查询到的值。Struts可以直接自动使用这个值显示在页面上。
		}
	}
	 
	private Employee model;  //用于接收表单提交数据。

	public String save(){
		//System.out.println(model);
		if(id == null){  //由于每次都是创建一个新的model，这在编辑记录操作时会出现createTime时间会变成编辑操作提交的时间。所以这里需要先判断一下再根据需要确定要不要新建一个时间。
			model.setCreateTime(new Date());  //由于每次都是创建一个新的model，这在编辑记录操作时会出现createTime时间会变成编辑操作提交的时间
		}
		employeeService.saveOrUpdate(model);
		return "saveSuccess";
	}
	
	/**
	 * 这个方法正是Preparable拦截器所要干的事，就是在执行save方法之前需要调用这个prepareSave方法，来完成对save方法的预处理。
	 * 
	 */
	public void prepareSave(){
		if( id == null){
			model = new Employee();  //这里每次都是save时都是新建一个model，这回导致在编辑操作提交结果后，有些字段的值为空。要解决这个问题有两种方式：1. 在页面中添加隐藏表单域；2. 将model从数据库中查询出来而不是新建出来。
		}
		else{
			model = employeeService.get(id);  //此处是解决某些字段值为空的第2种方式：从数据库中获取一条记录再进行编辑。struts的拦截器会自动根据表单中有的字段的值来设置model对应字段的值，而表单中没有的字段则使用从数据库中取出来的数据值。
		}
	}
	
	/**
	 * Preparable接口的方法。
	 * @throws Exception
	 */
	@Override
	public void prepare() throws Exception {
	// 本项目该方法为空。prepare方法主要有两个用途：1. 执行action中一些相同的预处理业务；2. 不使用隐藏域实现不同参数运行同一个SQL语句的效果。
	}
	
	/**
	 * ModelDriven接口的方法，用于获取需要的对象。
	 * @return
	 */
	@Override
	public Employee getModel() {
		return model;
	}
	
	private String lastName;  //用来接收从客户端传过来的lastName值。
	
	public void setLastName(String lastName){  //这个方法由框架在接收页面参数时调用。
		this.lastName = lastName;
	}
	
	/**
	 * 验证名字是否已经存在的action方法。
	 * 验证的流程是，利用从页面获得的lastName去数据库中查询。如果找到则表名名字已经存在。
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
		
		return "lastNameValid";  //这里其实可以与delete的action返回值相同，因为它们都是Ajax的返回。但这里为了区别，还是选择不一样的返回值。
	}
}
