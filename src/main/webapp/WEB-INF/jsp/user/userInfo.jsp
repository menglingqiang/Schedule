<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.menglingqiang.schedule.util.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	function uploadUserPic(obj)
	{
		var obj = document.getElementById("file1");
		var objValue = document.getElementById("file1").value;
		if(objValue=="")
		{
			alert("请先选择上传文件");
			return ;
		}
		var email = document.getElementById("email").value;
		if(PreviewImage(obj,'imgView','pic_preview'))
		{
			var form = document.getElementById("form1");
			form.action="<%=basePath %>user/changeUserPic?email="+email;
			form.submit();
		}else
		{
			alert("请检查一下文件的后缀名是否是jpg等图片格式");
		}
	}
	function changeEmail(email)
	{
		var newEmail = document.getElementById("newEmail").value;
		if(!isEmail(email)||!isEmail(newEmail))
		{
			alert("邮箱格式不正确，请查看后重新提交");
			return ;
		}
		if(isBindEmail(newEmail)=="true")
		{
			alert("此邮箱已经被绑定，请更换一个");
			return ;
		}
		else
		{
			var form = document.getElementById("form1");
			form.action="<%=basePath %>user/changeEmail?email="+email+"&newEmail="+newEmail;
			form.submit();
		}
	}
	function isEmail(str){
        var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        return reg.test(str);
    }
    function PreviewImage(obj, imgPreviewId, divPreviewId) {  
    	var extend=obj.value.substring(obj.value.lastIndexOf(".") + 1);
		var msg = document.getElementById("message");
		msg.innerHTML = '<font size="4px"  color="red" ></font>';//先清空一下信息
		if (extend != "" && !(extend=="jpg")) {
			msg.innerHTML = '<font size="4px"  color="red" >请上传后缀名为jpg的文件</font>';
			return false;
		}
        var browserVersion = window.navigator.userAgent.toUpperCase();  
        if (browserVersion.indexOf("MSIE") > -1) {  
            if (browserVersion.indexOf("MSIE 6.0") > -1) {//ie6  
                document.getElementById(imgPreviewId).setAttribute("src", obj.value);  
                return true;
            } else {//ie[7-8]、ie9  
                obj.select();  
                var newPreview = document.getElementById(divPreviewId + "New");  
                if (newPreview == null) {  
                    newPreview = document.createElement("div");  
                    newPreview.setAttribute("id", divPreviewId + "New");  
                    newPreview.style.width = 160;  
                    newPreview.style.height = 170;  
                    newPreview.style.border = "solid 1px #d2e2e2";  
                }  
                newPreview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + document.selection.createRange().text + "')";  
                var tempDivPreview = document.getElementById(divPreviewId);  
                tempDivPreview.parentNode.insertBefore(newPreview, tempDivPreview);  
                tempDivPreview.style.display = "none"; 
                return true;
            }  
        } else if (browserVersion.indexOf("FIREFOX") > -1) {//firefox  
            var firefoxVersion = parseFloat(browserVersion.toLowerCase().match(/firefox\/([\d.]+)/)[1]);  
            if (firefoxVersion < 7) {//firefox7以下版本  
                document.getElementById(imgPreviewId).setAttribute("src", obj.files[0].getAsDataURL());  
            } else {//firefox7.0+                      
                document.getElementById(imgPreviewId).setAttribute("src", window.URL.createObjectURL(obj.files[0]));  
            }
            return true;
        } else if (obj.files) {  
            //兼容chrome、火狐等，HTML5获取路径                     
            if (typeof FileReader !== "undefined") {  
                var reader = new FileReader();  
                reader.onload = function (e) {  
                    document.getElementById(imgPreviewId).setAttribute("src", e.target.result);  
                }  
                reader.readAsDataURL(obj.files[0]); 
                return true;
            } else if (browserVersion.indexOf("SAFARI") > -1) {  
                alert("暂时不支持Safari浏览器!");  
                return false;
            }  
        } else {  
            document.getElementById(divPreviewId).setAttribute("src", obj.value);  
            return true;
        }
        
    }
	function isBindEmail(email)
  	{
  		var result;
  		$.ajax({
            type: "Get",
            url: "<%=basePath%>user/isBindEmail?email="+email,
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            async: false,
            success: function (data) {
            	result= data;
            }
        });  
  		return result;
  	}
    function switchAlert(alertStatus,email)
    {
        var result;
        $.ajax({
            type: "Post",
            url: "<%=basePath%>user/switchAlert?alertStatus="+alertStatus+"&email="+email,
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            async: false,
            success: function (data) {
                result= data;
            }
        });
        return result;
    }
	</script>
</head>
<body>
	<form id="form1" action="" enctype="multipart/form-data" method="post">
	<input type="hidden" value="${user.email}" id="email" name="email"/>
	            <img src="/schedule/userimg/${user.userPic}" id="imgView" onclick="javascript:changeUserPic(${user.email})">
	    <ul>
	        <li>
	            <span>
	                	昵称 :${user.name}
	           </span>
	        </li>
	        <li>
	            <span>
	               	邮箱 :${user.email}
	               <input type="newEmail" id="newEmail" name="newEmail" placeholder="新绑定邮箱"/>
	               <input type="button" value="提交" onclick="javascript:changeEmail('${user.email}')"></input>
	            </span>
	        </li>
	        <li>
	            <span>
	                	分任务量 :${sum}
	            </span>
	        </li>
	        <li>
	            <span>
	                	已完成任务量 :${done}
	            </span>
	        </li>
           <c:if test="${msg!=null}">
			   <li>
	            <span>
	                	${msg}
	            </span>
			   </li>
		   </c:if>
	       <li>
	            <span>
					<c:choose>
						<c:when test="${user.alertStatus}">
							<a href="javascript:switchAlert(false,'${user.email}');">
								邮箱提醒已经开启,点击关闭
							</a>
						</c:when>
						<c:otherwise>
							<a href="javascript:switchAlert(true,'${user.email}');">
								邮箱提醒已经关闭,点击开启
							</a>
						</c:otherwise>
					</c:choose>
	            </span>
	        </li>
	        <li>
	             <a href="javascript:returnProject()">
	                	返回总任务界面
	            </a>
	           
	        </li>
	    </ul>
	
	   <div>  
	       <label >配图：</label>  
	       <img id="imgView" src=""  alt="" />  
	       <input type="button" value="上传" onclick="javascript:uploadUserPic(this)"/>
	       <input type="file" name="file1" id="file1" class="file"  value='浏览...' onchange="PreviewImage(this,'imgView','pic_preview')" />  
	       <p id="message" name="message" ></p> 
    	</div>  
	</form>
</body>
</html>
