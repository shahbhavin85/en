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
	<script type="text/javascript" src="js/approvalDlyRpt.js"></script>
	<link rel="stylesheet" type="text/css" href="css/common.css" />
	</head>
	<body>
	<jsp:include page="menu/menu.jsp"></jsp:include>
	<form method="post" name="frmViewEnquiry">
		<div class="formClass">
			<fieldset>
				<legend class="screenHeader">Salesman Approval Daily Report</legend>
				<jsp:include page="messages.jsp"></jsp:include>
			<%
				if(request.getAttribute(Constant.FORM_DATA) != null){
			%>
			<table width="100%" cellspacing="0" border="1" style="margin-top: 10px;">
				<tr>
					<th style="width: 80px;">S. NO.</th>
					<th>SALESMAN</th>
					<th>DATE</th>
					<th>SUBMITTED DATE / TIME</th>
					<th>ACTION</th>
				</tr>
				<%
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesmanDlyRptModel[])(request.getAttribute(Constant.FORM_DATA))).length > 0){
						SalesmanDlyRptModel[] items = (SalesmanDlyRptModel[])(request.getAttribute(Constant.FORM_DATA));
						for(int i=0; i<items.length; i++){
				%>
					<tr>
						<td align="center"><%=(i+1)%></td>
						<td align="center"><%=items[i].getUser().getUserName()%></td>
						<td align="center"><%=items[i].getRptDt() %></td>
						<td align="center"><%=items[i].getSentDt() %></td>
						<td align="center"><input type="button" value="Click to View and Approve" onclick="fnViewReport('<%=items[i].getUser().getUserId()%>','<%=items[i].getRptDt()%>');"/></td>
					</tr>
				<%
						}
					} else {
				%>
					<tr><td colspan="4" align="center">No report submitted for approval.</td></tr>
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
	<input type="hidden" name="txtUserId" value="">
	<input type="hidden" name="txtRptDate" value="">
	</form>
	</body>
	<script type="text/javascript" src="js/dateUtil.js"></script>
	</html>