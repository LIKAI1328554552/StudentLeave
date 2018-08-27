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
			})
			
			return false ;
		}) ;
		
		$("#openAddModelBtn").click(function() {
			$("#dlg").dialog("open").dialog("setTitle","添加流程部署") ;
			return false ;
		}) ;
		
		$("#saveBtn").click(function() {
			
			$("#fm").form("submit",{
				url:'${pageContext.request.contextPath}/deploy/deploy',
				onSubmit:function() {
					return $(this).form("validate") ;
				},
				success:function(data) {
					var data = eval('('+data+')') ;
					$("#deployFile").val("") ;
					$("#dg").datagrid("reload") ;
					$("#dlg").dialog("close") ;
					$.messager.alert("系统提示",data.data) ;
				}
			}) ;
			return false ;
		}) ;
		
		$("#removeBtn").click(function() {
			var selectRows = $("#dg").datagrid("getSelections") ;
			if(selectRows.length == 0) {
				$.messager.alert("系统提示","请选择至少一条记录") ;
			} else {
				var strIds = [] ;
				for(var x = 0 ; x < selectRows.length ; x ++) {
					strIds.push(selectRows[x].id) ;
				}
				var ids = strIds.join(",") ;
				$.messager.confirm("系统提示","您确定要删除这 " + selectRows.length + " 条记录吗？",function(r) {
					if(r) {
						$.ajax({
							type:'DELETE',
							url:'${pageContext.request.contextPath}/deploy/remove/' + ids,
							success:function(data) {
								$("#dg").datagrid("reload") ;
								$.messager.alert("系统提示",data.data) ;
							}
						}) ;
					}
				}) ;
			}
			
			return false ;
		}) ;
		
	}) ;
</script>
</head>
<body style="margin: 1px">
	<table id="dg" class="easyui-datagrid" style="padding: 10px;" width="100%" height="100%" data-options="
		title:'流程部署管理',
		rownumber:true,
		pagination:true,
		fitColumns:true,
		method:'GET',
		url:'${pageContext.request.contextPath}/deploy/list',
		toolbar:'#tb'
	">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="id" width="100" align="center">编号</th>
		 		<th field="name" width="100" align="center">流程名称</th>
		 		<th field="deploymentTime" width="100" align="center">部署时间</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="openAddModelBtn" iconCls="icon-add" plain="true" >添加</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="removeBtn" iconCls="icon-remove" plain="true" >删除</a>
		</div>
		<hr />
		<div>
			&nbsp;&nbsp;&nbsp;流程名称 <input type="text" name="s_name" id="s_name" width="200px" />
			<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" >搜索</a>
		</div>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width: 320px;height: 150px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
	 	<form id="fm" method="post" enctype="multipart/form-data">
	 		<input type="file" id="deployFile" name="deployFile" class="easyui-validatebox" required="true"/>
	 	</form>
	</div>

	<div id="dlg-buttons">
		<a id="saveBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>