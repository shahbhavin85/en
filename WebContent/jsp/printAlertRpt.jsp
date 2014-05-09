<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AlertModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/enquiryRpt.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body onload="window.print();">
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
			<table cellpadding="3" width="100%">
				<tr>
					<td align="center"><b><u>Enquiry Alert Report</u></b></td>
				</tr>
				<tr>
					<td align="center">Access Point :
							<%
								if(request.getAttribute(Constant.ACCESS_POINT) == null){
							%>--<%
								} else {
									AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
									AccessPointModel temp = null;
									for(int i=0; i<accessPoints.length; i++){
										temp = accessPoints[i];
							%><%=(request.getAttribute(Constant.ACCESS_POINT) != null && 
								Integer.parseInt((String)request.getAttribute(Constant.ACCESS_POINT)) == temp.getAccessId()) ? temp.getFullAccess() : "" %>
							<%
									}
								}
							%>
					</td>
				</tr>
				<tr>
					<td align="center">From <%=request.getAttribute("fromDate")!= null ? request.getAttribute("fromDate") : ""%> To <%=request.getAttribute("toDate")!= null ? request.getAttribute("toDate") : ""%></td>
				</tr>
			</table>
			<%
				if(request.getAttribute("reportList") != null){
			%>
			<table width="100%" border = 1 cellpadding="2">
				<tr>
					<th width="10%">Enq No</th>
					<th width="30%">Customer</th>
					<th width="15%">Alert Date</th>
					<th width="15%">Current Point</th>
					<th width="30%">Remark</th>
				</tr>
			<%
					AlertModel[] rptList = (AlertModel[]) request.getAttribute("reportList");
					for(int i=0; i<rptList.length; i++){
			%>
				<tr>
					<td align="center"><%=rptList[i].getEnqNo()%></td>
					<td align="center"><%=rptList[i].getCustomer().getFullCustomerName()%></td>
					<td align="center"><%=Utils.convertToAppDate(rptList[i].getAppDt())%></td>
					<td align="center"><%=rptList[i].getCurrentpoint().getFullAccess()%></td>
					<td align="left"><%=rptList[i].getRemarks()%></td>
				</tr>
			<%
					} 
			%>
			</table>
			<%
				}
			%>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input name="txtEnqNo" type="hidden"/>
</form>
</body>
</html>