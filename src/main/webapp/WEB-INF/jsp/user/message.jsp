<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<!DOCTYPE html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ico = Constant.WEBICO;
%>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
	<title>用户信息</title>
	<script src="/schedule/script/jquery-1.8.0.min.js"></script>
	<script type="text/javascript">
		function returnProject()
		{
			var email = document.getElementById("email").value;
			var form = document.getElementById("form1");
			form.action ="<%=basePath%>project/getProjectByEmail?email="+email;
			form.submit();
		}
	</script>
</head>
<body>
	<form id="form1"  method="post">
		<input id="email" name="email" value="${user.email}" type="hidden">
			${user.name}:您好 ${messageList}
		<input  value="返回总任务" type="button" onclick="javascript:returnProject()">
	</form>
</body>
</html>