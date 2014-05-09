<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.util.RequestContants"%>
<%@page import="com.en.model.AppointmentModel"%>
<%@page import="com.en.util.DateUtil"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Enquiry Pending Appointment</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border="1" width="100%">
				<tr>
					<th align="center" style="width: 80px;">Enquiry No</th>
					<th align="center" style="width: 120px;">Appointment Date</th>
					<th align="center" style="width: 120px;">Appointment Time</th>
					<th align="center">Description</th>
					<th align="center" style="width: 200px;">Actions</th>
				</tr>
			<%
				AppointmentModel[] appList = request.getAttribute(RequestContants.PENDING_APP_LIST) != null ? (AppointmentModel[]) request.getAttribute(RequestContants.PENDING_APP_LIST) : new AppointmentModel[0];
				int k = appList.length;
				for(int i=0; i<k; i++){
			%>
				<tr>
					<td valign="top" align="center"><a href="javascript:fnOpenEnquiryPopup(<%=appList[i].getEnqNo()%>);"><%=appList[i].getEnqNo()%></a></td>
					<td valign="top" align="center"><%=Utils.convertToAppDate(appList[i].getAppDt()) %></td>
					<td valign="top" align="center"><%=appList[i].getAppAppTime() %></td>
					<td valign="top">Customer : <%=appList[i].getCustomer().getFullCustomerName() %><br/>Remarks : <%=appList[i].getRemarks()%></td>
					<td valign="top" align="center"><input type="button" onclick="javascript:fnReplyToAppointment(<%=appList[i].getEnqNo()%>,'<%=appList[i].getAppDt()%>');" value="Reply">&nbsp;<input type="button" value="Delay" onclick="javascript:fnDelayToAppointment(<%=appList[i].getEnqNo()%>,'<%=appList[i].getAppDt()%>');">&nbsp;<input type="button" value="Cancel" onclick="javascript:fnCancelToAppointment(<%=appList[i].getEnqNo()%>,'<%=appList[i].getAppDt()%>');"></td>
				</tr>
			<%
				}
			%>
			</table>
			<table width="100%"><tr><td align="center"><input type="button" value="Go Back" onclick="fnGoBack();"/></td></tr></table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtEnqNo"/>
	<input type="hidden" name="txtAppDt"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>