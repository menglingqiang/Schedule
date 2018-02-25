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
  <title>用户忘记密码输入页面</title>
   <link href="/schedule/css/login.css" rel="stylesheet" type="text/css" />
  <script src="/schedule/script/jquery-1.8.0.min.js"></script>
  <link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
  <script type="text/javascript">
  	
  	function forgetPassword()
  	{
  		var email = document.getElementById("email").value;//得到输入的email
  		var msg = document.getElementById("msg");
  		var password = document.getElementById("password").value;//得到输入的密码
		var rePassword = document.getElementById("rePassword").value;
		var form =document.getElementById("form1");
		if(!isEmail(email))
		{
			msg.innerHTML = '<font size="4px"  color="red" >邮箱格式不正确，请重新输入</font>';
			return ;
		}
		if(password!= rePassword)
		{
  			msg.innerHTML = '<font size="4px"  color="red" >两次输入的密码不一致，请重新输入</font>';
			return ;
		}
  		$.ajax({
            type: "Get",
            url: "<%=basePath%>user/isRegister?email="+email+"&password="+password,
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            success: function (data) {
            	if(data=="true")
           		{
		          	form.submit();  //注册了发送信息
           		}
            	else{
		  			msg.innerHTML = '<font size="4px"  color="red" >您还没有注册，请检查输入的邮箱，或者去注册！</font>'; //没注册提示注册，或者检查email文件 */
            	}
            }
        }); 
  	}
  	function isEmail(str){
        var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        return reg.test(str);
    }
  	</script>
</head>
<!-- 提交之后先调用js，判断是否已经注册，没有注册提示注册并弹出注册信息界面，注册了之后提交表单信息发送信息跳转页面 -->
<body class="login_bj" >
<div class="zhuce_body">
    <div class="zhuce_kong">
    	<div class="zc">
        	<div class="bj_bai">
            <h3>忘记密码</h3>
     <form id="form1" action="<%=basePath %>user/forgetPassword" method="post">
		<table>
		
			<tr>
				<td><input id="email" name="email" type="email" class="kuang_txt email" placeholder="请输入注册邮箱"/></td>
			</tr>
			
			<tr>
				<td><input id="password" name="password" type="password" placeholder="请输入新密码" class="kuang_txt possword" autocomplete="off"/></td>
			</tr>
			
			<tr>
				<td><input id="rePassword" name="rePassword" type="password" placeholder="重新输入密码" class="kuang_txt possword" autocomplete="off"/></td>
			</tr>
			
			<tr>
				<td><input value="提交" type="button" onclick="javascript:forgetPassword()" class="btn_zhuce" /></td>
			</tr>
							
			<tr>
				<td><a href="<%=basePath %>user/preLogin"/>想起来密码，去登陆?</br></td>
			</tr>
                
			<tr>
				<td><p id="msg" name="msg"/></td>
			</tr>
		</table>
	</form>
            </div>
        	<div class="bj_right">
            	<p>使用以下账号直接登录</p>
                <a href="#" class="zhuce_qq">QQ注册</a>
                <a href="#" class="zhuce_wb">微博注册</a>
                <a href="#" class="zhuce_wx">微信注册</a>
                <p>已有账号？<a href="<%=basePath %>user/preRegister">立即注册</a></p>
            
            </div>
        </div>
    </div>

</div>

	<%-- <form id="form1" action="<%=basePath %>user/forgetPassword" method="post">
		请输入注册邮箱<input id="email" name="email" type="email" class="kuang_txt email"/></br>
		<input value="提交" type="button" onclick="isRegister()"/></br>
		<a href="<%=basePath %>user/preLogin"/>想起来密码，去登陆?</br>
		<a id="msg" name="msg"/>
	</form> --%>

</body>


</html>