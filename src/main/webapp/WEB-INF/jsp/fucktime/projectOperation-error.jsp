<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<!DOCTYPE html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ico = Constant.WEBICO;
%>
<html>
<link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
<head>
  <title>项目操作失败页面</title>
  
</head>
<body >
	项目操作失败

</body>


</html>