<%--
  Created by IntelliJ IDEA.
  User: likai
  Date: 2018/07/24
  Time: 21:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>任务办理</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script language="JavaScript">
        $(function() {
            $.ajax({
                url:"${pageContext.request.contextPath}/leave/getLeaveByTaskId?taskId=" + '${taskId }',
                type:"GET",
                success:function (data) {
                    $("#leavePerson").val(data.data.userName) ;
                    $("#leaveDays").val(data.data.leaveDays) ;
                    $("#leaveReason").val(data.data.leaveReason) ;
                    $("#taskId").val(data.data.taskId) ;

                }
            }) ;
        }) ;
        function submit(value) {
            var flag = $("#fm").form("validate") ;
            if(flag) {
                var taskId = $("#taskId").val() ;
                var leaveDays = $("#leaveDays").val() ;
                var comment = $("#comment").val() ;
                $.ajax({
                    url:'${pageContext.request.contextPath}/task/audit_bz',
                    type:'POST',
                    data:{
                        'taskId':taskId,
                        'leaveDays':leaveDays,
                        'comment':comment,
                        'state':value
                    },
                    success:function(data) {
                        $.messager.alert("系统提示",data.data) ;
                    }
                }) ;
            }
        }

    </script>
</head>
<body style="margin: 1px;">
    <div id="p" class="easyui-panel" title="班长审批" style="width: 700px;height: 280px;padding: 10px;">
        <form id="fm" method="post">
            <input type="text" name="taskId" id="taskId" hidden />
            <table cellspacing="8px">
                <tr>
                    <td>请假人:</td>
                    <td><input type="text" id="leavePerson" name="leavePerson" readonly/></td>
                    <td>&nbsp;</td>
                    <td>请假时间:</td>
                    <td><input type="text" id="leaveDays" name="leaveDays" readonly/></td>
                </tr>
                <tr>
                    <td>
                        请假原因:
                    </td>
                    <td colspan="4">
                        <textarea id="leaveReason" name="leaveReason" rows="2" cols="49" readonly></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        批注:
                    </td>
                    <td colspan="4">
                        <textarea  class="easyui-validatebox" id="comment" name="comment" rows="2" cols="49" required></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td colspan="4">
                        <a class="easyui-linkbutton" id="pzBtn" iconCls="icon-ok" onclick="submit(1)">批注</a>
                        &nbsp;&nbsp;
                        <a class="easyui-linkbutton" id="bhBtn" iconCls="icon-no" onclick="submit(2)">驳回</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
