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
		$("#openAddModel").click(function() {
			$("#dlg").dialog("open").dialog("setTitle","添加请假信息") ;
			return false ;
		}) ;

		$("#closeBtn").click(function() {
			$("#dlg").dialog("close");
			return false ;
		}) ;
		
		
		$("#saveBtn").click(function() {
			var flag = $("#fm").form("validate") ;
			if(flag) {
				var leaveDays = $("#leaveDays").val() ;
				var leaveReason = $("#leaveReason").val() ;
				$.ajax({
					type:'POST',
					data:{
						leaveDays:leaveDays,
						leaveReason:leaveReason
					},
					url:'${pageContext.request.contextPath}/leave/update',
					success:function(data) {
						$.messager.alert("系统提示",data.data) ;
						cleanInfo() ;
						$("#dg").datagrid("reload") ;
						$("#dlg").dialog("close") ;
					}
				})  ;
			}
			return false ;
		}) ;
	}) ;
	function formatterAction(val,row) {
		var state = row.state ;
		if(state=="未提交") {
			return "<a href='javascript:startApply("+JSON.stringify(row.id)+")'>发起流程</a>" ;
		} else if(state == "审核通过" || state == "审核未通过") {
			return "<a href='javascript:openCommentListModel("+ JSON.stringify(row.processInstanceId) +")'>查看历史批注</a>" ;
		} else {
			return "" ;
		}
	}
	
	function startApply(leaveId) {
		console.log(leaveId)
		$.ajax({
			type:'GET',
			data:{
				leaveId:leaveId
			},
			url:'${pageContext.request.contextPath}/leave/startApply',
			success:function(data) {
				$.messager.alert("系统提示",data.data) ;
				$("#dg").datagrid("reload") ;	
			}
		}) ;
	}

	function openCommentListModel(processInstanceId) {
		//加载表格
		$("#hisList").datagrid("load",{
		    processInstanceId:processInstanceId
		}) ;

		//打开对话框
        $("#dlg2").dialog("open").dialog("setTitle","历史批注") ;
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
	
	function cleanInfo() {
		$("#leaveDays").val("") ;
		$("#leaveReason").val("") ;
	}

</script>
</head>
<body style="margin: 1px">
	<table id="dg" class="easyui-datagrid" style="padding: 10px;" width="100%" height="100%" data-options="
		title:'请假信息管理',
		rownumber:true,
		pagination:true,
		fitColumns:true,
		method:'GET',
		url:'${pageContext.request.contextPath}/leave/list',
		toolbar:'#tb'
	">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="id" width="80" align="center" hidden></th>
				<th field="processInstanceId" width="80" align="center" hidden></th>
		 		<th field="leaveDate" width="40" align="center" formatter="formatterDate">请假日期</th>
		 		<th field="leaveDays" width="40" align="center">请假天数</th>
		 		<th field="leaveReason" width="100" align="center">请假原因</th>
		 		<th field="state" width="40" align="center">状态</th>
		 		<th field="action" width="30" align="center" formatter="formatterAction">操作</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a id="openAddModel" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加请假信息</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改请假信息</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除请假信息</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 620px;height: 250px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
	 	<form id="fm" method="post">
	 		<table cellpadding="8px">
	 			<tr>
	 				<td>请假天数：</td>
	 				<td>
	 					<input type="text" id="leaveDays" name="leaveDays" class="easyui-numberbox" required="true"/>
	 				</td>
	 			</tr>
	 			<tr>
	 				<td>请假原因：</td>
	 				<td>
	 					<textarea type="text" id="leaveReason" name="leaveReason"  rows="5" cols="49" class="easyui-validatebox" required="true"></textarea>
	 				</td>
	 			</tr>
	 		</table>
	 	</form>
	</div>

	<div id="dlg-buttons">
		<a id="saveBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>

	<div id="dlg2" class="easyui-dialog" style="width: 700px;height: 250px;" closed="true" >
		<table id="hisList" class="easyui-datagrid" style="width: 690px;height: 230px;padding-top: 5px;" data-options="
            url:'${pageContext.request.contextPath}/task/listHistoryCommentByProcessInstanceId',
		    method:'GET',
		    fitColumns:true
            "
		>
			<thead>
			<tr>
				<th field="time" width="120" align="center" formatter="formatterDate">批注时间</th>
				<th field="userId" width="100" align="center">批注人</th>
				<th field="message" width="200" align="center">批注信息</th>
			</tr>
			</thead>

		</table>
	</div>


</body>
</html>