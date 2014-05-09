<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.QuotationModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.CustomerGroupModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="loginClass" style="min-width: 625px;">
		<fieldset>
			<legend class="screenHeader">Quotation Follow up Details</legend>
			<table cellpadding="3" border=1 style="width: 600px;" align="center" cellspacing="0">
				<tr>
					<td  style="width: 120px; padding-left: 20px;" align="right">Bill No</td>
					<td><b><%=(request.getAttribute("billNo"))%></b></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="width: 600px;margin-top: 10px; font-size: 11px;">
				<tr>
					<th style="width: 60px;">S No</th>
					<th style="width: 100px;">Followed up By</th>
					<th style="width: 60px;">Followed Up Date</th>
					<th>Followed up Remarks</th>
				</tr>
				<%
					QuotationModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((QuotationModel[])(request.getAttribute(Constant.FORM_DATA))) : new QuotationModel[0];
					for(int i=0; i<entries.length; i++){
				%>
				<tr align="center">
					<td><%=entries[i].getFollowupCnt() %></td>
					<td><%=entries[i].getFollowupUser() %></td>
					<td><%=Utils.convertToAppDateDDMMYY(entries[i].getFollowupDt())%></td>
					<td align="left"><%=entries[i].getFollowupRemark()%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Close" onclick="javascript: window.close();"/></td>		 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="txtInvoiceNo"/>
</form>

</body>
</html>