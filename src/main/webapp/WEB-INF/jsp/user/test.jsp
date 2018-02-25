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
	<title>用户信息</title>
	<link rel="stylesheet" type="text/css" href="/schedule/css/normalize.css" />
	<link rel="stylesheet" type="text/css" href="/schedule/css/default.css">
	<link href="/schedule/css/material-design-iconic-font.min.css" rel="stylesheet">
	<link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="/schedule/css/styles.css">
	<script src="/schedule/script/modernizr.js" type="text/javascript"></script>
	<script type="text/javascript">
   		
		<% 
			String userName = "";
			String password = "";
			boolean flag = false;
			Cookie[] cookies = request.getCookies();
			if(cookies!=null||cookies.length>0)
			{
				for(int i=0;i<cookies.length;i++)
				{
					if(cookies[i].getName().equals("userName"))
						userName =cookies[i].getValue();
					if(cookies[i].getName().equals("password"))
					{
						password = cookies[i].getValue();
						flag=true;
					}
				}
			}
			if(flag)	
				request.getRequestDispatcher("testtwo.jsp").forward(request,response);
		%>
   
	</script>
</head>
<body>
<% %>
	<form id="form1" action="<%=basePath %>project/testTwo" method="get">
	    <table>
	    	<tr>
	    		<input type="text" id="userName" name="userName" value="<%= userName%>" /><br>
	    	</tr>
	    	<tr>
	    		<input id="password" name="password" type="password" value="<%= password%>" /><br>
	    	</tr>
	    	<tr>
	    		自动登录10天<input type="checkbox" id="autoLogin" name="autoLogin" checked/>
	    	</tr>
	    	<tr>
	    		<input type="submit" value="提交" />
	    	</tr>
	    </table>
	</form>
    
</body>
</html>


