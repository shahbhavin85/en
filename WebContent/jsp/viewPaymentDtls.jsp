<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.EntryModel"%>
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
			<legend class="screenHeader">Sales Payment Details</legend>
			<table cellpadding="3" border=1 style="width: 600px;" align="center" cellspacing="0">
				<tr>
					<td  style="width: 120px; padding-left: 20px;" align="right">Bill No</td>
					<td><b><%=(request.getAttribute("billNo"))%></b></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="width: 600px;margin-top: 10px; font-size: 11px;">
				<tr>
					<th style="width: 60px;">Date</th>
					<th width="20%">Branch</th>
					<th>Paymode</th>
					<th width="40%">Desc</th>
					<th style="width: 60px;">Amount</th>
				</tr>
				<%
					double gTotal = 0;
					double total = 0;
					EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])(request.getAttribute(Constant.FORM_DATA))) : new EntryModel[0];
					for(int i=0; i<entries.length; i++){
						total = total + entries[i].getAdjAmt() + entries[i].getAmount();
				%>
				<tr align="center">
					<td><%=Utils.convertToAppDateDDMMYY(entries[i].getEntryDate())%></td>
					<td><%=entries[i].getBranch().getAccessName()%></td>
					<td><%=Utils.getEntryType(entries[i].getEntryType())%></td>
					<td align="left"><%=entries[i].getDesc()%></td>
					<td><%=Utils.get2Decimal(entries[i].getAmount()+entries[i].getAdjAmt())%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th colspan="4" align="right">TOTAL</th>
					<th><%=Utils.get2Decimal(total) %></th>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="width: 600px;margin-top: 10px; font-size: 11px;">
				<tr>
						<th>Adjustment Type</th>
						<th>Date</th>
						<th>Description</th>
						<th style="width: 60px;">Amount</th>
					</tr>
				<%
					gTotal = gTotal + total;
					total = 0;
					EntryModel[] adjust = (request.getAttribute(Constant.ADJUST_ENTRIES) != null) ? ((EntryModel[])(request.getAttribute(Constant.ADJUST_ENTRIES))) : new EntryModel[0];
					for(int i=0; i<adjust.length; i++){
						total = total + adjust[i].getAdjAmt() + adjust[i].getAmount();
				%>
				<tr align="center">
					<td align="center"><%=Utils.getAdjustEntryType(adjust[i].getEntryType())%></td>
					<td align="center"><%=Utils.convertToAppDateDDMMYY(adjust[i].getEntryDate()) %></td>
					<td align="center"><%=adjust[i].getRemark()%></td>
					<td align="center"><%=Utils.get2Decimal(adjust[i].getAmount()+adjust[i].getAdjAmt()) %></td>
				</tr>
				<%
					}
					gTotal = gTotal + total;
				%>
				<tr>
					<th colspan="3" align="right">TOTAL</th>
					<th><%=Utils.get2Decimal(total) %></th>
				</tr>
			</table>
			<table cellspacing="0" border="1" style="width: 600px; margin-top: 10px;">
				<tr>
					<th>GRAND TOTAL</th>
					<th style="width: 60px;"><%=Utils.get2Decimal(gTotal) %></th>
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