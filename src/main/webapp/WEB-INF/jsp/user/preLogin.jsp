<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<!DOCTYPE html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ico = Constant.WEBICO;

String appIdLocal= Constant.APPIDLOCAL;
String reUrlLocal =Constant.REURLLOCAL;

String appIdNet= Constant.APPIDNET;
String reUrlNet =Constant.REURLNET;
%>
<html>
<head>
	<meta charset="UTF-8",minimum-scale=0.5,maximum-scale=1.0,user-scala">
	<title>登录界面</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <link href="/schedule/css/login.css" rel="stylesheet" type="text/css" />
    <link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
    <script src="/schedule/script/jquery-1.8.0.min.js"></script>
    <script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
    function initLink()
    {
	    host = window.location.host;
	    var weiboNet = document.getElementById("weiboNet");
	    var weiboLocal = document.getElementById("weiboLocal");
	    if(host.indexOf("www")==1)
	    	weiboLocal.style.display="none";
	    else
	    	weiboNet.style.display="none";
	    
    }
  	function reloadImageCode()
  	{
  		var date = new Date();//不传入一个date的话浏览器认为这个请求是没有必要的，不会刷新页面
  		document.getElementById("imageCode").src="<%=basePath %>user/validate?"+date;
  	}
  	
  	function checkPicCode()
  	{
  		var inputCode = document.getElementById("inputCode").value.toUpperCase();//变成大写的，不区分大小写，并且后台写入的值是大写的
  		var checkCodeFlag = document.getElementById("checkCodeFlag");
  		//inputCode 都变成大写的
  		$.ajax({
            type: "Get",
            url: "<%=basePath%>user/getPicCode",
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            success: function (data) {
                if(inputCode==data)
               	{
               		document.getElementById("checkImage").src="/schedule/img/right.jpg";
               		checkCodeFlag.value="true";
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
  		if(inputCode.value.length>=4)
		{
  			inputCode.value=inputCode.value.substr(inputCode.value.length-4,inputCode.value.length);//输入超过4位，后面的数字自动顶上前面的数字
  			checkPicCode();
		}
  	}
  	function preLoginCheck()
  	{
  		
  		var email = document.getElementById("email").value;//得到输入的email
  		var password = document.getElementById("password").value;//得到输入的密码
  		var form =document.getElementById("form1");
  		var msg =document.getElementById("message");
  		var checkCodeFlag = document.getElementById("checkCodeFlag").value;
  		if(!isEmail(email))
		{
  			msg.innerHTML = '<font size="4px"  color="red" >email格式不正确，请重新输入</font>';
			return ;
		}
  		$.ajax({
            type: "Get",
            url: "<%=basePath%>user/loginFlag?email="+email+"&password="+password,
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            success: function (data) {
            	if(data=="2")
           		{
            		msg.innerHTML = '<font size="4px"  color="red" >您还没有注册，请检查输入的邮箱，或者去注册！</font>';
           		}
            	else if(data=="3")
            	{
		  			msg.innerHTML = '<font size="4px"  color="red" >密码不正确请重新输入</font>'; 
            	}
            	else if(data=="1")
            	{
            		if(checkCodeFlag=="true")
            			form.submit();
            		else
            			msg.innerHTML = '<font size="4px"  color="red" >验证码输入错误！</font>';
            	}
            	else
            	{
            		msg.innerHTML = '<font size="4px"  color="red" >不可预知的错误！</font>';
            	}
            		
            }
        });  
  	}
  	function isEmail(str){
        var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        return reg.test(str);
    }
	
	<% 
		String email = "";
		String password = "";
		Cookie[] cookies = request.getCookies();
		if(cookies!=null&&cookies.length>0)
		{
			for(int i=0;i<cookies.length;i++)
			{
				if(cookies[i].getName().equals("email"))
					email =cookies[i].getValue();
				if(cookies[i].getName().equals("password"))
				{
					password = cookies[i].getValue();
				}
			}
		}
		/* if(flag)	//进入总任务页面
			request.getRequestDispatcher("testtwo.jsp").forward(request,response); */
	%> 
	//如果password有值，那么默认是可以自动进入的
	window.onload=function(){
		var email = document.getElementById("email").value;//得到输入的email
  		var password = document.getElementById("password").value;//得到输入的密码
  		var form =document.getElementById("form1");
  		var msg =document.getElementById("message");
		if(password!=null&&password!="")
		{
			$.ajax({
	            type: "Get",
	            url: "<%=basePath%>user/loginFlag?email="+email+"&password="+password,
	            contentType: "application/json; charset=utf-8",
	            dataType: "text",
	            success: function (data) {
	            	if(data=="2")
	           		{
	            		msg.innerHTML = '<font size="4px"  color="red" >您还没有注册，请检查输入的邮箱，或者去注册！</font>';
	           		}
	            	else if(data=="3")
	            	{
			  			msg.innerHTML = '<font size="4px"  color="red" >密码不正确请重新输入</font>'; 
	            	}
	            	else if(data=="1")
	            	{
	            		form.submit();
	            	}
	            	else
	            	{
	            		msg.innerHTML = '<font size="4px"  color="red" >不可预知的错误！</font>';
	            	}
	            		
	            }
	        });  
		}
		reloadImageCode();
		initLink();
	}
  </script>
  <script src="http://tjs.sjs.sinajs.cn/t35/apps/opent/js/frames/client.js" language="JavaScript"></script>
</head>
<body class="login_bj" >
<div class="zhuce_body">
    <div class="zhuce_kong">
    	<div class="zc">
        	<div class="bj_bai">
            <h3>登录</h3>
            <input id="checkCodeFlag" name="checkCodeFlag" value="false" type="hidden"/>
     <form id="form1" action="<%=basePath %>user/login" method="post">
		<table>
		
			<tr>
				<td><input type="email" id="email" name="email" class="kuang_txt email" placeholder="邮 箱" value="<%=email%>"/></td>
			</tr>
			
			<tr>
				<td><input type="password" id="password" name="password" class="kuang_txt possword" placeholder="密码" value="<%=password%>"autocomplete="off"></td>
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
				
				<td><input id="login" name="login" type="button" value="登录" class="btn_zhuce" onclick="javascript:preLoginCheck()"/></td>
				
			</tr>
			<tr>
				
				<td><input type="checkbox" id="autoLogin" name="autoLogin" checked/>自动登录10天</td>
				
			</tr>
			<tr>
				<td>
					<a href="<%=basePath %>user/preForgetPassword">忘记密码</a></br>
					 
					<p id="message" name="message" ></p>
				</td>
			</tr>
		</table>
	</form>
			  
       	  	  
            </div>
        	<div class="bj_right">
            	<p>使用以下账号直接登录</p>
                <a href="#" class="zhuce_qq">QQ登陆</a>
			    <a id="weiboNet" href="https://api.weibo.com/oauth2/authorize?client_id=<%=appIdLocal%>&response_type=code&redirect_uri=<%=reUrlLocal%>" class="zhuce_wb">微博登录</a>
              	<a id="weiboLocal" href="https://api.weibo.com/oauth2/authorize?client_id=<%=appIdNet%>&response_type=code&redirect_uri=<%=reUrlNet%>" class="zhuce_wb">微博本地</a>
               <%-- <a href="<%=basePath %>user/loginByWeiBo" class="zhuce_wb">微博登陆</a>  --%>
                <!-- <wb:login-button type="3,2" ></wb:login-button> -->
                <a href="#" class="zhuce_wx">微信登陆</a>
                <p>已有账号？<a href="<%=basePath %>user/preRegister">立即注册</a></p>
            	<p><a href="http://www.miibeian.gov.cn/" target="_blank">京ICP备18007444号</a></p>
            </div>
        </div>
    </div>

</div>
    
</body>
</html>