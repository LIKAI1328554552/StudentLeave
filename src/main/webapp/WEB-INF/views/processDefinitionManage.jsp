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
				s_name:$("#s_name").val()
			})
			return false ;
		}) ;
	}) ;
	
	function formatterBtn(val,row) {
		return "<a href='${pageContext.request.contextPath}/processDefinition/showView?deploymentId="+row.deploymentId+"&diagramResourceName="+row.diagramResourceName+"' target='_blank'>查看流程图</a>"
	} 
</script>
</head>
<body style="margin: 1px">
	<table id="dg" class="easyui-datagrid" style="padding: 10px;" width="100%" height="100%" data-options="
		title:'流程定义管理',
		rownumber:true,
		pagination:true,
		fitColumns:true,
		method:'GET',
		url:'${pageContext.request.contextPath}/processDefinition/list',
		toolbar:'#tb'
	">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="id" width="80" align="center">编号</th>
		 		<th field="name" width="60" align="center">流程名称</th>
		 		<th field="key" width="50" align="center">流程定义的Key</th>
		 		<th field="version" width="20" align="center">版本</th>
		 		<th field="resourceName" width="70" align="center">流程定义的规则文件名称</th>
		 		<th field="diagramResourceName" width="70" align="center">流程定义的规则图片名称</th>
		 		<th field="deploymentId" width="30" align="center">流程部署Id</th>
		 		<th field="aa" width="30" align="center" formatter="formatterBtn">操作</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			&nbsp;&nbsp;&nbsp;流程名称 <input type="text" name="s_name" id="s_name" width="200px" />
			<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" >搜索</a>
		</div>
	</div>
</body>
</html>