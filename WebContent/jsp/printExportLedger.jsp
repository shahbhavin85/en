<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.EntryModel"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.SalesModel"%>
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
<%
if(request.getAttribute("export") != null && ((String) request.getAttribute("export")).equals("Y")){
	response.setHeader("Content-type","application/xls");
	response.setHeader("Content-disposition","inline; filename=customer_ledger_"+DateUtil.getCurrDt()+".xls");	
} 
%>
<%
EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])request.getAttribute(Constant.FORM_DATA)) : new EntryModel[0];
%>
</head>
<body onload="window.print();">
<form method="post" name="frmViewEnquiry">
	<table cellpadding="3" width="100%" style="font-weight: bold; font-size: 10px; font-family: tahoma;">
		<tr>
			<td colspan="5" align="center" style="padding-bottom: 3px;"><font style="font-size: 12px; text-decoration: underline;"><%=Constant.TITLE%></font></td>
		</tr>
		<tr>
			<td colspan="5" align="center" style="font-size: 8px;">ACCOUNTS DEPARTMENT : +91 93821 52555</td>
		</tr>
		<tr>
			<td colspan="5" align="center" style="padding-bottom: 3px;"><font style="font-size: 12px; text-decoration: underline;">LEDGER REPORT</font></td>
		</tr>
		<tr>
			<td colspan="5" align="center"><%=((CustomerModel) request.getAttribute(Constant.CUSTOMERS)).getLedgerLabel() %></td>
		</tr>
		<tr>
			<td colspan="5" align="center" style="font-size: 10px;"><%=Utils.convertToAppDateDDMMYY((String)request.getAttribute("txtFromDate"))%> To <%=Utils.convertToAppDateDDMMYY((String)request.getAttribute("txtToDate"))%></td>
		</tr>
	</table>
	<%
		if(entries.length > 0){
			double crTotal = 0;
			double drTotal = 0;
			double oTotal = 0;
	%>
	<table cellspacing="0" cellpadding="0" align="center" width="100%" border="1" style="margin-top: 10px;font-size: 9px; font-family : tahoma; border-color: transparent;">
		<tr style="border-top-color: black; border-bottom-color: black;">
			<th style="width: 50px;" style=" padding: 3px;">Date</th>
			<th style=" padding: 3px;">Particulars</th>
			<th style="width: 120px;" style=" padding: 3px;">DR. (RECEIVABLE)</th>
			<th style="width: 120px;" style=" padding: 3px;">CR. (PAYABLE)</th>
			<th style="width: 100px;" style=" padding: 3px;">Balance</th>
		</tr>
		<%
				String bgColor = "";
				for(int i=0; i<entries.length; i++){
					if(entries[i].getCrdr().equals("D")) {
						crTotal += entries[i].getAmount();
						oTotal = oTotal + entries[i].getAmount();
					} else { 
						drTotal += entries[i].getAmount();
						oTotal = oTotal - entries[i].getAmount();
					}
		%>
		<tr>
			<td align="center" valign="top"  style="border-color: transparent; padding: 3px;"><%=Utils.convertToAppDateDDMMYY(entries[i].getEntryDate())%></td>
			<td align="left" valign="top"  style="border-color: transparent; padding: 3px;"><%=entries[i].getLedgerDesc() %></td>
			<td align="center" valign="top"  style="border-color: transparent; padding: 3px;"><%=(entries[i].getCrdr().equals("D")) ? Utils.get2Decimal(entries[i].getAmount()) : "--"%></td>
			<td align="center" valign="top"  style="border-color: transparent; padding: 3px;"><%=(entries[i].getCrdr().equals("C")) ? Utils.get2Decimal(entries[i].getAmount()) : "--"%></td>
			<td align="center" valign="top"  style="border-color: transparent; padding: 3px;"><%=(oTotal > 0) ? Utils.get2Decimal(oTotal)+"Dr." : Utils.get2Decimal(0-oTotal)+"Cr."  %></td>
		</tr>
		<%
				}
		%>
		<tr style="border-top-color: black; border-bottom-color: black;"> 
			<td align="center" valign="top" style=" padding: 3px;"><%=Utils.convertToAppDateDDMMYY(Utils.convertToSQLDate((String)request.getAttribute("txtToDate")))%></td>
			<td align="left" valign="top"  style=" padding: 3px;">Closing Balance - <%=(oTotal > 0) ? "Receivable" : "Payable" %></td>
			<td align="center" valign="top"  style=" padding: 3px;"><%=Utils.get2Decimal(crTotal)%></td>
			<td align="center" valign="top"  style=" padding: 3px;"><%=Utils.get2Decimal(drTotal)%></td>
			<td align="center" valign="top"  style=" padding: 3px;"><%=(oTotal > 0) ? Utils.get2Decimal(oTotal)+"Dr." : Utils.get2Decimal(0-oTotal)+"Cr."  %></td>
		</tr>
	</table>
	<table border="1" align="center" style="font-family: tahoma; font-size: 14px; font-weight: bold; margin-top: 10px;" cellspacing="0">
		<tr>
			<td colspan="5" align="center" style="padding: 3px;">Total <%=(oTotal > 0) ? "Receivable" : "Payable" %> : <%=(oTotal > 0) ? Utils.get2Decimal(oTotal) : Utils.get2Decimal(0-oTotal) %></label></td>
		</tr>
	</table>
	<%	
		}
	%>
</form>
</body>
</html>