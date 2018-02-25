<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String ico = Constant.WEBICO;
%>
<html lang="en-US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户登录成功</title>
<link rel="stylesheet" type="text/css" href="/schedule/css/main.css">
<link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
<!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>
<body>
<div id="wrapper"><a class="logo" href="/"></a>
  <div id="main">
    <header id="header">
      <h1><span class="icon">√</span>登录成功</h1>
    </header>
    <div id="content">
      <h2> ${user.name}:您好,你还没有激活，请查收邮箱，如果没有收到可以点击激活重新发送，虽然激活之后也什么也没什么用 </h2>
	  <div style="height:50px; line-height:50px;text-align:center;">
		<h2><a href="http://www.menglingqiang.com/schedule/user/activation?flag=false&code=${user.code}">点击激活</a></h2>
	  </div>
    </div>
    
  </div>
</div>
</html>
	