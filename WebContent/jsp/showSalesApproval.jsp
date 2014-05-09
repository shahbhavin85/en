<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.SalesModel"%>
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
<script type="text/javascript">
function fnBack(){
	document.forms[0].HANDLERS.value = "APPROVAL_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_APP_SALES_LST";
	fnFinalSubmit();
}

function editSalesBill(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "ACC_EDIT_SALES";
	fnFinalSubmit();
}

function fnApprove(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "APPROVAL_HANDLER";
	document.forms[0].ACTIONS.value = "APPROVE_SALES";
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Sales Invoice Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Bill No</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() %></b></td>
					<td align="right">Date</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSalesdate()%></b></td>
					<td align="right" valign="top">Salesman</td>
					<td valign="top"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserName()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" valign="top" align="right">Customer</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getLabel()%></b></td>
					<td align="right">Tax Type</td><td><b><%if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) {%>VAT BILL<%} else if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></b></td>
					<td align="right">Pay Mode</td><td><b><%if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 1) {%>CASH<%} else if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 2) {%>CREDIT CARD<%} else {%>CREDIT OF <%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCreditDays()%> DAYS<%}%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">TIN/CST</td>
					<td colspan="2"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin() + " / " +((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst()%></b></td>
					<td align="right">Contact Person / Phone No</td>
					<td colspan="2"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getContactPerson()+ " / "+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getPhone1()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">Despatched By</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTransport()%></b></td>
					<td align="right">LR No</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLrno()%></b></td>
					<td align="right" valign="top">LR Date</td>
					<td valign="top"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLrdt()%></b></td>
				</tr>
				<tr>
					<td align="right">Remarks</td><td colspan="5"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() %></b></td>
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
					<th style="width:65px;">MRP<br/>(Rs.)</th>
					<th style="width:65px;">Diff<br/>(%)</th>
				</tr>
				<%
					double total = 0;
					double stotal = 0;
					double ttotal = 0;
					double qty = 0;
					SalesItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesItemModel[]) (((SalesModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new SalesItemModel[0]))) : new SalesItemModel[0];
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
					<td><%=salesItems[i].getItem().getItemPrice()%></td>
					<td style="background-color: yellow;"><%=(Double.parseDouble(salesItems[i].getItem().getItemPrice())!=0) ? Utils.get2Decimal(((((100-salesItems[i].getDisrate())/100) * salesItems[i].getRate()) - Double.parseDouble(salesItems[i].getItem().getItemPrice()))*100/Double.parseDouble(salesItems[i].getItem().getItemPrice())) : "--"%>%</td>
				</tr>
				<%
						qty = qty + (salesItems[i].getQty());
						stotal = stotal + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate();
						ttotal = ttotal + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate() * salesItems[i].getTaxrate())/100;
						total = total + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate()+((salesItems[i].getRate() * salesItems[i].getTaxrate())/100));
					}
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation() >0){
						total = total + ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation();
				%>
				<tr align="center">
					<th align="right" colspan="2">INSTALLTION(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation())%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking() >0){
						total = total + ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking();
				%>
				<tr align="center">
					<th align="right" colspan="2">PACKING(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking())%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding() >0){
						total = total + ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding();
				%>
				<tr align="center">
					<th align="right" colspan="2">FORWARDING(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding())%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess() >0){
						total = total - ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess();
				%>
				<tr align="center">
					<th align="right" colspan="2">LESS(-)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess())%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
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
					<td>&nbsp;</td>
					<td>&nbsp;</td>
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
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<th align="right" colspan="2">ADJUSTED</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>>
					<th style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getAdjAmt())%></th>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
			<%
				double zTotal = total + Utils.get2DecimalDouble(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getAdjAmt());
			%>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="width: 600px;margin-top: 10px; font-size: 11px;">
				<tr>
					<th style="width: 60px;">Date</th>
					<th width="20%">Branch</th>
					<th>Paymode</th>
					<th width="40%">Desc</th>
					<th style="width: 60px;">Amount</th>
				</tr>
				<%	
					double bTotal = total;
					double gTotal = 0;
					total = 0;
					EntryModel[] entries = (request.getAttribute(Constant.PAYMENT_DATA) != null) ? ((EntryModel[])(request.getAttribute(Constant.PAYMENT_DATA))) : new EntryModel[0];
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
			<table cellspacing="0" border="1" align="center" style="width: 600px; margin-top: 10px;">
				<tr>
					<th>GRAND TOTAL</th>
					<th style="width: 60px;"><%=Utils.get2Decimal(zTotal) %></th>
				</tr>
				<tr>
					<th>BALANCE</th>
					<th style="width: 60px; font-size: 22px"><%=Utils.get2Decimal(zTotal-gTotal) %></th>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Approve" onclick="fnApprove('<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid()%>')" />
					&nbsp;<input type="button" value="Edit Bill" <%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).isLock() ? "disabled=\"disabled\"" : "" %> onclick="editSalesBill('<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid()%>')" />
					&nbsp;<input type="button" value="Back" onclick="fnBack();" /></td> 
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