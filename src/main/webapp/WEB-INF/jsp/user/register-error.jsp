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
<title>用户注册失败界面</title>
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
      <h1><span class="icon">!</span>抱歉,<span class="sub">注册失败</span></h1>
    </header>
    <div id="content">
      	<h2>注册失败，请重新注册</h2>
      	<p><a href="<%=basePath %>user/preRegister">注册</a></p>
    </div>
    
  </div>
</div>
</html>


</html>