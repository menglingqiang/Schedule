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
<html>
<head>
  <title>用户总任务列表页面</title>
  <link href="/schedule/css/all.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" type="text/css" href="/schedule/css/datedropper.css">
  <link rel="stylesheet" type="text/css" href="/schedule/css/timedropper.min.css">
  <link rel="icon" href="/schedule/img/panda.ico" type="image/x-icon" />
  <link rel="icon" href="/schedule/img/<%=ico%>/" type="image/x-icon" />
  <script src="/schedule/script/jquery-1.8.0.min.js"></script>
  <script src="/schedule/script/datedropper.min.js"></script>
  <script src="/schedule/script/timedropper.min.js"></script>
  <style>
.black_overlay{
	display: none;
	position: absolute;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index:1001;
	-moz-opacity: 0.8;
	opacity:.80;
	filter: alpha(opacity=80);
}
.white_content {
	display: none;
	position: absolute;
	top: 10%;
	left: 10%;
	width: 80%;
	height: 80%;
	border: 16px solid lightblue;
	background-color: white;
	z-index:1002;
	overflow: auto;
}
.white_content_small {
	display: none;
	position: absolute;
	top: 20%;
	left: 30%;
	width: 40%;
	height: 50%;
	border: 16px solid lightblue;
	background-color: white;
	z-index:1002;
	overflow: auto;
}
</style>
<script type="text/javascript">
//弹出隐藏层
function ShowDiv(show_div,bg_div,para,type,secPara){
 if(type=="modify")
 	{
  document.getElementById("modifyDetailProjectId").value= para;
  document.getElementById("beforeProjectName").value=secPara;
}
 else
 	document.getElementById("projectId").value= para;
 document.getElementById(show_div).style.display='block';
 document.getElementById(bg_div).style.display='block' ;
 var bgdiv = document.getElementById(bg_div);
 bgdiv.style.width = document.body.scrollWidth;
 // bgdiv.style.height = $(document).height();
 $("#"+bg_div).height($(document).height());
};
//关闭弹出层
function CloseDiv(show_div,bg_div)
{
 document.getElementById(show_div).style.display='none';
 document.getElementById(bg_div).style.display='none';
};
//检查时间
function checkInputCode(id)
{
 var time = document.getElementById(id).value;
 if(time.length==4||time.length==7)
  document.getElementById(id).value=document.getElementById(id).value+"-";
}
function checkDate(type)
{
 if(type=="modify")
 {
	if(!checkAddOrModifyDate("modify"))
		return ;
 	var startTime = document.getElementById("modifyStartTime").value;
 	var endTime = document.getElementById("modifyEndTime").value;
 	var projecctName = document.getElementById("modifyProjectName").value;
 	if(projecctName=="")
 	{	
 		alert("请输入新添加的项目名称");
 		return ;
 	}
 	return checkTime(startTime,endTime);
 }
 else if(type=="add")
 {
	if(!checkAddOrModifyDate("add"))
		return ;
 	var startTime = document.getElementById("addStartTime").value;
 	var endTime = document.getElementById("addEndTime").value;
 	var projecctId = document.getElementById("addProjectName").value;
 	if(projecctId=="")
 	{	
 		alert("请输入修改后的项目名称");
 		return ;
 	}
 	return checkTime(startTime,endTime);
 }
 else {
	 alert("error");
	 return false;
 }
}
function checkTime(startTime,endTime)//2014-09-09
{
	 //获取当前时间
	var nowStr = getNowFormatDate().substr(0,10);
	var now = new Date(nowStr);
	var start = new Date(startTime); 
	var end = new Date(endTime);
	if(startTime==""||endTime=="")
	{
		alert("你从哪里要要到哪里去，开始时间和结束时间都不为空");
		return false;
	}
	if(start > end) 
	{ 
		alert("时光不可以倒流，开始时间不可以大于结束时间");
		return false; 
	}
	if(now > end) 
	{ 
		alert("这是一个网站，不是时光机，结束时间不可以小于当前的时间");
		return false; 
	}
	return true;
	
}
//获取当前的日期时间 格式“yyyy-MM-dd HH:MM:SS”
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
} 
function addProject()
{
 if(!checkDate("add"))
	 return ;
 var name = document.getElementById("name").value;//总任务名称

 var projectId = document.getElementById("projectId").value;
 var projectName = document.getElementById("addProjectName").value;
 var startTime = document.getElementById("addStartTime").value;
 var endTime = document.getElementById("addEndTime").value;
 var form = document.getElementById("mainForm");
 form.action = "<%=basePath%>project/detailOperation?type=add&projectDetailName="+projectName+"&detailStartTime="+startTime+"&detailEndTime="+endTime+"&projectId="+projectId+"&name="+name;
 form.submit();
}
function modifyProject()
{
 if(!checkDate("modify"))
	 return ;	
 var name = document.getElementById("name").value;//总任务名称
 var detailProjectId = document.getElementById("modifyDetailProjectId").value;
 var projectId = document.getElementById("projectId").value;
 var projectName = document.getElementById("modifyProjectName").value;
 var startTime = document.getElementById("modifyStartTime").value;
 var endTime = document.getElementById("modifyEndTime").value;
 //alert(name+":"+projectId+":"+projectName+":"+startTime+":"+endTime);
 var form = document.getElementById("mainForm");
 form.action = "<%=basePath%>project/detailOperation?type=modify&projectDetailId="+detailProjectId+"&projectDetailName="+projectName+"&detailStartTime="+startTime+"&detailEndTime="+endTime+"&name"+name+"&projectId="+projectId;
 form.submit();
}
function deleteProject(projectDetailId)
{
 var name = document.getElementById("name").value;//总任务名称
 var projectId = document.getElementById("projectId").value;
 //alert(projectId);
 var con=confirm("确定删除?"); //在页面上弹出对话框
 if(con==true)
 {
  var form = document.getElementById("mainForm");
  form.action = "<%=basePath%>project/detailOperation?type=delete&projectDetailId="+projectDetailId+"&name"+name+"&projectId="+projectId;
  form.submit();
 }

}
function back()
{
	 var form = document.getElementById("mainForm");
	 var projectId = document.getElementById("projectId").value;
	 form.action = "<%=basePath%>project/getProjectByEmail?projectId="+projectId;
	 form.submit();
}
function done(detailProjectId)
{
	var con=confirm("确定完成了，没骗我?"); //在页面上弹出对话框
	 if(con==true)
	 {
		 var projectId = document.getElementById("projectId").value;
		 var name = document.getElementById("name").value;//总任务名称
		 var form = document.getElementById("mainForm");
		 form.action = "<%=basePath%>project/doneDetailProject?detailProjectId="+detailProjectId+"&projectId="+projectId+"&name"+name;
		 form.submit();
	 }
		
}
function changeDateFormat()
{
	var content=document.getElementsByTagName("p");[0].innerHTML;
	for(var i=1;i<content.length;i++)
	{
		var d = new Date(content[i].innerHTML);
		var sd = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
		content[i].innerHTML = sd;
	}
}
function getProjectNameAndTime()
{
	var projectId = document.getElementById("projectId").value;
	var content=document.getElementById("projectName");
	var modifyContent=document.getElementById("projectName1");
	var addContent=document.getElementById("projectName2");
	var projectStartTime = document.getElementById("projectStartTime");
	var projectEndTime = document.getElementById("projectEndTime");
	//先计算比例值
	$.ajax({
        type: "GET",
        url: "<%=basePath%>project/queryPorjectNameAndTime?projectId="+projectId,
        cache: false,
        async:false,
        contentType: "application/json; charset=utf-8",
        dataType: "text",
        success: function (data) {
        	var dateList = data.split(",");//将得到的字符串经过，分割分别得到项目名称，开始时间和结束时间
        	content.innerHTML =dateList[0];
        	addContent.innerHTML = dateList[0];
        	modifyContent.innerHTML =dateList[0];
        	
        	projectStartTime.value = dateList[1];
        	projectEndTime.value=dateList[2];
        }
	});
   
}
function checkAddOrModifyDate(operation)
{
	var projectStartTime = document.getElementById("projectStartTime").value;
	var projectEndTime = document.getElementById("projectEndTime").value;
	if(operation=="add")
	{
		var addStartTime = document.getElementById("addStartTime").value; 
		var addEndTime = document.getElementById("addEndTime").value;
		if(compateDate(addStartTime,projectStartTime) && compateDate(projectEndTime,addEndTime))
			return true;
		else
		{
			alert("分项目的开始时间和结束时间等应该总项目的时间之内\n总项目开始时间："+projectStartTime+"   总项目结束时间："+projectStartTime);
			return false;
		}
	}else if(operation=="modify")
	{
		var modifyStartTime = document.getElementById("modifyStartTime").value; 
		var modifyEndTime = document.getElementById("modifyEndTime").value; 
		if(compateDate(modifyStartTime,projectStartTime) && compateDate(projectEndTime,modifyEndTime))
			return true;
		else
		{
			alert("分项目的开始时间和结束时间等应该总项目的时间之内\n总项目开始时间："+projectStartTime+"   总项目结束时间："+projectEndTime);
			return false;
		}
		
	}
}
function compateDate(dayOne,dayTwo)//dayOne如果大于等于dayTwo返回true否则返回false
{
	var one = new Date($.trim(dayOne));
	var two = new Date($.trim(dayTwo));
	if(one >= two)
		return true;
	else
		return false;
}
window.onload=function(){
	
	getProjectNameAndTime();
} 
</script>
</head>
<body >
	<h1><span id ="projectName"></span>:分任务展示界面</h1>
	
	<body style="background: #e1e9eb;">
		<form action="<%=basePath%>project/queryDetailProjectByNameOrTime" id="mainForm" method="post">
			<input id="name" name="name" value='${projectName}' type="hidden"/>
			<input id="projectId" name="projectId" value='${projectId}' type="hidden"/> 
			<input id="projectStartTime" name="projectStartTime" value='${projectStartTime}' type="hidden"/> 
			<input id="projectEndTime" name="projectEndTime" value='${projectEndTime}' type="hidden"/> 
			<div class="right">
				<!-- <div class="current">当前位置：<a href="javascript:void(0)" style="color:#6E6E6E;">内容管理</a> &gt; 内容列表</div> -->
				<div class="rightCont">
					<p class="g_title fix">分任务内容列表 
					<a class="btn03" href="<%=basePath%>user/logout">注销</a>
					<a class="btn03" href="javascript:ShowDiv('MyAddDiv','fade','${projectId}','add','')">增加</a>
					</p>
					
					<table class="tab1">
						<tbody>
							<tr>
								<td width="90" align="right">项目名称：</td>
								<td>
									<input name="queryDetailProjectName" id="queryDetailProjectName" type="text" class="allInput" />
								</td>
								<td width="90" align="right">日期：</td>
								<td>
									<div class="demo">
										<input type="text" class="allInput" id="queryTime" name="queryTime"/>
									</div>
								</td>
	                            <td width="85" align="right"><input type="submit" class="tabSub" value="查 询" /></td>
	       					</tr>
						</tbody>
					</table>
					<div class="zixun fix">
						<table class="tab2" width="100%">
							<tbody>
								<tr>
								    <!-- <th><input type="checkbox" id="all" onclick="#"/></th> -->
								    <th>序号</th>
								    <th>项目名称</th>
								    <th>开始日期</th>
								    <th>结束日期</th>
								    <th>是否完成</th>
								    <th>选项</th>
								</tr>
								<c:forEach items="${detailProjectList}" var="detailProject" varStatus="status">
									<tr <c:if test="${status.index % 2 != 0}">style='background-color:#ECF6EE;'</c:if>>
										<%-- <td><input type="checkbox"  name="ids" value="${project.projectId}"/></td> --%>
										<%-- <input type="hidden" name="projectId" id="projectId" value="${project.projectId}"> --%>
										<td>${status.index + 1}</td>
										<td>${detailProject.projectDetailName}</td>
										<td>${detailProject.getDetailStartDate()}</td>
										<td>${detailProject.getDetailEndDate()}</td>
										<td><c:if test="${detailProject.done==1}">
												 <font color="#009900">完成</font>
											</c:if>
											
											<c:if test="${detailProject.done==0}">
												<c:set var="nowDate" value="<%=System.currentTimeMillis()%>"></c:set>
												<c:if  test="${nowDate-detailProject.detailEndTime.time >0}">
													<a href="javascript:done('${detailProject.projectDetailId}')"> <font color="#FF0000">已超时</font></a>
												</c:if>
												<c:if test="${nowDate-detailProject.detailEndTime.time <=0}">
													<a href="javascript:done('${detailProject.projectDetailId}')"><font color="#0066CC">进行中</font></a>
												</c:if>
												
											</c:if>
										</td>
										<td>
											
											<a href="javascript:ShowDiv('MyModifyDiv','fade','${detailProject.projectDetailId}','modify','${detailProject.projectDetailName}')">修改</a>&nbsp;&nbsp;&nbsp;
											<a href="javascript:deleteProject('${detailProject.projectDetailId}')">删除</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div style="height:30px; line-height:30px;text-align:center;">
							<input class="btn03" type="button" onclick="javascript:back()" value="返回总任务界面"/>
						</div>
					</div>
				</div>
			</div>
	    </form>
		<!--弹出层时背景层DIV-->
		<div id="fade" class="black_overlay">
		</div>
		<!-- 修改弹出层 -->
		<div id="MyModifyDiv" class="white_content">
			<div style="text-align: right; cursor: default; height: 40px;">
			<span style="font-size: 16px;" onclick="CloseDiv('MyModifyDiv','fade')">关闭</span>
			</div>
			<h1><span id ="projectName1"></span>:分任务修改界面</h1>
			<input type="hidden" id="modifyDetailProjectId" name="modifyDetailProjectId">
			<table class="tab2" width="100%" id="fm2_table">
				<tr style='background-color:#ECF6EE;'>
					<td>原项目名称</td>
					<td><input name="beforeProjectName" id="beforeProjectName"type="text" class="allInput" readonly/></td>
				</tr>
				<tr>
					<td>现项目名称</td>
					<td><input name="modifyProjectName" id="modifyProjectName" type="text" class="allInput" /></td>
				</tr>
				<tr style='background-color:#ECF6EE;'>
					<td>开始时间</td>
					<td>
						<div class="demo">
							<input type="text" class="allInput" id="modifyStartTime" />
						</div>
					</td>
				</tr>
				<tr>
					<td>结束时间</td>
					<td>
						<div class="demo">
							<input type="text" class="allInput" id="modifyEndTime" />
						</div>
					</td>
				</tr>
			</table>
			</br>
			<div style="height:30px; line-height:30px;text-align:center;">
				<input class="btn03" type="button" onclick="javascript:modifyProject()" value="修改"/>
			</div>
		</div>
		<!-- 增加弹出层 -->
		<div id="MyAddDiv" class="white_content">
			<div style="text-align: right; cursor: default; height: 40px;">
			<span style="font-size: 16px;" onclick="CloseDiv('MyAddDiv','fade')">关闭</span>
			</div>
			<h1><span id ="projectName2"></span>:分任务添加界面</h1>
			<input type="hidden" id="addEmail" name="addEmail">
			<table class="tab2" width="100%" id="fm2_table">
				<tr style='background-color:#ECF6EE;'>
					<td>项目名称</td>
					<td><input name="addProjectName" id="addProjectName" type="text" class="allInput"/></td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td>
						<div class="demo">
							<input type="text" class="allInput" id="addStartTime" />
						</div>
					</td>
				</tr>
				<tr style='background-color:#ECF6EE;'>
					<td>结束时间</td>
					<td>
						<div class="demo">
							<input type="text" class="allInput" id="addEndTime" />
						</div>
					</td>
				</tr>
			</table>
			</br>
			<div style="height:30px; line-height:30px;text-align:center;">
				<input class="btn03" type="button" onclick="javascript:addProject()" value="增加"/>
			</div>
		</div>
		<script type="text/javascript">
			//js
			$("#addStartTime").dateDropper({
				animate: false,
				format: 'Y-m-d',
				maxYear: '2020'
			});
			$("#addEndTime").dateDropper({
				animate: false,
				format: 'Y-m-d',
				maxYear: '2020'
			});
			$("#modifyStartTime").dateDropper({
				animate: false,
				format: 'Y-m-d',
				maxYear: '2020'
			});
			$("#modifyEndTime").dateDropper({
				animate: false,
				format: 'Y-m-d',
				maxYear: '2020'
			});
			$("#queryTime").dateDropper({
				animate: true,
				format: 'Y-m-d',
				maxYear: '2020'
			});
		</script>
	</body>

</html>