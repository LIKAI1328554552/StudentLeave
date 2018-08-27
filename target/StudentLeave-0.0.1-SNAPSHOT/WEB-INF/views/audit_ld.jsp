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
                var comment = $("#comment").val() ;
                $.ajax({
                    url:'${pageContext.request.contextPath}/task/audit_ld',
                    type:'POST',
                    data:{
                        'taskId':taskId,
                        'comment':comment,
                        'state':value
                    },
                    success:function(data) {
                        $.messager.alert("系统提示",data.data) ;
                    }
                }) ;
            }
        }

        function dateFormatter(val,row) {
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
<body style="margin: 1px;">
    <div id="p" class="easyui-panel" title="领导审批" style="width: 700px;height: 280px;padding: 10px;">
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

    <div id="h" class="easyui-panel" title="批注列表" style="padding-top: 5px;">

        <table id="hisList" class="easyui-datagrid" style="width: 700px;height: 200px;" data-options="
            url:'${pageContext.request.contextPath}/task/listHistoryComment?taskId=${taskId }',
		    method:'GET',
		    fitColumns:true
            "
        >
            <thead>
                <tr>
                    <th field="time" width="120" align="center" formatter="dateFormatter">批注时间</th>
                    <th field="userId" width="100" align="center">批注人</th>
                    <th field="message" width="200" align="center">批注信息</th>
                </tr>
            </thead>

        </table>
    </div>
</body>
</html>
