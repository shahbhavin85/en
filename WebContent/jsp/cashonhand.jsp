<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.util.Utils"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.AccessPointModel"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="loginClass" style="min-width: 375px;">
		<fieldset>
			<legend class="screenHeader">Cash On Hand Details</legend>
			<table border="1" cellspacing="0" cellpadding="3" id="enqItem" align="center" style="width: 350px;margin-top: 10px; font-size: 11px;">
				<tr>
					<th>Branch</th>
					<th style="width: 100px;">Amount</th>
				</tr>
				<%
					double total = 0;
					AccessPointModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel[])(request.getAttribute(Constant.FORM_DATA))) : new AccessPointModel[0];
					for(int i=0; i<entries.length; i++){
						total = total + entries[i].getOpenBal();
				%>
				<tr align="center">
					<td><%=entries[i].getAccessName()+ " - " + entries[i].getCity()%></td>
					<td align="right"><%=Utils.get2Decimal(entries[i].getOpenBal())%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th align="right">GRAND TOTAL</th>
					<th align="right"><%=Utils.get2Decimal(total) %></th>
				</tr>
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