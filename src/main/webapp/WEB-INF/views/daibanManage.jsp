<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" language="JavaScript">

	$(function() {
		$("#searchBtn").click(function() {
			$("#dg").datagrid("load",{
				"s_name":$("#s_name").val()
			}) ;
		}) ;
	}) ;

	function formatterAction(val,row) {
		return "<a href='javaScript:openBLTab("+row.id+")'>办理任务</a>&nbsp;<a target='_black' href='${pageContext.request.contextPath}/task/showView?taskId="+row.id+"'>查看当前流程图</a>"
	}

	function formatterDate(val,row) {
		var datetimeType = ""; 
		var date = new Date(); 
		date.setTime(val);
		datetimeType = date.getFullYear()+"-"+getMonth(date)+"-"+getDay(date)+" "+getHours(date)+":"+getMinutes(date)+":"+getSeconds(date);//yyyy-MM-dd 00:00:00格式日期
		return datetimeType;
	
	}
	//返回 01-12 的月份值  
	function getMonth(date){ 
	  var month = ""; 
	  month = date.getMonth() + 1; //getMonth()得到的月份是0-11 
	  if(month<10){ 
	    month = "0" + month; 
	  } 
	  return month; 
	} 
	//返回01-30的日期 
	function getDay(date){ 
	  var day = ""; 
	  day = date.getDate(); 
	  if(day<10){ 
	    day = "0" + day; 
	  } 
	  return day; 
	}
	//返回小时
	function getHours(date){
	  var hours = "";
	  hours = date.getHours();
	  if(hours<10){ 
	    hours = "0" + hours; 
	  } 
	  return hours; 
	}
	//返回分
	function getMinutes(date){
	  var minute = "";
	  minute = date.getMinutes();
	  if(minute<10){ 
	    minute = "0" + minute; 
	  } 
	  return minute; 
	}
	//返回秒
	function getSeconds(date){
	  var second = "";
	  second = date.getSeconds();
	  if(second<10){ 
	    second = "0" + second; 
	  } 
	  return second; 
	}

	function openBLTab(taskId) {
    	$.ajax({
			url:"${pageContext.request.contextPath}/task/redirectPage?taskId=" + taskId,
			type:"GET",
			success:function(data) {
				window.parent.openTab("办理任务",data.data+"?taskId=" + taskId,"icon-check") ;
			}
		}) ;
	}

</script>
</head>
<body style="margin: 1px">
	<table id="dg" class="easyui-datagrid" style="padding: 10px;" width="100%" height="100%" data-options="
		title:'待办任务管理',
		rownumber:true,
		pagination:true,
		fitColumns:true,
		method:'GET',
		url:'${pageContext.request.contextPath}/task/list',
		toolbar:'#tb'
	">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="id" width="80" align="center" hidden>任务ID</th>
		 		<th field="name" width="40" align="center">任务名称</th>
		 		<th field="createTime" width="40" align="center" formatter="formatterDate">创建时间</th>
		 		<th field="action" width="30" align="center" formatter="formatterAction">操作</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			&nbsp;&nbsp;&nbsp;任务名称 <input type="text" name="s_name" id="s_name" width="200px" />
			<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" >搜索</a>
		</div>
	</div>
</body>
</html>