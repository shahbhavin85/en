<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.QuotationModel"%>
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
<script type="text/javascript" src="js/quotation.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Quotation Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Quotation No</td>
					<td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+Utils.padBillNo(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId())%></b></td>
					<td align="right">Date</td>
					<td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationdate()%></b></td>
					<td align="right" valign="top">Status</td>
					<td valign="top" <%=(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("1")) ? "style='background-color : black; color : white;'" : "" %>><b><%=(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("1")) ? "Closed" : "Open"%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" valign="top" align="right">Customer</td>
					<td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getLabel()%></b></td>
					<td align="right">Tax Type</td><td><b><%if(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) {%>VAT BILL<%} else if(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></b></td>
					<td align="right">Sample Invoice</td><td><b><%if(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getSample() == 1) {%>NO<%} else {%>YES<%}%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">TIN/CST</td>
					<td colspan="1"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin() + " / " +((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst()%></b></td>
					<td align="right">Contact Person / Phone No</td>
					<td colspan="1"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getContactPerson()+ " / "+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getPhone1()%></b></td>
					<td align="right">Valid Till</td><td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getValidDate() %></b></td>
				</tr>
				<tr>
					<td align="right">Remarks</td><td colspan="3"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getRemarks()+((!((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getOrderNo().equals("0")) ? "<br/>Order No : "+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"O"+Utils.padBillNo(Integer.parseInt(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getOrderNo())) : "")%></b></td>
					<td align="right" valign="top">Salesman</td>
					<td valign="top"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserName()%></b></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="width: 950px;margin-top: 10px;">
				<tr>
					<th  style="width:50px;">S No.</th>
					<th>Description of Goods</th>
					<th style="width:50px;">Qty</th>
					<th style="width:50px;">Rate<br/>(Rs.)</th>
					<th style="width:50px;">Dis<br/>(%)</th>
					<th style="width:65px;">Gross Rate<br/>(Rs.)</th>
					<th style="width:50px;">Tax<br/>(%)</th>
					<th style="width:50px;">Tax<br/>(Rs.)</th>
					<th style="width:65px;">Nett Rate<br/>(Rs.)</th>
				</tr>
				<%
					double total = 0;
					double stotal = 0;
					double ttotal = 0;
					double qty = 0;
					SalesItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesItemModel[]) (((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new SalesItemModel[0]))) : new SalesItemModel[0];
					for(int i=0; i<salesItems.length; i++){
						
				%>
				<tr align="center">
					<td align="center" style="width:50px;"><%=(i+1)%></td>
					<td align="left"><%=salesItems[i].getItem().getItemNumber() + " / "+ salesItems[i].getItem().getItemName() + " " + salesItems[i].getDesc()%></td>
					<td style="width:50px;"><%=salesItems[i].getQty()%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getRate())%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getDisrate())%>%</td>
					<td style="width:65px;"><%=Utils.get2Decimal(((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate())%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getTaxrate())%>%</td>
					<td style="width:50px;"><%=Utils.get2Decimal(((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate() * salesItems[i].getTaxrate())/100)%></td>
					<td style="width:65px;"><%=Utils.get2Decimal((((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate())+(((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty()*(salesItems[i].getRate() * salesItems[i].getTaxrate())/100)))%></td>
				</tr>
				<%
						qty = qty + (salesItems[i].getQty());
						stotal = stotal + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate();
						ttotal = ttotal + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate() * salesItems[i].getTaxrate())/100;
						total = total + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate()+((salesItems[i].getRate() * salesItems[i].getTaxrate())/100));
					}
					if(request.getAttribute(Constant.FORM_DATA) != null && ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getInstallation() >0){
						total = total + ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getInstallation();
				%>
				<tr align="center">
					<th align="right" colspan="2">INSTALLTION(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getInstallation())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getPacking() >0){
						total = total + ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getPacking();
				%>
				<tr align="center">
					<th align="right" colspan="2">PACKING(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getPacking())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getForwarding() >0){
						total = total + ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getForwarding();
				%>
				<tr align="center">
					<th align="right" colspan="2">FORWARDING(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getForwarding())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getLess() >0){
						total = total - ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getLess();
				%>
				<tr align="center">
					<th align="right" colspan="2">LESS(-)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getLess())%></td>
				</tr>
				<% }
					double roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
					if(roundOff != 0){
						total = total + roundOff;
				%>
				<tr align="center">
					<th align="right" colspan="2">ROUNDOFF</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(roundOff)%></td>
				</tr>
				<% }  %>
				<tr>
					<th align="right" colspan="2">GRAND TOTAL</th>
					<th style="width:50px;"><%=qty%></th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;"><%=Utils.get2Decimal(stotal)%></th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;"><%=Utils.get2Decimal(ttotal)%></th>
					<th style="width:65px;"><%=Utils.get2Decimal(total)%></th>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Print" onclick="printQuotation('<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId()%>',0)" />
					&nbsp;<input type="button" value="Original Copy" onclick="printQuotation('<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId()%>',1)" />
					&nbsp;<input type="button" value="Office Copy" onclick="printQuotation('<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId()%>',2)" />
					&nbsp;<input type="button" value="Edit Bill" <%=(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).isLock() || ((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("1")) ? "disabled=\"disabled\"" : "" %> onclick="editSalesBill('<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId()%>')" />
					&nbsp;<input type="button" value="Email" onclick="fnEmailQuotation('<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId()%>')" />
					&nbsp;<input type="button" value="SMS" onclick="smsQuotation('<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId()%>')" /></td> 
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