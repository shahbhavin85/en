<%@page import="com.en.util.Constant"%>
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
<% SalesModel model = (SalesModel) request.getAttribute("FORM_DATA"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Delivery Challan</title>
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
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><U>Delivery Challan</U></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;">&nbsp;</label></center><br/>
<center></center>
<table width="100%" style="font-family: Calibri; font-size: 13px; margin-bottom: 5px;">
	<tr>
		<td align="left" width="30%"><b>VAT No.:</b> <%=model.getBranch().getVat() %> </td>
		<td align="center"><label style="border: 2px black;border-style: solid; font-weight: bolder; font-size :12px;padding: 3px;"><%if(model.getTaxtype() == 1) {%>VAT BILL<%} else if(model.getTaxtype() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></label></td>
		<td align="right" width="30%"><b>CST No.:</b> <%=model.getBranch().getCst() %></td>
	</tr>
</table>
<center><label style="font-family: Cambria; font-size: 22px; font-stretch: wider; font-weight: bold;"><%=Constant.TITLE%></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 14px;"><%= model.getBranch().getAddress() %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 12px;"> <b> Phone No:</b> <%= model.getBranch().getStdcode() + " - " +model.getBranch().getPhone1() + "/" +model.getBranch().getPhone2() %>  <b> Email: </b><%=model.getBranch().getEmail() %></label></center>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left"><b>Invoice No.:</b> <%=model.getBranch().getBillPrefix()+Utils.padBillNo(model.getSaleid())%></td>
		<td align="right"><b> Date:</b> <%=model.getPrintSalesdate()%></td>
	</tr>
</table>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left" rowspan="5" valign="top" width="3%"><b>M/s:</b></td>
		<td align="left" rowspan="5" valign="top" width="62%"><%=model.getCustomer().getLabel()%></td>
		<td align="left" width="35%"><b> VAT No.:</b> <%=model.getCustomer().getTin()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> CST No.:</b> <%=model.getCustomer().getCst()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Person:</b> <%=model.getCustomer().getContactPerson() %></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Phone:</b> <%=model.getCustomer().getPhone1()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Order Taken By:</b> <%=model.getSalesman().getUserName()%></td>
	</tr>
	<tr>
		<td align="left" colspan="3">
			<table width="100%">
				<tr>
					<td width="33%"><b>Despatched By:</b> <%=model.getTransport() %></td>
					<td width="32%"><b> L R No:</b> </td>
					<td width="35%"><b> L R Dt:</b> </td>
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
	</tr>
<%
	double total = 0;
	double stotal = 0;
	double ttotal = 0;
	double qty = 0;
	int o=0;
	SalesItemModel itm = null;
	for(int i=0; i<model.getItems().size(); i++){
		o++;
%>
	<tr>
		<td align="center" style="min-width:50px;"><%=(i+1)%></td>
		<td align="left"><%=model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName() + model.getItems().get(i).getDesc()%></td>
		<td align="right" style="min-width:50px;"><%=model.getItems().get(i).getQty()%></td>
	</tr>
<%
		qty = qty + (model.getItems().get(i).getQty());
		stotal = stotal + model.getItems().get(i).getQty() * model.getItems().get(i).getRate();
		ttotal = ttotal + model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100;
		total = total + model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()+((model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100));
	}
%>
	<tr>
		<th align="right" colspan="2">GRAND TOTAL</th>
		<th align="right" style="min-width:50px;"><%=qty%></th>
	</tr>
<%
	for(int k=0; k<30-o; k++){
%>
	<tr><td colspan="3">&nbsp;</td></tr>
<%
	}
%>
</table>
<table class="dtls" width="100%" cellspacing ="0">
	<tr>
		<th style="border: 0px black;border-style: solid;vertical-align: top; width : 400px;  font-weight: bolder; padding-left:3px; margin-left:5px;font-size :13px;" align ="left">Remarks: <%=model.getRemarks()%></th>
		<th style="border: 1px white; vertical-align: top; padding-right:20px; font-weight : normal; font-size :14px; height: 60px;" align="right">For <b><%=Constant.TITLE%></b></th>
	</tr>
</table>
<center><br/><label style="font-size:9px; font-family: Calibri;font-weight : bold;"><u>Registered Office:</u></label><br/><label style="font-size:11px; font-family: Calibri;"><b><%=Constant.TITLE%>,</b> <%=Constant.CORP_ADDRESS%> Email : <%=Constant.CORP_EMAIL%></label></center>
</body>
</html>