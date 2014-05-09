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
<script type="text/javascript" src="js/salesmanEnq.js"></script>
<script type="text/javascript" src="js/timepicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<%
	int type = (Integer)request.getAttribute("sType");
%>
<script type="text/javascript">
 var vType = <%=type%>;
</script>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">New Action</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" width="100%">
				<tr>
					<td align="right">Enquiry No. :</td>
					<td><b><%=request.getAttribute("EnqNo")%></b></td>
				</tr>
				<tr>
					<td align="right">Action Type :</td>
					<td><b><%=ActionType.inquiry_actions[type]%></b></td>
				</tr>
				<%
				if(type == 8) {
				%>
				<tr>
					<td align="right" style="padding-left: 18px; width: 120px;">Remarks :</td>
					<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td align="right" style="padding-left: 18px;">More Action :</td>
					<td><input type="radio" value="noAction" name="rAction" id="noAction" onChange="fnReplyChange(this.value);" checked="checked"><label for="noAction">No Action</label> </td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%">
							<tr valign="top">
								<td width="33%">
									<fieldset>
										<legend><input type="radio" name="rAction" onChange="fnReplyChange(this.value);" value="alert" id="alert"/><label for="alert">Alert</label></legend>
										<table>
											<tr>
												<td align="right">Alert Date :</td>
												<td><input name="txtAlertDate" disabled="disabled" id="txtAlertDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAlertDate'),event);" 
												value="<%=DateUtil.getCurrDt()%>"><img
							                                    src='images/date.gif'
							                                    title='Click Here' alt='Click Here'
							                                    onclick="scwShow(scwID('txtAlertDate'),event);" /></td>
											</tr>
											<tr>
												<td align="right" style="padding-left: 18px;">Remarks :</td>
												<td><textarea  name="taAlertRemark" disabled="disabled" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
								<td width="33%">
									<fieldset>
										<legend><input type="radio" name="rAction" onChange="fnReplyChange(this.value);" value="reApp" id="reApp"/><label for="reApp">Re Appointment</label></legend>
										<table>
											<tr> 
												<td align="right" style="width: 200px;">Date :</td>
												<td><input disabled="disabled" name="txtReAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtReAppointDate'),event);"
												value="<%=DateUtil.getCurrDt()%>"><img
							                                    src='images/date.gif'
							                                    title='Click Here' alt='Click Here'
							                                    onclick="scwShow(scwID('txtReAppointDate'),event);" /></td>
											</tr>
											<tr>
												<td align="right">Time :</td>
												<td><input disabled="disabled" id='txtReAppointTime'  name="txtReAppointTime" style="width: 195px;" readonly="readonly" type='text' value='9:45 am' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime1,'txtReAppointTime')" onclick="selectTime(document.forms[0].inTime1,'txtReAppointTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime1" ALT="Pick a Time!" ONCLICK="selectTime(this,'txtReAppointTime')" STYLE="cursor:hand;visibility: hidden;"></td>
											</tr>
											<tr>
												<td align="right" style="padding-left: 18px;">Remarks :</td>
												<td><textarea disabled="disabled" name="taReRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
								<td width="33%">
									<fieldset>
										<legend><input type="radio" name="rAction" onChange="fnReplyChange(this.value);" value="close" id="close"/><label for="close">Close</label></legend>
										<table>
											<tr>
												<td align="right" style="padding-left: 18px;">Remarks :</td>
												<td><textarea  name="taCloseRemark" disabled="disabled" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Save" onclick="fnConfirmCancel();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel"/></td>
				</tr>
				<%
					} else if(type == 1 || type == 6){
				%>
				<tr>
					<td align="right" style="width: 120px;">Appointment Date :</td>
					<td><input name="txtAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly"  onclick="scwShow(scwID('txtAppointDate'),event);"
					value="<%=(String)request.getAttribute("AppDt")%>"><img
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
					<td colspan="2" align="center"><input type="button" value="Save" onclick="fnConfirmDelay();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel"/></td>
				</tr>
				<%
					} else if(type == 4){
				%>
				<tr>
					<td align="right" style="width: 120px;">Meeting Date :</td>
					<td><input name="txtAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly" 
					value="<%=(String)request.getAttribute("AppDt")%>"></td>
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
					<td align="right" style="padding-left: 18px;">More Action :</td>
					<td><input type="radio" value="noAction" name="rAction" id="noAction" onChange="fnReplyChange(this.value);" checked="checked"><label for="noAction">No Action</label> </td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%">
							<tr valign="top">
								<td width="33%">
									<fieldset>
										<legend><input type="radio" name="rAction" onChange="fnReplyChange(this.value);" value="alert" id="alert"/><label for="alert">Alert</label></legend>
										<table>
											<tr>
												<td align="right">Alert Date :</td>
												<td><input name="txtAlertDate" disabled="disabled" id="txtAlertDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAlertDate'),event);" 
												value="<%=DateUtil.getCurrDt()%>"><img
							                                    src='images/date.gif'
							                                    title='Click Here' alt='Click Here'
							                                    onclick="scwShow(scwID('txtAlertDate'),event);" /></td>
											</tr>
											<tr>
												<td align="right" style="padding-left: 18px;">Remarks :</td>
												<td><textarea  name="taAlertRemark" disabled="disabled" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
								<td width="33%">
									<fieldset>
										<legend><input type="radio" name="rAction" onChange="fnReplyChange(this.value);" value="reApp" id="reApp"/><label for="reApp">Re Appointment</label></legend>
										<table>
											<tr> 
												<td align="right" style="width: 200px;">Date :</td>
												<td><input disabled="disabled" name="txtReAppointDate" id="txtAppointDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtReAppointDate'),event);"
												value="<%=DateUtil.getCurrDt()%>"><img
							                                    src='images/date.gif'
							                                    title='Click Here' alt='Click Here'
							                                    onclick="scwShow(scwID('txtReAppointDate'),event);" /></td>
											</tr>
											<tr>
												<td align="right">Time :</td>
												<td><input disabled="disabled" id='txtReAppointTime'  name="txtReAppointTime" style="width: 195px;" readonly="readonly" type='text' value='9:45 am' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime1,'txtReAppointTime')" onclick="selectTime(document.forms[0].inTime1,'txtReAppointTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime1" ALT="Pick a Time!" ONCLICK="selectTime(this,'txtReAppointTime')" STYLE="cursor:hand;visibility: hidden;"></td>
											</tr>
											<tr>
												<td align="right" style="padding-left: 18px;">Remarks :</td>
												<td><textarea disabled="disabled" name="taReRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
								<td width="33%">
									<fieldset>
										<legend><input type="radio" name="rAction" onChange="fnReplyChange(this.value);" value="close" id="close"/><label for="close">Close</label></legend>
										<table>
											<tr>
												<td align="right" style="padding-left: 18px;">Remarks :</td>
												<td><textarea  name="taCloseRemark" disabled="disabled" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Save" onclick="fnConfirmReply();"/>
						&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel"/></td>
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
	<input name="txtRptDate" value="<%=request.getAttribute("AppDt")%>" type="hidden"/>
	<input name="sType" value="<%=type%>" type="hidden"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>