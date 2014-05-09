<%@page import="com.en.util.Constant"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.QuotationModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<% QuotationModel model = (QuotationModel) request.getAttribute("FORM_DATA"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Quotation Bill</title>
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
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><U><%if(model.getSample()==1){%>QUOTATION<%} else {%>ON-APPROVAL INVOICE<%}%></U></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><%=copyType[a] %></label></center><br/>
<center></center>
<table width="100%" style="font-family: Calibri; font-size: 13px; margin-bottom: 0px;">
	<tr>
		<td align="left" width="30%"><b>VAT No.:</b> <%=model.getBranch().getVat() %> </td>
		<!--td align="center"><label style="border: 2px black;border-style: solid; font-weight: bolder; font-size :12px;padding: 3px;"><%if(model.getTaxtype() == 1) {%>VAT BILL<%} else if(model.getTaxtype() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></label></td-->
		<td align="right" width="30%"><b>CST No.:</b> <%=model.getBranch().getCst() %></td>
	</tr>
</table>
<center><label style="font-family: Cambria; font-size: 22px; font-stretch: wider; font-weight: bold;"><%=Constant.TITLE%></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 14px;"><%= model.getBranch().getAddress() %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 12px;"> <b> Phone No:</b> <%= model.getBranch().getStdcode() + " - " +model.getBranch().getPhone1() + "/" +model.getBranch().getPhone2() %>  <b> Email: </b><%=model.getBranch().getEmail() %></label></center>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left"><b>Quotation No.:</b> <%=model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())%></td>
		<td align="right"><b> Date:</b> <%=Utils.convertToAppDate(model.getQuotationdate())%></td>
	</tr>
</table>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left" rowspan="6" valign="top" width="3%"><b>M/s:</b></td>
		<td align="left" rowspan="6" valign="top" width="62%"><%=model.getCustomer().getLabel()%></td>
		<td align="left" width="35%"><b> Valid Till:</b> <%=Utils.convertToAppDateDDMMYY(model.getValidDate())%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Person:</b> <%=model.getCustomer().getContactPerson() %></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Phone:</b> <%=model.getCustomer().getStdcode()+"-"+model.getCustomer().getPhone1()+"/"+model.getCustomer().getPhone2()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Contact Mobile:</b> <%=model.getCustomer().getMobile1()+"/"+model.getCustomer().getMobile2()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Representative:</b> <%=model.getSalesman().getUserName()%></td>
	</tr>
	<tr>
		<td align="left" width="35%"><b> Representative Mobile:</b> <%=model.getSalesman().getMobile1()%></td>
	</tr>
</table>
<table class="dtls" width="100%" cellspacing ="0">
	<tr>
		<th align="left" style="min-width:50px;">S No.</th>
		<th align="left">Description of Goods</th>
		<th align="right" style="min-width:50px;">Qty</th>
		<th align="right" style="min-width:50px;">Rate<br/>(Rs.)</th>
		<th align="right" style="min-width:50px;">Dis.<br/>(%)</th>
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
	double temp = 0;
	int o=0;
	SalesItemModel itm = null;
	for(int i=0; i<model.getItems().size(); i++){
		o++;
%>
	<tr>
		<td align="center" style="min-width:50px;"><%=(i+1)%></td>
		<td align="left"><%=model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName() + " " +  (model.getItems().get(i).getDesc().equals("--") ? "" : model.getItems().get(i).getDesc()) %></td>
		<td align="right" style="min-width:50px;"><%=model.getItems().get(i).getQty()%></td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getRate())%></td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getDisrate())%>%</td>
		<td align="right" style="min-width:65px;"><%=Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate())%></td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(model.getItems().get(i).getTaxrate())%>%</td>
		<td align="right" style="min-width:50px;"><%=Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100)%></td>
		<td align="right" style="min-width:65px;"><%=Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()+((model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100)))%></td>
	</tr>
