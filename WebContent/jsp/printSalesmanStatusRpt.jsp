<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.mysql.jdbc.Util"%>
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
<script type="text/javascript" src="js/salesmanStatusRpt.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body onload="window.print();">
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Salesman Status Report</legend>
			<table cellpadding="3" width="100%">
				<tr>
					<td align="left">User Id : <b><%=request.getAttribute(Constant.USER) %></b></td>
					<td align="right">From Date : <b><%=request.getAttribute("fromDate")%></b></td>
				</tr>
				<tr>
					<td align="left">Status : <b><%if(request.getAttribute("status").equals("0")){%>Open or Rejected<%}
											else if(request.getAttribute("status").equals("1")){%>Awaiting Approval<%}
											else if(request.getAttribute("status").equals("2")){%>Approved<%}
											else {%>--<%}%></b></td>
					<td align="right">To Date : <b><%=request.getAttribute("toDate")%></b></td>
				</tr>
			</table>
			<%
				if(request.getAttribute("reportList") != null){
			%>
			<table width="100%" border = 1 cellpadding="2">
				<tr>
					<th>S No</th>
					<th width="40%">Report Date</th>
					<th width="40%">Status</th>
				</tr>
			<%
					ArrayList rptList = (ArrayList) request.getAttribute("reportList");
					String temp = "";
					for(int i=0; i<rptList.size(); i++){
						temp = ((String[])rptList.get(i))[1];
			%>
				<tr>
					<td align="center"><%=i+1%></td>
					<td align="center"><%=Utils.convertToAppDate(((String[])rptList.get(i))[0]) %></td>
					<td align="center"><%=((String[])rptList.get(i))[1] %></td>
				</tr>
			<%
					}
			%>
			</table>
			<%
				}
			%>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>