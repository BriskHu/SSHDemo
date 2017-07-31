<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  <!-- 引入标签。 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h4>Employee List Page</h4>
	
	<s:if test="#request.employees == null ||#request.employees.size() == 0">
		没有任何员工信息
	</s:if>
	<s:else>
		<table border="1" cellpadding="10" cellspacing="0">
			<tr>
				<td>Id</td>
				<td>LastName</td>
				<td>Email</td>
				<td>Birth</td>
				<td>CreateTime</td>
				<td>Department</td>
				<td>Delete</td>
				<td>Edit</td>
			</tr>
			<s:iterator value="#request.employees">  <!-- 从Request域中循环获取employees值。 -->
				<tr>
					<td>${id }</td>
					<td>${lastName }</td>
					<td>${email }</td>
					<td>
						<!-- ${birth } -->  <!-- 最初直接使用从数据库中取出的数据显示，时间显示格式为“2001-01-01 00:00:00.0” -->
						<s:date name="birth" format="yyyy-MM-dd"/>  <!-- 利用struts的date标签的format属性对时间格式进行调整。其中的name属性对应实体类的对应字段名，使用“${birth }”方式的birth。 -->
					</td>
					<td>
						<!-- ${createTime } -->
						<s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>${department.departmentName }</td>  <!-- 由于这里需要去查询department对应的表，而在DAO中Spring只在查询employee表时才打开Session（因Spring的声明式事务只截获get*方法，见Spring的applicationContext.xml配置。），所以会报no session异常。 -->
					<td>
						<!-- <a href="emp-delete?id=${id }">Delete</a> -->  <!-- 直接删除，不会有删除前的确认提示。 --> 
						<a href="emp-delete?id=${id }" class="delete">Delete</a>  <!-- 为了后面添加edit操作，这里加上了一个名为delete的id。在emp-add.jsp中演示了另外一种不使用id（那里的方式是通过input的name属性来查找对应的元素）就能给某个元素设置对应的事件处理函数。在此处，通过这个class属性指定了这里的<a>元素的id，然后在script中，为这个元素设置对应的单击事件。 -->
						<input type="hidden" value="${lastName }" /> <!-- 为了在提示框里显示lastName，这里给该列使用了一个隐藏域，该隐藏域的值会随着单击事件一起提交。也可以通过<a>结点（当前结点)的父结点<td>的父结点<tr>的第二个结点<td>的值来获得lastName的值。 -->
					</td>
					<td>
						<a href="emp-addEmp?id=${id }">Edit</a>  <!-- 这里将表单中的id作为参数传递到方法中。 -->
					</td>
				</tr>	
			</s:iterator>
		</table>
	</s:else>
	
	<!-- 为了不影响加载，建议将脚本放置在html文件的尾部。 -->
	<%--导入JQuery文件--%>
<script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		//1. 点击delete时，弹出确定要删除 xx 的信息吗？若确定，则删除；如果取消，则返回
		$(".delete").click(function(){
			var lastName = $(this).next(":input").val();  //通过给delete链接列添加一个隐藏域，来获得表单里的lastName值。这里this指的是<a>结点,因为这整个代码块都是作用于“.delete”，而.delete是位于<a>中的。
			var flag = confirm("确定要删除 "+lastName+" 的信息吗？");
			
			if(flag){
				//使用AJAX方式删除记录
				var url = this.href;  //这里的this仍然是指向删除列的<a>对象的。
				var args = {"delete time":new Date()};
				
				var $tr = $(this).parent().parent();  //把当前行业删除。删除当前行需要获得发生单击事件的所在行的引用，从而在用代码删除该行。
				$.post(url, args, function(data){  //post方法将请求发送给服务器。通过在浏览器上查看请求头（浏览器->工具->开发者工具->Network栏),post发送的具体内容。
					//若data的返回值为1，则提示删除成功，且把当前行删除。
					/*
					这里要获得data的返回值，则涉及到struts2如何使用AJAX的。通过查看struts2的文档可知Struts2有两种方式使用ajax，一种是直接使用jsp的out来输出，
					另一种是类似于文件上传下载方式，利用流获取少量的数据。第二种方式特别适用于传输标志量这样的少量数据。使用AJAX也可以传输json数据。这种方式需要在action类中进行实现。
					*/
					if(data == "1"){  //这个data数据就是Ajax从服务器获得标志量的值，也是服务器对应Action类中的InputStream对象传输的内容。
						alert("删除成功！");
						$tr.remove();
					}
					//若data的返回值不为1，则提示删除失败。
					else{
						alert("删除失败！");
					}
				});
			}
			return false;//通过将单击事件的函数返回false，可以取消超链接的默认行为
		});  //给表单中的delete域添加了一个但是事件。
		
		//2. 
	})
</script>
</body>
</html>