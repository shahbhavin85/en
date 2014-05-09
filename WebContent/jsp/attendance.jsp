<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	var dt = document.forms[0].txtAttDate.value.substring(3,5)+"-"+document.forms[0].txtAttDate.value.substring(0,2)+"-"+document.forms[0].txtAttDate.value.substring(6);
	var myDate = new Date(dt);
	myDate.setDate(myDate.getDate() - 1);
	document.forms[0].txtAttDate.value = dateFormat(myDate,'DD-MM-YYYY');
	document.forms[0].HANDLERS.value = "ATTENDANCE_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ATTENDANCE";
	fnFinalSubmit();
}
function fnNextDay(){
	var dt = document.forms[0].txtAttDate.value.substring(3,5)+"-"+document.forms[0].txtAttDate.value.substring(0,2)+"-"+document.forms[0].txtAttDate.value.substring(6);
	var myDate = new Date(dt);
	myDate.setDate(myDate.getDate() + 1);
	document.forms[0].txtAttDate.value = dateFormat(myDate,'DD-MM-YYYY');
	document.forms[0].HANDLERS.value = "ATTENDANCE_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ATTENDANCE";
	fnFinalSubmit();
}
function fnUpdateAttendance(){
	document.forms[0].HANDLERS.value = "ATTENDANCE_HANDLER";
	document.forms[0].ACTIONS.value = "UPDATE_ATTENDANCE";
	fnFinalSubmit();
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<%
String input_date=(String)request.getAttribute("txtAttDate");
SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
Date dt1=format1.parse(input_date);
DateFormat format2=new SimpleDateFormat("EEEE"); 
String finalDay=format2.format(dt1);
%>
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
					value="<%=(request.getAttribute("txtAttDate") != null) ? request.getAttribute("txtAttDate") : DateUtil.getCurrDt()%>"></td>
				</tr>
			</table>
			<table cellpadding="8" style="width: 850px;">
				<tr>
					<td><input type="button" value="<< PREVIOUS DAY" onclick="fnPreviousDay();"/></td>
					<th style="font-size: 20px;">ATTENDANCE FOR <%=request.getAttribute("txtAttDate") %> :: (<%=finalDay.toUpperCase() %>)</th>
					<td align="right"><input type="button" value="NEXT DAY >>" onclick="fnNextDay();" <%=(DateUtil.getCurrDt().equals(request.getAttribute("txtAttDate"))) ? "disabled=\"disabled\"" : "" %>/></td>
				</tr>
			</table>
			<table border="1" cellpadding="8" style="width: 850px;">
				<tr>
					<th>USERS</th>
					<th>ATTENDANCE</th>
					<th>IN TIME</th>
					<th>OUT TIME</th>
					<th>OVERTIME</th>
					<th>LATE FINE</th>
					<th>PENALTY</th>
					<th>PENALTY REMARKS</th>
				</tr>
<%
			AttendanceModel[] details = (AttendanceModel[])request.getAttribute(Constant.FORM_DATA);
			for(int i=0; i<details.length; i++){
%>
				<tr>
					<th><%=details[i].getUser().getUserName() %> - (<%=details[i].getUser().getUserId() %>) <input <%=details[i].getUser().getType() < 4 ? "" : "name=\"users\""%> type="hidden" value="<%=details[i].getUser().getUserId()%>"/> <input type="hidden" name="type<%=details[i].getUser().getUserId()%>" value="<%=details[i].getUser().getType()%>"/> </th>
					<th>
						<select <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> name= "att<%=details[i].getUser().getUserId()%>">
							<option value = "--">-----</option>
							<option value = 0 <%=(details[i].getType().equals("0")) ? "selected=\"selected\"" : "" %>>Full Day</option>
							<option value = 1 <%=(details[i].getType().equals("1")) ? "selected=\"selected\"" : "" %>>Half Day</option>
							<option value = 2 <%=(details[i].getType().equals("2")) ? "selected=\"selected\"" : "" %>>Leave</option>
							<option value = 3 <%=(details[i].getType().equals("3")) ? "selected=\"selected\"" : "" %>>Holiday</option>
						</select>
					</th>
					<th>
						<input <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> type="text" maxlength="10" style="width: 60px;" name="inTime<%=details[i].getUser().getUserId()%>" value="<%=details[i].getInTime()%>"/>
					</th>
					<th>
						<input <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> type="text" maxlength="10" style="width: 60px;" name="outTime<%=details[i].getUser().getUserId()%>" value="<%=details[i].getOutTime()%>"/>
					</th>
					<th>
						<input <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> type="text" maxlength="5" style="width: 60px;" name="ot<%=details[i].getUser().getUserId()%>" value="<%=details[i].getOt()%>"/>
					</th>
					<th>
						<input <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> type="text" maxlength="10" style="width: 60px;" name="lateFine<%=details[i].getUser().getUserId()%>" value="<%=details[i].getLateFine()%>"/>
					</th>
					<th>
						<input <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> type="text" maxlength="10" style="width: 60px;" name="panelty<%=details[i].getUser().getUserId()%>" value="<%=details[i].getPanelty()%>"/>
					</th>
					<th>
						<textarea <%=details[i].getUser().getType() < 4 ? "disabled=\"disabled\"" : "" %> name="remark<%=details[i].getUser().getUserId()%>"><%=details[i].getRemarks()%></textarea>
					</th>
				</tr>
<%
			}
%>
			</table>
			<table style="width: 850px;">
				<tr>
					<td align="center"><input type="button" value="Updated" onclick="fnUpdateAttendance();"/></td>
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