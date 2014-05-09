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
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Salesman Status Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">User Name <span style="color: red">*</span> :</td>
					<td>
						<select  name="txtUserId" style="width: 200px;">
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"  <%=((request.getAttribute(Constant.USER)!= null && request.getAttribute(Constant.USER).equals(users[i].getUserId()))) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
					<td align="right">Status :</td>
					<td>
						<select name="sStatus">
							<option value="--">-----</option>
							<option value="0" <%=(request.getAttribute("status")!= null && request.getAttribute("status").equals("0")) ? "selected=\"selected\"" : "" %> style="background-color: red; color: yellow;">Open or Rejected</option>
							<option value="1" <%=(request.getAttribute("status")!= null && request.getAttribute("status").equals("1")) ? "selected=\"selected\"" : "" %> style="background-color: orange; color: black; ">Awaiting Approval</option>
							<option value="2" <%=(request.getAttribute("status")!= null && request.getAttribute("status").equals("2")) ? "selected=\"selected\"" : "" %> style="background-color: black; color: white;">Approved</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=request.getAttribute("fromDate")!= null ? request.getAttribute("fromDate") : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
					</td>
					<td align="right">To Date<span style="color: red">*</span> :</td>
					<td><input name="txtToDate" id="txtToDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=request.getAttribute("toDate")!= null ? request.getAttribute("toDate") : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" value="Get Report" onclick="fnGetReport();"/></td>
				</tr>
			</table>
			<%
				if(request.getAttribute("reportList") != null){
			%>
			<table style="width: 850px;" border = 1 cellpadding="2">
				<tr>
					<th>S No</th>
					<th width="30%">Report Date</th>
					<th width="20%">Submitted Date</th>
					<th width="20%">Approved By</th>
					<th width="20%">Status</th>
					<th width="20%">Attendance</th>
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
					<td align="center"><%=((String[])rptList.get(i))[2] %></td>
					<td align="center"><%=((String[])rptList.get(i))[3] %></td>
					<%
						if(temp.equals("Approved")) {
					%>
					<td align="center" style="background-color: black; color: white;"><%=((String[])rptList.get(i))[1] %></td>
					<%  
						} else if(temp.equals("Awaiting Approval")) {
					%>
					<td align="center" style="background-color: orange; color: black;"><%=((String[])rptList.get(i))[1] %></td>
					<%  
						} else if(temp.equals("Open Or Rejected")) {
					%>
					<td align="center" style="background-color: red; color: yellow;"><%=((String[])rptList.get(i))[1] %></td>
					<%  
						} else {
					%>
					<td align="center"><%=((String[])rptList.get(i))[1] %></td>
					<%
						}
					%>
					<td align="center"><%=((String[])rptList.get(i))[4] %></td>
				</tr>
			<%
					} 
					if(rptList.size() > 0){
			%>
				<tr><td colspan="6" align="center"><input type="button" value="Print Report" onclick="fnPrintReport();"/></td></tr>
			<%
					}
			%>
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>