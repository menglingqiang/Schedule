<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<!DOCTYPE html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ico = Constant.WEBICO;
%>
<html xmlns:wb="http://open.weibo.com/wb">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js" type="text/javascript" charset="utf-8"></script>
	<title>用户信息</title>
	cold
	<wb:share-button appkey="1179732476" addition="number" type="button"></wb:share-button>
</body>
</html>


