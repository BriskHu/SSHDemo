<%@ page import="java.util.Date" language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>

	<body>
		<s:debug></s:debug>
	
		<h4>Add New Employee Page</h4>
		
		<s:form action="emp-save" method="post">
			<s:if test="id != null">  <!-- 编辑记录时，id不为空，此时lastName应该不可以修改。 -->
				<s:textfield name="lastName" label="LastName" disabled="true"></s:textfield>  <!-- Struts中实体类的字段名称要与对应jsp中textfield中name属性值相同。disable属性用来指定文本框是否不可修改。 -->
				<s:hidden name="id"></s:hidden>  <!-- 为了能够知道修改的是哪条记录的，所以这里增加了一个id隐藏域。 -->
			<%-- 
				<!-- 通过添加隐藏域的方式把未显式提交的字段值提交到服务器。在不设置这两个隐藏域时，lastName和使用隐藏域的缺点：1. 降低代码可读性；2. 不适用于大量使用隐藏域数据。 -->
				<s:hidden name="lastName"></s:hidden>
				<s:hidden name="createTime"></s:hidden>
			--%>

			</s:if>
			<s:else>
				<s:textfield name="lastName" label="LastName"></s:textfield>  <!-- Struts中实体类的字段名称要与对应jsp中textfield中name属性值相同 -->
			</s:else>
			
			<s:textfield name="email" label="Email"></s:textfield>
			<s:textfield name="birth" label="Birth"></s:textfield>
			
			<s:select list="#request.departments" 
				listKey="id" listValue="departmentName" name="department.id" label="Department" >  <!-- 这里的listKey将生成对应HTML中的value值，listValue将生成为label值。 -->
			</s:select>
			<s:submit></s:submit>
		</s:form> 
	</body>
	
	<!-- 为了不影响页面的加载，建议将script脚本放置在body标签之后。 -->
	<script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
		$(function(){
			$(":input[name=lastName]").change(function(){  //为页面中name为lastName的元素添加一个改变事件。
				var val = $(this).val();  //取出该元素的值。
				val = $.trim(val);  //去取前后空格。
				var $this = $(this);  //新建了一个对当前对象的引用。
				
				if(val != ""){
					$this.nextAll("font").remove();  //将当前结点后面的所有font兄弟结点删除。
					/*
					发送url请求。
					*/
					var url = "emp-validateLastName";  //转到服务器对应的action中去。
					var args = {"lastName":val, "time":new Date()};
					$.post(url, args, function(data){  //此时的“$”代表JQuery对象。
						if(data == "1"){   //表示名字可用
							$this.after("<font color='green'>LastName可用！</font>");  //这里表示在lastName元素的后面显示一个绿色的字符串。
						} 
						else if(data == "0"){  //表示名字不可用
							$this.after("<font color='red'>LastName不可用！</font>");
						}
						else{  //服务器错误。
							alert("服务器错误！");
						}
					});
				}
				else{
					alert("LastName不能为空");
					this.focus();
				}
			});
		})
		
	</script>
</html>