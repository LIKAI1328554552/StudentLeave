<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
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
			var s_userName = $("#s_userName").val() ;
			$("#dg").datagrid("load",{
				"id":s_userName
			}) ;
			
			return false ;
		}) ;
		
		$("#authModelBtn").click(function() {
			var selectRows = $("#dg").datagrid("getSelections") ;
			if(selectRows.length != 1) {
				$.messager.alert("系统提示","请选择一条数据") ;
			} else {
				var row = selectRows[0] ;
				userId = row.id ;
				$("#dlg").dialog("open").dialog("setTitle","设置用户权限") ;
				loadAllGroups() ;
				loadGroupsByUserId(row.id) ;
			}
			return false ;
		}) ;
		
		$("#saveBtn").click(function() {
			var obj = $("input[name='groupId']") ;
			var groupIds = "" ;
			for(var x = 0 ; x < obj.length ; x ++) {
				if(obj[x].checked) {
					groupIds += obj[x].value + "," ;
				}
			}
			$.ajax({
				type:'POST',
				data:{
					userId:userId,
					groupIds:groupIds
				},
				url:'${pageContext.request.contextPath}/membership/update',
				success:function(data) {
					$("#dg").datagrid("reload") ;
					$("#dlg").dialog("close") ;
					$.messager.alert("系统提示",data.data) ;
				}
			})
			return false ;
		}) ;
		
		$("#closeBtn").click(function() {
			$("#dlg").dialog("close") ;
			return false ;
		}) ;
		
	}) ;
	
	function loadAllGroups() {
		var content = "" ;
		//置空
		$("#groupList").empty() ;
		$.ajax({
			type:'GET',
			async:false,
			url:'${pageContext.request.contextPath}/group/combobox',
			success:function(data) {
				for(var x = 0 ; x < data.length ; x ++) {
					content += '<input type="checkbox" name="groupId" value="'+data[x].id+'" /><font>'+data[x].name+'</font>&nbsp;' ;
				}
			}
		}) ;
		$("#groupList").html(content) ;
	}
	
	function loadGroupsByUserId(id) {
		$.ajax({
			type:'GET',
			async:false,
			data:{
				id:id
			},
			url:'${pageContext.request.contextPath}/group/findByUserId',
			success:function(data) {
				for(var x = 0 ; x < data.length ; x ++) {
					$("[value="+data[x].id+"]:checkbox").attr("checked",true) ;
				}
			}
		}) ;
	}
</script>
</head>
<body style="margin: 1px">
	<table id="dg" class="easyui-datagrid" style="padding: 10px;" width="100%" height="100%" data-options="
		title:'用户管理',
		rownumber:true,
		pagination:true,
		fitColumns:true,
		method:'GET',
		url:'${pageContext.request.contextPath}/user/uagList',
		toolbar:'#tb'
	">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="id" width="80" align="center">用户名</th>
		 		<th field="firstName" width="50" align="center">姓</th>
		 		<th field="lastName" width="50" align="center">名</th>
		 		<th field="email" width="100" align="center">邮箱</th>
		 		<th field="groups" width="80" align="center">用户角色</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div>
			<a id="authModelBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-power" plain="true">用户权限设置</a>
		</div>
		<hr />
		<div>
			&nbsp;&nbsp;&nbsp;
			用户名：<input type="text" name="s_userName" id="s_userName" style="width: 200px;"/>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">搜索</a>		
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 450px;height: 200px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
	 	<div id="groupList" style="padding: 10px;"></div>
	</div>

	<div id="dlg-buttons">
		<a id="saveBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>