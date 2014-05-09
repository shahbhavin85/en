<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
function fnGetAttendanceDetails(){
	var appDt = document.forms[0].txtAttDate.value.substring(3,5)+"-"+document.forms[0].txtAttDate.value.substring(0,2)+"-"+document.forms[0].txtAttDate.value.substring(6);
	var currDt = '<%=DateUtil.getCurrDt()%>';
	currDt = currDt.substring(3,5)+"-"+currDt.substring(0,2)+"-"+currDt.substring(6);
	if((new Date(appDt)).getTime() > (new Date(currDt)).getTime()){
		alert('You cannot select the future dates.');
		return;
	}
	document.forms[0].HANDLERS.value = "ATTENDANCE_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ATTENDANCE";
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
			<legend class="screenHeader">Staff Attendance</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Attendance Date<span style="color: red">*</span> :</td>
					<td><input name="txtAttDate" id="txtAttDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAttDate'),event);" 
					value="<%=(request.getAttribute("txtAttDate") != null) ? request.getAttribute("txtAttDate") : DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtAttDate'),event);" />
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Go >>" onclick="fnGetAttendanceDetails()"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>