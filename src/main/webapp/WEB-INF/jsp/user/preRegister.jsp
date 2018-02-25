<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<!DOCTYPE html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ico = Constant.WEBICO;
%>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>注册界面</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1,minimum-scale=1.0,maximum-scale=1.25,user-scala">
    <link href="/schedule/css/login.css" rel="stylesheet" type="text/css" />
    <link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
    <script src="/schedule/script/jquery-1.8.0.min.js"></script>
    <script type="text/javascript">
   
  	function reloadImageCode()
  	{
  		var date = new Date();//不传入一个date的话浏览器认为这个请求是没有必要的，不会刷新页面
  		document.getElementById("imageCode").src="<%=basePath %>/user/validate?"+date;
  	}
  	
  	function checkPicCode()
  	{
  		var inputCode = document.getElementById("inputCode").value.toUpperCase();//变成大写的，不区分大小写，并且后台写入的值是大写的
  		//inputCode 都变成大写的
  		var checkCodeFlag = document.getElementById("checkCodeFlag");
  		$.ajax({
            type: "Get",
            url: "<%=basePath%>user/getPicCode",
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            success: function (data) {
                if(inputCode==data)
               	{
               		checkCodeFlag.value="true";
               		document.getElementById("checkImage").src="/schedule/img/right.jpg";
               	}
                //退回的时候按键的状态是可以点击的，相当于绕过了前台的验证
                else
               	{
                	checkCodeFlag.value="false";
               		document.getElementById("checkImage").src="/schedule/img/wrong.jpg";
               	}
            }
        }); 
  		//checkInputCode();
  	}
  	function checkInputCode()
  	{
  		var inputCode = document.getElementById("inputCode");
  		
  		if(inputCode.value.length>=4)//TODO bug
		{
  			inputCode.value=inputCode.value.substr(inputCode.value.length-4,inputCode.value.length);//输入超过4位，后面的数字自动顶上前面的数字
  			checkPicCode();
		}
  	}
  	window.onload = function(){
  		reloadImageCode();
  		checkInputCode();
  	}
  	function preRegister()
  	{
  		var password = document.getElementById("password").value;//得到输入的密码
  		var rePassword = document.getElementById("rePassword").value;//得到确认密码
  		var email = document.getElementById("email").value;//得到输入的email
  		var msg =document.getElementById("message");
  		if(!isEmail(email))
		{
  			msg.innerHTML = '<font size="4px"  color="red" >email格式不正确，请重新输入</font>';
			return ;
		}
  		if(!isPassword(password))
		{
  			msg.innerHTML = '<font size="4px"  color="red" >密码格式不正确，请输入数字和字母组合且不少于8位</font>';
			return ;
		}
  		if(password!= rePassword)
		{
  			msg.innerHTML = '<font size="4px"  color="red" >两次输入的密码不一致，请重新输入</font>';
			return ;
		}
  		var form =document.getElementById("form1");
  		var checkCodeFlag = document.getElementById("checkCodeFlag").value;
  		$.ajax({
            type: "Get",
            url: "<%=basePath%>user/registerFlag?email="+email,
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            success: function (data) {
            	if(data=="1")
           		{
            		msg.innerHTML = '<font size="4px"  color="red" >该账号已经注册！</font>';
           		}
            	else if(data=="0")
            	{
            		if(checkCodeFlag=="true")
            			form.submit();
            		else
            			msg.innerHTML = '<font size="4px"  color="red" >验证码输入错误！</font>';
            	}
            }
        });  
  		
  	}
  	
  	 function isEmail(str){
         var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
         return reg.test(str);
     }
  	function isPassword(str){
       /*  var reg =  /^[a-zA-Z]{8,16}$/;
        return reg.test(str); */
        return true;
    }
  </script>
</head>
<body class="login_bj" >

<div class="zhuce_body">
    <div class="zhuce_kong">
    	<div class="zc">
        	<div class="bj_bai">
            <h3>欢迎注册</h3>
            <input id="checkCodeFlag" name="checkCodeFlag" value="false" type="hidden"/>
       	  	  <form id="form1" action="<%=basePath %>user/register" method="post">
               
                <table>
		
					<tr>
						<td> <input id="name" name="name" type="" class="kuang_txt phone" placeholder="用户名"></td>
					</tr>
					
					<tr>
						<td><input id="email" name="email" type="email" class="kuang_txt email" placeholder="邮箱"></td>
					</tr>
					<tr>
						<td><input id="password" name="password" type="password" class="kuang_txt possword" min="8" placeholder="字母和数字的组合且不少于8位" autocomplete="off"/></td>
					</tr>
					<tr>
						<td><input id="rePassword" name="rePassword" type="password" class="kuang_txt possword" placeholder="确认密码" autocomplete="off"/></td>
					</tr>				
					<tr>
						<!-- input只可以输入4位 -->
						<td><input name="inputCode" id="inputCode" type="text"  oninput="javascript:checkInputCode()" class="kuang_txt yanzm" placeholder="验证码" autocomplete="off"></td>
					    <td><img id="checkImage" src="/schedule/img/wen.jpg" style="width:20px;height:20px;" /></td>
					</tr>
                
					<tr>
						<td><img id="imageCode" src="" width="100" height="50"></td>
						<td><img onclick="javascript:reloadImageCode()" src="/schedule/img/zc_25.jpg" width="13" height="14" title='点击刷新验证码'></td>
					</tr>
					<tr>
						<td>
							<input type="button" value="注册" class="btn_zhuce" onclick="javascript:preRegister()"/>
							<p id="message" name="message" ></p>
						</td>
					</tr>
				</table>
                </form>
            </div>
        	<div class="bj_right">
            	<p>使用以下账号直接登录</p>
                <a href="#" class="zhuce_qq">QQ登陆</a>
                <a href="<%=basePath %>user/loginByWeiBo" class="zhuce_wb">微博登陆</a>
                <a href="#" class="zhuce_wx">微信登陆</a>
                <p>已有账号？<a href="<%=basePath %>user/preLogin">立即登录</a></p>
            
            </div>
        </div>
    </div>

</div>

</body>
</html>