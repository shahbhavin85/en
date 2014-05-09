<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.TransferModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.SalesModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<% TransferModel model = (TransferModel) request.getAttribute("FORM_DATA"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Sales Bill</title>
<style type="text/css">
.dtls th
{
	border-top: 1px black;
	border-bottom: 1px black;
	border-top-style: solid;
	border-bottom-style: solid;
	vertical-align: middle;
	horizontal-align : left;
	font-family: Calibri;
	font-size: 12px;
}
.dtls td
{
	vertical-align: middle;
	horizontal-align : left;
	font-family: Calibri;
	font-size: 12px;
}
.dtls table{
	border-bottom: 1px black;
	border-right: 1px black;
	border-bottom-style: solid;
	border-right-style: solid;
}
</style>
</head>
<body onload="window.print();">
<%
	String[] copyType = (String[]) request.getAttribute("copy");
	for(int a=0; a<copyType.length; a++){
		if(a!=0){
%>
<div style="page-break-after: always;"></div>
<%		}
%>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><U>STOCK TRANSFER</U></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><%=copyType[a] %></label></center><br/>
<center></center>
<table width="100%" style="font-family: Calibri; font-size: 13px; margin-bottom: 5px;">
	<tr>
		<td align="left" width="30%"><b>VAT No.:</b> <%=model.getFromBranch().getVat() %> 
		<br/><b>CST No.:</b> <%=model.getFromBranch().getCst() %></td>
	</tr>
</table>
<center><label style="font-family: Cambria; font-size: 22px; font-stretch: wider; font-weight: bold;"><%=Constant.TITLE%></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 14px;"><%= model.getFromBranch().getAddress() %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 12px;"> <b> Phone No:</b> <%= model.getFromBranch().getStdcode() + " - " +model.getFromBranch().getPhone1() + "/" +model.getFromBranch().getPhone2() %>  <b> Email: </b><%=model.getFromBranch().getEmail() %></label></center>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left"><b>Invoice No.:</b> <%=model.getFromBranch().getBillPrefix()+"ST"+Utils.padBillNo(model.getTransferid())%></td>
		<td align="right"><b> Date:</b> <%=model.getTransferdate()%></td>
	</tr>
</table>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left" rowspan="4" valign="top" width="3%"><b>M/s:</b></td>
		<td align="left" rowspan="4" valign="top" width="62%"><%=Constant.TITLE%><br/><%=model.getToBranch().getAddress() %></td>
		<td align="left" width="35%"><b> VAT No.:</b> <%=model.getToBranch().getVat()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> CST No.:</b> <%=model.getToBranch().getCst()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Phone:</b> <%=model.getToBranch().getPhone1()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Mobile:</b> <%=model.getToBranch().getMobile1()%></td>
	</tr>
	<tr>
		<td align="left" colspan="3">
			<table width="100%">
				<tr>
					<td width="33%"><b>Despatched By:</b> <%=model.getTransport() %></td>
					<td width="32%"><b> L R No:</b> <%=model.getLrno() %></td>
					<td width="35%"><b> L R Dt:</b> <%=model.getLrdt() %></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table class="dtls" width="100%" cellspacing ="0">
	<tr>
		<th align="left" style="min-width:50px;">S No.</th>
		<th align="left">Description of Goods</th>
		<th align="right" style="min-width:50px;">Qty</th>
		<th align="right" style="min-width:80px;">Rate<br/>(Rs.)</th>
		<th align="right" style="min-width:80px;">Amount<br/>(Rs.)</th>
	</tr>
<%
	double total = 0;
	double qty = 0;
	int o=0;
	for(int i=0; i<model.getItems().size(); i++){
		o++;
%>
	<tr>
		<td align="center" style="min-width:50px;"><%=(i+1)%></td>
		<td align="left"><%=model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName() + model.getItems().get(i).getDesc()%></td>
		<td align="right" style="min-width:50px;"><%=model.getItems().get(i).getQty()%></td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getRate())%></td>
		<td align="right" style="min-width:65px;"><%=Utils.get2Decimal(model.getItems().get(i).getQty() * model.getItems().get(i).getRate())%></td>
	</tr>
<%
		qty = qty + (model.getItems().get(i).getQty());
		total = total + (model.getItems().get(i).getQty() * model.getItems().get(i).getRate());
	}
%>
	<tr>
		<th align="right" colspan="2">GRAND TOTAL</th>
		<th align="right" style="min-width:50px;"><%=qty%></th>
		<th align="left" style="min-width:50px;">&nbsp;</th>
		<th align="right" style="min-width:65px;"><%=Utils.get2Decimal(total)%></th>
	</tr>
<%
	for(int k=0; k<30-o; k++){
%>
	<tr><td colspan="7">&nbsp;</td></tr>
<%
	}
%>
</table>
<table class="dtls" width="100%" cellspacing ="0">
	<tr>
		<th style="border: 0px black;border-style: solid;vertical-align: top; width : 400px;  font-weight: bolder; padding-left:3px; margin-left:5px;font-size :13px;" align ="left">Remarks: <%=model.getRemark()%></th>
		<th style="border: 1px white; vertical-align: top; padding-right:20px; font-weight : normal; font-size :14px; height: 60px;" align="right">For <b><%=Constant.TITLE%></b></th>
	</tr>
</table>
<center><br/><label style="font-size:9px; font-family: Calibri;font-weight : bold;"><u>Registered Office:</u></label><br/><label style="font-size:11px; font-family: Calibri;"><b><%=Constant.TITLE%>,</b> <%=Constant.CORP_ADDRESS%> Email : <%=Constant.CORP_EMAIL%></label></center>
<%} %>
</body>
</html>