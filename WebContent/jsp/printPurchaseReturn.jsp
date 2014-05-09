<%@page import="com.en.util.Constant"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.PurchaseReturnModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<% PurchaseReturnModel model = (PurchaseReturnModel) request.getAttribute("FORM_DATA"); %>
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
<table style="width: 100%; padding: 0.4in;"><tr><td>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><U>DEBIT NOTE</U></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><%=copyType[a] %></label></center><br/>
<center></center>
<table width="100%" style="font-family: Calibri; font-size: 13px; margin-bottom: 5px;">
	<tr>
		<td align="left" width="30%"><b>VAT No.:</b> <%=model.getBranch().getVat() %> </td>
		<td align="center"><label style="border: 2px black;border-style: solid; font-weight: bolder; font-size :12px;padding: 3px;"><%if(model.getBillType() == 1) {%>VAT BILL<%} else if(model.getBillType() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></label></td>
		<td align="right" width="30%"><b>CST No.:</b> <%=model.getBranch().getCst() %></td>
	</tr>
</table>
<center><label style="font-family: Cambria; font-size: 22px; font-stretch: wider; font-weight: bold;"><%=Constant.TITLE%></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 14px;"><%= model.getBranch().getAddress() %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 12px;"> <b> Phone No:</b> <%= model.getBranch().getStdcode() + " - " +model.getBranch().getPhone1() + "/" +model.getBranch().getPhone2() %>  <b> Email: </b><%=model.getBranch().getEmail() %></label></center>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left"><b>Return No.:</b> <%=model.getBranch().getBillPrefix()+"PR"+Utils.padBillNo(model.getPurchaseId())%></td>
		<td align="right"><b> Date:</b> <%=Utils.convertToAppDateDDMMYY(model.getReturnDt())%></td>
	</tr>
</table>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left" rowspan="5" valign="top" width="3%"><b>M/s:</b></td>
		<td align="left" rowspan="5" valign="top" width="62%"><%=model.getSupplier().getLabel()%></td>
		<td align="left" width="35%"><b> VAT No.:</b> <%=(model.getSupplier().getTin().equals("")) ? "URD" : model.getSupplier().getTin()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> CST No.:</b> <%=model.getSupplier().getCst()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Person:</b> <%=model.getSupplier().getContactPerson() %></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Phone:</b> <%=model.getSupplier().getStdcode()+"-"+model.getSupplier().getPhone1()+"/"+model.getSupplier().getPhone2()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Mobile:</b> <%=model.getSupplier().getMobile1()+"/"+model.getSupplier().getMobile2()%></td>
	</tr>
</table>
<table class="dtls" width="100%" cellspacing ="0">
	<tr>
		<th align="left" style="min-width:50px;">S No.</th>
		<th align="left">Description of Goods</th>
		<th align="right" style="min-width:50px;">Qty</th>
		<th align="right" style="min-width:50px;">Rate<br/>(Rs.)</th>
		<th align="right" style="min-width:65px;">Gross Rate<br/>(Rs.)</th>
		<th align="right" style="min-width:50px;">Tax<br/>(%)</th>
		<th align="right" style="min-width:50px;">Tax<br/>(Rs.)</th>
		<th align="right" style="min-width:65px;">Nett Rate<br/>(Rs.)</th>
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
		<td align="left"><%=model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName()%></td>
		<td align="right" style="min-width:50px;"><%=model.getItems().get(i).getQty()%></td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getRate())%></td>
		<td align="right" style="min-width:65px;"><%=Utils.get2Decimal(model.getItems().get(i).getQty() * model.getItems().get(i).getRate())%></td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getTax())%>%</td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTax())/100)%></td>
		<td align="right" style="min-width:65px;"><%=Utils.get2Decimal(model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()+((model.getItems().get(i).getRate() * model.getItems().get(i).getTax())/100)))%></td>
	</tr>
<%
		qty = qty + (model.getItems().get(i).getQty());
		stotal = stotal + model.getItems().get(i).getQty() * model.getItems().get(i).getRate();
		ttotal = ttotal + model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTax())/100;
		//total = total +( model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()))+((model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100);
	}
	total = stotal+ttotal;
	double roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
	roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
	
		if(roundOff != 0.00){
	%><tr>
			<th align="right" style="border-color :white;" colspan="2">ROUND OFF  (<%=(roundOff>0) ? "+" : "-" %>)</th> 
			<th align="left" style="border-color :white;">&nbsp;</th>
			<th align="left" style="min-width:50px;border-color :white;">&nbsp;</th>
			<th align="left" style="min-width:50px;border-color :white;">&nbsp;</th>
			<th align="left" style="min-width:50px;border-color :white;">&nbsp;</th>
			<th align="left" style="min-width:50px;border-color :white;">&nbsp;</th>
			<th align="left" style="min-width:50px;border-color :white;">&nbsp;</th>
			<th align="right" style="min-width:50px;border-color :white;"><%=roundOff%></th>
		</tr>
	<%
			total = Utils.get2DecimalDouble(total) + roundOff;
			o++;
		}
	%>
	<tr>
		<th align="right" colspan="2">GRAND TOTAL</th>
		<th align="right" style="min-width:50px;"><%=qty%></th>
		<th align="left" style="min-width:50px;">&nbsp;</th>
		<th align="right" style="min-width:65px;"><%=Utils.get2Decimal(stotal)%></th>
		<th align="left" style="min-width:50px;">&nbsp;</th>
		<th align="right" style="min-width:50px;"><%=Utils.get2Decimal(ttotal)%></th>
		<th align="right" style="min-width:65px;"><%=Utils.get2Decimal(total)%></th>
	</tr>
<%
	for(int k=0; k<20-o; k++){
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
<!-- <label style="font-size:9px; font-family: Calibri;font-weight : bold;"><u>Terms and Conditions:</u></label><br> -->
<!-- <label style="font-size:9px; font-family: Calibri;"> -->
<!-- 	1.	Cheque or Draft should be in favor of <b>"<%=Constant.TITLE%>".</b>  -->
<!-- 	2.	Goods once sold cannot be exchanged or taken back.  -->
<!-- 	3.  <b>STRICTLY NO RETURN ACCEPTED, IF SENT WITHOUT INTIMATING WE ARE NOT RESPONSIBLE.</b>  -->
<!-- 	4.  If any discrepancy, It should be informed within 7 days.  -->
<!-- 	5.	Interest @ 24% p.a. will be charged if payment is not received within 30 days from the date of bill unless otherwise we agree to in writing.  -->
<%-- 	6.	In event of dispute jurisdiction of a competent court of law at <%=model.getBranch().getCity() %> will apply, whose judgement will be accepted as final and irrevocable.  --%>
<!-- 	7.	Our responsibility ceases once goods leave our premises.  -->
<!-- 	8.  We shall be not responsible for failure in performing our obligation if such non performance is due to reasons beyond our control. -->
<!-- 	9.	<b>OUR PRICE IS EX-SHOWROOM</b> -->
<!-- 	10.  E & O.E. <br/> -->
<!-- </label> -->
<center><br/><label style="font-size:9px; font-family: Calibri;font-weight : bold;"><u>Registered Office:</u></label><br/><label style="font-size:11px; font-family: Calibri;"><b><%=Constant.TITLE%>,</b> <%=Constant.CORP_ADDRESS%> Email : <%=Constant.CORP_EMAIL%></label></center>
</td></tr></table>
<%} %>
</body>
</html>