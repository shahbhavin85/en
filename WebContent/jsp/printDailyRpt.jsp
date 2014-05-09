<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesmanDlyItmModel"%>
<%@page import="com.en.model.SalesmanDlyRptModel"%>
<%@page import="com.en.model.UserModel"%>
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
<script type="text/javascript" src="js/viewDlyRpt.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body onload="window.print();">
<form method="post" name="frmViewEnquiry">
	<%
		if(request.getAttribute(Constant.FORM_DATA) != null){
	%>
	<table width="100%">
		<tr>
			<td align="left"><label style="font-size: 16px; font-weight: bold;"><u>Daily Report</u></label></td>
			<td align="right"><label style="font-size: 16px; font-weight: bold;"><u>Status</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getStatus() : ""%></label></td>
		</tr>
		<tr>
			<td align="left"><label style="font-size: 16px; font-weight: bold;"><u>User Id</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getUser().getUserName() : ""%></label></td>
			<td align="right"><label style="font-size: 16px; font-weight: bold;"><u>Report Date</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptDt()) : ""%></label></td>
		</tr>
	</table>
	<table width="100%" cellspacing="0" border="1" style="margin-top: 10px;">
		<tr>
			<th style="width: 80px;">IN TIME</th>
			<th style="width: 80px;">OUT TIME</th>
			<th width="30%">DESCRIPTION</th>
			<th>REMARKS</th>
		</tr>
		<%
			if(request.getAttribute(Constant.FORM_DATA) != null){
				SalesmanDlyItmModel[] items = (SalesmanDlyItmModel[])((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getDtls().toArray(new SalesmanDlyItmModel[0]);
				for(int i=0; i<items.length; i++){
		%>
			<tr>
				<td align="center"><%=items[i].getAppInTime()%></td>
				<td align="center"><%=items[i].getAppOutTime()%></td>
		<%
					if(items[i].getType().equals("0")){
		%>
				<td>Meeting with <%=items[i].getCustomer().getCustomerName()+" - "+items[i].getCustomer().getArea()+" - "+items[i].getCustomer().getCity() %></td>
		<%
					} else {
		%>
				<td>Others</td>
		<%
					}
		%>
				<td><%=(items[i].getRemark() != null) ? items[i].getRemark() : "--" %></td>
			</tr>
		<%
				}
			}
		%>
	</table>
	<%
		}
	%>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>