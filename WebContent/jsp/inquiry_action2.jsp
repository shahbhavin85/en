<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.EnquiryModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.ActionType"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.OfferModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/newaction.js"></script>
<script type="text/javascript" src="js/timepicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<%
	int type = (Integer)request.getAttribute("sType");
%>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">New Action</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Enquiry No. :</td>
					<td><b><%=request.getAttribute("EnqNo")%></b></td>
				</tr>
				<tr>
					<td align="right">Action Type :</td>
					<td><b><%=ActionType.inquiry_actions[type]%></b></td>
				</tr>
				<%
				if(type == 0 || type == 7 || type == 8) {
				%>
				<tr>
					<td align="right" style="padding-left: 18px;">Remarks :</td>
					<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="<%=(type == 8) ? "Cancel Appointment" : "Close Enquiry"%>" onclick="fnForward();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
				<%
					} else if(type == 1 || type == 6){
				%>
				<tr>
					<td align="right">User Id <span style="color: red">*</span> :</td>
					<td>
						<select name="sUserId" id="sUserId" style="width: 200px;">
						
							<option value="--">-------</option>
							<%												
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Appointment Date :</td>
					<td><input name="txtAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAppointDate'),event);" 
					value="<%=DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtAppointDate'),event);" /></td>
				</tr>
				<tr>
					<td align="right">Appointment Time :</td>
					<td><input id='txtAppointTime'  name="txtAppointTime" style="width: 195px;" readonly="readonly" type='text' value='9:45 am' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime1,'txtAppointTime')" onclick="selectTime(document.forms[0].inTime1,'txtAppointTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime1" ALT="Pick a Time!" ONCLICK="selectTime(this,'txtAppointTime')" STYLE="cursor:hand;visibility: hidden;"></td>
				</tr>
				<tr>
					<td align="right" style="padding-left: 18px;">Remarks :</td>
					<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Confirm Appointment" onclick="fnAddAppointment();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
				<%
					} else if(type == 2) {
				%>
				<tr>
					<td align="right">Current Access Point :</td>
					<td><b><%=((EnquiryModel) request.getAttribute(Constant.FORM_DATA)).getAccessPoint().getAccessName()+" - "+((EnquiryModel) request.getAttribute(Constant.FORM_DATA)).getAccessPoint().getCity()%></b></td>
				</tr>
				<tr>
					<td align="right">Access Point <span style="color: red">*</span> :</td>
					<td>
						<select name="current_point" id="current_point" style="width: 200px;">
							<%
							AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
							AccessPointModel temp = null;
								for(int i=0; i<accessPoints.length; i++){
									temp = accessPoints[i];
									if(((EnquiryModel) request.getAttribute(Constant.FORM_DATA)).getAccessPoint().getAccessId() == temp.getAccessId()){
										continue;
									}
							%>
							<option value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right" style="padding-left: 18px;">Remarks :</td>
					<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Forward Enquiry" onclick="fnForward();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
				<%
					} else if(type == 4){
				%>
				<tr>
					<td align="right">Meeting Date :</td>
					<td><input name="txtAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAppointDate'),event);" 
					value="<%=DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtAppointDate'),event);" /></td>
				</tr>
				<tr>
					<td align="right">In Time :</td>
					<td><input id='txtInTime'  name="txtInTime" style="width: 195px;" readonly="readonly" type='text' value='9:45 am' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime,'txtInTime')" onclick="selectTime(document.forms[0].inTime,'txtInTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime" ALT="Pick a Time!" ONCLICK="selectTime(this,'txtInTime')" STYLE="cursor:hand;visibility: hidden;"></td>
				</tr>
				<tr>
					<td align="right">Out Time :</td>
					<td><input id='txtOutTime'  name="txtOutTime" style="width: 195px;" readonly="readonly" type='text' value='9:45 am' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].outTime,'txtOutTime')" onclick="selectTime(document.forms[0].outTime,'txtOutTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="outTime" ALT="Pick a Time!" ONCLICK="selectTime(this,'txtOutTime')" STYLE="cursor:hand;visibility: hidden;"></td>
				</tr>
				<tr>
					<td align="right" style="padding-left: 18px;">Meeting Brief :</td>
					<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Appointment Reply" onclick="fnAppointmentReply();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
				<%
					}  else if(type == 5){
				%>
				<tr>
					<td align="right">Alert Date :</td>
					<td><input name="txtAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAppointDate'),event);" 
					value="<%=DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtAppointDate'),event);" /></td>
				</tr>
				<tr>
					<td align="right" style="padding-left: 18px;">Remarks :</td>
					<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Appointment Reply" onclick="fnAlert();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
				<%
					} 
				%>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input name="txtEnqNo" value="<%=request.getAttribute("EnqNo")%>" type="hidden"/>
	<input name="sType" value="<%=type%>" type="hidden"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>