<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生请假系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" language="JavaScript">
	function openTab(text,url,iconCls) {
		if($("#tabs").tabs("exists",text)) {
			$("#tabs").tabs("select",text) ;
		} else {
			var content = '<iframe frameborder=0 scrolling="auto" style="width:100%;height:100%" src="${pageContext.request.contextPath}/page/'+url+'"></iframe>' ;
			$("#tabs").tabs("add",{
				title:text,
				iconCls:iconCls,
				content:content,
				closable:true
			}) ;
		}
	}

    function openPasswordModifyDialog(){
        $("#dlg").dialog("open").dialog("setTitle","修改密码")
    }
    
    function submit() {
        var flag = vaild() ;
        var oldPassWord = $("#oldPassWord").val() ;
        var newPassWord = $("#newPassWord").val() ;
        if(flag) {
            $.ajax({
                url:'${pageContext.request.contextPath }/user/updatePassWord',
                type:'POST',
                data:{
                    oldPassWord:oldPassWord,
                    newPassWord:newPassWord
                },
                success:function(data) {
                    $.messager.alert("系统提示",data.data) ;
                    $("#dlg").dialog("close") ;
                    $("#oldPassWord").val("") ;
                    $("#newPassWord").val("") ;
                    $("#confirmPassWord").val("") ;
                }
            }) ;
        }
        return false ;
    }
	
	function logout() {
		$.messager.confirm("系统提示","您确定要退出本系统吗?",function(r) {
			if(r) {
				window.location.href = "${pageContext.request.contextPath}/user/logout" ;
			}
		})
	}
    function vaild() {
        var flag = true ;
        var password = $("#oldPassWord").val() ;
        var newPassword = $("#newPassWord").val() ;
        var confirmPassword = $("#confirmPassWord").val() ;

        if(password == null || password == "") {
            alert("密码不能为空") ;
            flag = false ;
        }

        if(newPassword == null || newPassword == "") {
            alert("新密码不能为空") ;
            flag = false ;
        } else {
            if(newPassword != confirmPassword) {
                alert("两次密码不一致") ;
                flag = false ;
            }
        }

        return flag ;
    }

</script>
</head>
<body class="easyui-layout">
	<div region="north" style="height: 78px;background-color: #E0ECFF" >
		<table width="100%" style="padding: 5px;">
			<tr>
				<td width="50%">
					<img src="${pageContext.request.contextPath}/static/images/logo.png">
				</td>
				<td valign="bottom" align="right" width="50%">
					<font size="3"><strong>欢迎：${currentMemberShip.user.id }(${currentMemberShip.user.firstName }${currentMemberShip.user.lastName })【${currentMemberShip.group.name }】</strong></font>
				</td>
			</tr>
		</table>
	</div>
	<div region="center">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="首页" data-options="iconCls:'icon-home'">
			<div align="center" style="padding-top: 100px;"><font color="red" size="10">欢迎使用</font></div>
		</div>
	</div>
	</div>
	<div region="west" style="width: 200px;" title="导航菜单" split="true">
		<div class="easyui-accordion" data-options="fit:true,border:false">
		    <c:if test="${currentMemberShip.group.name=='管理员' }">
				<div title="基础数据管理" data-options="selected:true,iconCls:'icon-item'" style="padding: 10px">
					<a href="javascript:openTab('用户管理','userManage','icon-user')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user'" style="width: 150px">用户管理</a>
					<a href="javascript:openTab('角色管理','groupManage','icon-role')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-role'" style="width: 150px">角色管理</a>
					<a href="javascript:openTab('用户权限管理','authManage','icon-power')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-power'" style="width: 150px">用户权限管理</a>
				</div>
				<div title="流程管理"  data-options="iconCls:'icon-flow'" style="padding:10px;">
					<a href="javascript:openTab('流程部署管理','deployManage','icon-deploy')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-deploy'" style="width: 150px;">流程部署管理</a>
					<a href="javascript:openTab('流程定义管理','processDefinitionManage','icon-definition')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-definition'" style="width: 150px;">流程定义管理</a>
				</div>
			</c:if>
			<div title="任务管理" data-options="iconCls:'icon-task'" style="padding:10px">
				<a href="javascript:openTab('待办任务管理','daibanManage','icon-daiban')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-daiban'" style="width: 150px;">待办任务管理</a>
				<a href="javascript:openTab('已办任务管理','yibanManage','icon-yiban')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-yiban'" style="width: 150px;">已办任务管理</a>
				<a href="javascript:openTab('历史任务管理','lishiManage','icon-lishi')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-lishi'" style="width: 150px;">历史任务管理</a>
			</div>
			<c:if test="${currentMemberShip.group.name=='学生'}">
				<div title="业务管理"  data-options="iconCls:'icon-yewu'" style="padding:10px">
					<a href="javascript:openTab('请假申请','leaveManage','icon-apply')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-apply'" style="width: 150px">请假申请</a>
				</div>
			</c:if>
			<div title="系统管理"  data-options="iconCls:'icon-system'" style="padding:10px">
				<a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
				<a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'" style="width: 150px;">安全退出</a>
			</div>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog" style="width: 400px;height: 220px;padding: 10px"
		 data-options="modal:true,closed:true,buttons:'#dlg-buttons',iconCls:'icon-modifyPassword'">
		<form id="fm" style="margin-top: 5px;margin-left: 50px;">
			<table>

				<tr>
					<td align="right">原密码：</td>
					<td><input type="text" id="oldPassWord" name="oldPassWord"/></td>
				</tr>

				<tr>
					<td align="right">新密码：</td>
					<td><input type="text" id="newPassWord" name="newPassWord"/></td>
				</tr>

				<tr>
					<td align="right">确认新密码：</td>
					<td><input type="text" id="confirmPassWord" name="confirmPassWord"/></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a id="submitBtn" onclick="submit()" class="easyui-linkbutton" iconCls="icon-ok">确定</a>
	</div>

	<div region="south" style="height: 26px;padding: 5px" align="center">
		Copyright @ 2018-2020 才华有限公司 版权所有
	</div>


</body>
</html>