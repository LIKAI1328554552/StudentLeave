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
		
		//自定义验证规则
		$.extend($.fn.validatebox.defaults.rules,{
			//验证Id
			vaildGroupId: {
				validator:function(value) {
					var res = "" ;
					$.ajax({
						type:'GET',
						async:false,
						url:'${pageContext.request.contextPath}/group/vaild/' + value,
						success:function(data) {
							res = data.data ;
						}
					}) ;
					
					return res == "true" ;
				},
				message:'此角色Id已经存在' 
			}
			
		}) ;
		
		$.extend($.fn.validatebox.methods, {    
		    remove: function(jq, newposition){    
		        return jq.each(function(){    
		            $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus.validatebox').unbind('blur.validatebox');  
		        });    
		    },  
		    reduce: function(jq, newposition){    
		        return jq.each(function(){    
		           var opt = $(this).data().validatebox.options;  
		           $(this).addClass("validatebox-text").validatebox(opt);  
		        });    
		    }     
		});
		
		
		$("#searchBtn").click(function() {
			var s_name = $("#s_name").val() ;
			$("#dg").datagrid("load",{
				"name":s_name
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
							url:'${pageContext.request.contextPath}/group/remove/' + ids,
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
		
		$("#addModelBtn").click(function() {
			$("#type").val("1") ;
			$("#dlg").dialog("open").dialog("setTitle","添加用户信息") ;
			$("#id").attr("readonly",false) ;
			$("#id").validatebox("reduce") ;
			return false ;
		}) ;
		
		$("#editModelBtn").click(function() {
			
			var selectRows = $("#dg").datagrid("getSelections") ;
			if(selectRows.length != 1) {
				$.messager.alert("系统提示","请选择一条数据") ;
			} else {
				var row = selectRows[0] ;
				$("#id").val(row.id) ;
				$("#name").val(row.name) ;
				$("#type").val("2") ;
				$("#dlg").dialog("open").dialog("setTitle","修改用户信息") ;
				$("#id").attr("readonly",true) ;	
				$("#id").validatebox("remove") ;
			}
			return false ;
		}) ;
		
		$("#saveBtn").click(function() {
			var type = $("#type").val() ;
			var id = $("#id").val() ;
			var name = $("#name").val() ;
			if(type == "1" || type == 1) {
				//验证表单
				var flag = $("#fm").form("validate") ;
				if(flag) {
					$.ajax({
						type:'POST',
						data:{
							id:id,
							name:name,
							type:type
						},
						url:'${pageContext.request.contextPath}/group/update',
						success:function(data) {
							$.messager.alert("系统提示",data.data) ;
							$("#dlg").dialog("close") ;
							$("#dg").datagrid("reload") ;
							cleanForm() ;
						}
					}) ;
				}
			} else {
				var flag = $("#fm").form("validate") ;
				if(flag) {
					$.ajax({
						type:'POST',
						data:{
							id:id,
							name:name,
							type:type
						},
						url:'${pageContext.request.contextPath}/group/update',
						success:function(data) {
							$.messager.alert("系统提示",data.data) ;
							$("#dlg").dialog("close") ;
							$("#dg").datagrid("reload") ;
							cleanForm() ;
						}
					}) ;
				}
			}
			
			return false ;
		}) ;
		
		$("#closeBtn").click(function() {
			cleanForm() ;
			$("#dlg").dialog("close") ;
			return false ;
		}) ;
	}) ;
	
	function cleanForm() {
		$("#type").val("") ;
		$("#id").val("") ;
		$("#name").val("") ;
	}
</script>
</head>
<body style="margin: 1px">
	<table id="dg" class="easyui-datagrid" style="padding: 10px;" width="100%" height="100%" data-options="
		title:'角色管理',
		rownumber:true,
		pagination:true,
		fitColumns:true,
		method:'GET',
		url:'${pageContext.request.contextPath}/group/list',
		toolbar:'#tb'
	">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="id" width="80" align="center">角色Id</th>
		 		<th field="name" width="100" align="center">角色名称</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a id="addModelBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加角色</a>
			<a id="editModelBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改角色</a>
			<a id="removeBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除角色</a>
		</div>
		<hr />
		<div>
			&nbsp;&nbsp;&nbsp;
			角色名：<input type="text" name="s_name" id="s_name" style="width: 200px;"/>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">搜索</a>		
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 620px;height: 250px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
	 	<form id="fm" method="post">
	 		<table cellpadding="8px">
	 			<tr>
	 				<td>角色Id：</td>
	 				<td>
	 					<input type="text" id="id" name="id" class="easyui-validatebox" required="true" data-options="validType:'vaildGroupId'"/>
	 				</td>
	 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 				<td>角色名称：</td>
	 				<td>
	 					<input type="text" id="name" name="name" class="easyui-validatebox" required="true"/>
	 					<input type="text" id="type" name="type" hidden />
	 				</td>
	 			</tr>
	 		</table>
	 	</form>
	</div>

	<div id="dlg-buttons">
		<a id="saveBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>