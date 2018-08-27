<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生请假系统-登录界面</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" language="JavaScript">
	$(function() {
		$.ajax({
			type:'GET',
			dataType:'json',
			url:'${pageContext.request.contextPath}/group/combobox',
			success:function(data) {
				data.unshift({'id':'','name':'请选择角色...'}) ;
				$("#groupId").combobox({
					data:data,
					valueField:'id',
					textField:'name',
					panelHeight:'auto',
					editable:false
				}) ;
			}
		}) ;
		
		$("#submitBtn").click(function(){
			var userName = $("#userName").val() ;
			var password = $("#password").val() ;
			var groupId = $("#groupId").combobox("getValue") ;
			if(groupId != null && groupId != '') {
				$.ajax({
					type:'POST',
					async:true,
					data:{
						userName:userName,
						password:password,
						groupId:groupId
					},
					url:'${pageContext.request.contextPath}/user/login',
					success:function(data) {
						$.messager.alert("系统提示",data.data) ;
						if(data.error == null || data.error == '') {
							window.location.href = '${pageContext.request.contextPath}/page/main' ;
						}
					}
				}) ;
			} else {
				$.messager.alert("系统提示","请选择登录角色") ;
			}
			
			return false ;
		}) ;
		
		$("#resetBtn").click(function() {
			var userName = $("#userName").val("") ;
			var password = $("#password").val("") ;
			var groupId = $("#groupId").combobox("setValue","") ;
			return false ;
		}) ;
	}) ;
</script>
</head>
<body>
	<div style="position: absolute;width: 100%;height: 100%;z-index: -1;left: 0;top: 0;">
		<img src="${pageContext.request.contextPath }/static/images/login_bg.jpg" width="100%" height="100%" style="left: 0;top: 0;">
	</div>
	<div class="easyui-window" title="学生请假系统" style="width: 400px;height: 280px;padding: 10px;" data-options="
		modal:true,
		closable:false,
		collapsible:false,
		minimizable:false,
		maximizable:false">
		<form>
			<table cellpadding="6px" align="center">
				<tr align="center">
					<th colspan="2" style="padding-bottom: 10px;"><big>用户登录</big></th>
				</tr>
				<tr>
					<td>用户名：</td>
					<td>
						<input type="text" id="userName" id="userName" class="easyui-validatebox" style="width: 200px;" required/>
					</td>
				</tr>
				<tr>
					<td>用户名：</td>
					<td>
						<input type="password" id="password" id="passwrod" class="easyui-validatebox" style="width: 200px;" required/>
					</td>
				</tr>
				<tr>
					<td>角色：</td>
					<td>
						<input id="groupId" name="groupId" class="easyui-combobox"/>
					</td>
				</tr>
				<tr>
					<td colspan="2"></td>	
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<a id="submitBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-my-submit">登录</a>&nbsp;
						<a id="resetBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-my-reset">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>