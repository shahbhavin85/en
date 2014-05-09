<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.TargetModel"%>
<%@page import="com.en.model.AttendanceModel"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.mysql.jdbc.exceptions.DeadlockTimeoutRollbackMarker"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">
function fnPreviousDay(){
	document.forms[0].HANDLERS.value = "USER_HANDLER";
	document.forms[0].ACTIONS.value = "PREV_TARGET";
	fnFinalSubmit();
}
function fnNextDay(){
	document.forms[0].HANDLERS.value = "USER_HANDLER";
	document.forms[0].ACTIONS.value = "NEXT_TARGET";
	fnFinalSubmit();
}
function fnUpdateAttendance(){
	document.forms[0].HANDLERS.value = "USER_HANDLER";
	document.forms[0].ACTIONS.value = "SAVE_TARGET";
	fnFinalSubmit();
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Sales Executive Targets</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="8" style="width: 850px;">
				<tr>
					<td><input type="button" value="<< PREVIOUS QUARTER" onclick="fnPreviousDay();"/></td>
					<th style="font-size: 20px;">QUARTER FROM <%=Utils.convertToAppDateDDMMYY((String)request.getAttribute("txtFromDate")) %> TO <%=Utils.convertToAppDateDDMMYY((String)request.getAttribute("txtToDate")) %></th>
					<td align="right"><input type="button" value="NEXT QUARTER >>" onclick="fnNextDay();"/></td>
				</tr>
			</table>
			<table border="1" cellpadding="8" style="width: 850px;">
				<tr>
					<th>USERS</th>
					<th>TARGET</th>
					<th>COMMISSION</th>
<!-- 					<th>QUARTER SALES</th> -->
				</tr>
<%
			TargetModel[] details = (TargetModel[])request.getAttribute(Constant.FORM_DATA);
			for(int i=0; i<details.length; i++){
%>
				<tr>
					<th><%=details[i].getUser().getUserName() %> - (<%=details[i].getUser().getUserId() %>) <input name="users" type="hidden" value="<%=details[i].getUser().getUserId()%>"/> </th>
					<th>
						<input type="text" maxlength="10" style="width: 120px; text-align: right;" name="target<%=details[i].getUser().getUserId()%>" value="<%=details[i].getTarget()%>"/>
					</th>
					<th>
						<input type="text" maxlength="10" style="width: 60px; text-align: right;" name="commission<%=details[i].getUser().getUserId()%>" value="<%=details[i].getCommission()%>"/>%
					</th>
<!-- 					<th> -->
<%-- 						Rs. <%=Utils.get2Decimal(details[i].getSalesAmt()) %> --%>
<!-- 					</th> -->
				</tr>
<%
			}
%>
			</table>
			<table style="width: 850px;">
				<tr>
					<td align="center"><input type="button" value="Update" onclick="fnUpdateAttendance();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtFromDate" value="<%=request.getAttribute("txtFromDate")%>"/>
	<input type="hidden" name="txtToDate" value="<%=request.getAttribute("txtToDate")%>"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>