<%
		qty = qty + (model.getItems().get(i).getQty());
		stotal = stotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate();
		ttotal = ttotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100;
		//total = total +(((100-model.getItems().get(i).getDisrate())/100)* model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()))+(((100-model.getItems().get(i).getDisrate())/100)*(model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100);
	}
	total = stotal+ttotal;
	double roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
	if(model.getPacking() > 0 || model.getForwarding() > 0 || model.getInstallation() > 0 || model.getLess() > 0 || roundOff != 0){
		o++;
%>
	<tr>
		<th align="right" style="border-bottom-color: white;" colspan="8">SUB TOTAL</th>
		<th align="right" style="border-bottom-color: white; min-width:65px;"><%=Utils.get2Decimal(total)%></th>
	</tr>
<%  
	} 
	if(model.getInstallation() > 0 || model.getPacking() > 0 || model.getForwarding() > 0){
		String tempStr = "";
	if(model.getInstallation() > 0){
		total = Utils.get2DecimalDouble(total) + model.getInstallation();
		temp = temp + model.getInstallation();
		tempStr = tempStr + "INSTALLATION (Rs."+Utils.get2Decimal(model.getInstallation())+")";
	}
	if(model.getPacking() > 0){
		total = Utils.get2DecimalDouble(total) + model.getPacking();
		temp = temp + model.getPacking();
		tempStr = tempStr + (temp > 0 ? " + " : "") + "PACKING (Rs."+Utils.get2Decimal(model.getPacking())+")";
	}
	if(model.getForwarding() > 0){
		total = Utils.get2DecimalDouble(total) + model.getForwarding();
		temp = temp + model.getForwarding();
		tempStr = tempStr + (temp > 0 ? " + " : "") + "FORWARDING (Rs."+Utils.get2Decimal(model.getForwarding())+")";
	}
	o++;
%>
	<tr>
		<td align="right" style="border-bottom-color: white;" colspan="8"><%=tempStr%></td>
		<td align="right" style="border-bottom-color: white; min-width:65px;"><%=Utils.get2Decimal(temp)%></td>
	</tr>
<%  
	}
	if(model.getLess() > 0){
		o++;
		total = Utils.get2DecimalDouble(total) - model.getLess();
%>
	<tr>
		<td align="right" style="border-bottom-color: white;" colspan="8">LESS (-)</td>
		<td align="right" style="border-bottom-color: white; min-width:65px;"><%=Utils.get2Decimal(model.getLess())%></td>
	</tr>
	<%
		}
		roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
	
		if(roundOff != 0.00){
	%><tr>
			<th align="right" style="border-color :white;" colspan="8">ROUND OFF  (<%=(roundOff>0) ? "+" : "-" %>)</th> 
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
		<th align="left" style="min-width:50px;">&nbsp;</th>
		<th align="right" style="min-width:65px;"><%=Utils.get2Decimal(stotal)%></th>
		<th align="left" style="min-width:50px;">&nbsp;</th>
		<th align="right" style="min-width:50px;"><%=Utils.get2Decimal(ttotal)%></th>
		<th align="right" style="min-width:65px;"><%=Utils.get2Decimal(total)%></th>
	</tr>
<%
	for(int k=0; k<14-o; k++){
%>
	<tr><td colspan="7">&nbsp;</td></tr>
<%
	}
%>
</table>
<table class="dtls" width="100%" cellspacing ="0">
	<tr><td colspan="2" align="center" style="border: 1px; padding : 1px; border-color: black; border-style: solid; font-weight: bold; font-size: 13px;">PAYMENT TERMS : (Cheque / demand drafts should be in favour of "<%=Constant.TITLE%>".) <br/>ADVANCE 30% - Rs.<%=Utils.get2Decimal(total*0.3)%> /  BEFORE SHIPMENT 50% - Rs.<%=Utils.get2Decimal(total*0.5)%> / AGAINST DELIVERY 20% - Rs.<%=Utils.get2Decimal(total*0.2)%></td></tr>
	<tr>
		<th style="border: 0px black;border-style: solid;vertical-align: top; width : 400px;  font-weight: bolder; padding-left:3px; margin-left:5px;font-size :13px;" align ="left">Remarks: <%=model.getRemarks()%></th>
		<th style="border: 1px white; vertical-align: top; padding-right:20px; font-weight : normal; font-size :14px; height: 50px;" align="right">For <b><%=Constant.TITLE%></b></th>
	</tr>
</table>
<fieldset>
	<legend style="font-size:9.5px; font-family: Calibri;font-weight : bold;"><u>Terms and Conditions:</u></legend>
	<label style="font-size:9.5px; font-family: Calibri;">
		<b>PRICE:</b>Prices are Exclusive of Sales Tax, Octroi and Other Statutory variations in the rate of Sales Tax and other government levies, if any, will be extra to your account.<br/>
		<b>VALIDITY:</b>The validity of the quotation can change from offer to offer.<br/>
		<b>TERMS OF PAYMENT:</b>30% in advance by means of cash/cheque/demand draft and balance 50% prior to dispatch Against Performa invoice and balance 20% against installation. Cheque / demand drafts should be in favour of <b>"<%=Constant.TITLE%>".</b><br/>
		<b>TRANSPORTATION:</b>Freight charges will be charged extra and packing charges extra.<br/>
		<b>DELIVERY:</b>Most of the orders can normally be made / supplied within 15-20 days of Receipt of your firm order with advance payment. As agreed upon.<br/>
		<b>WARRANTY:</b>All goods are guaranteed / warranty by the manufacturer against manufacturing defects arising from material or workmanship for a period of 6 - 12 months (Differs from product to product) for a basic unit excluding wear & tear, belts, stone, body and transportation from the date of invoice. The warranty is applicable only if the equipment / display are used in the way prescribed by the manufacturer. No accidental damages to any part of the machine or its accessories will be covered under warranty.<br/>
		<b>LIABILITIES:</b>Except as otherwise provided explicitly herein above, we shall not be liable for any loss or damaged what so ever or however caused arising out of or connected with this contract.<br/>
		<b>EXEMPTION:</b>We shall not be responsible for failure in performing our obligations if such non performance is due to reasons beyond our control.<br/>
		<b>AGREEMENT:</b>The foregoing terms and conditions shall prevail not withstanding any variation contained in any document received from the customer unless such variation has been specifically agreed upon in writing by HESH STORE.
	</label>
</fieldset>
<center><br/><label style="font-size:9px; font-family: Calibri;font-weight : bold;"><u>Registered Office:</u></label><br/><label style="font-size:11px; font-family: Calibri;"><b><%=Constant.TITLE%>,</b> <%=Constant.CORP_ADDRESS%> Email : <%=Constant.CORP_EMAIL%></label></center>
</td></tr></table>
<%} %>
</body>
</html>