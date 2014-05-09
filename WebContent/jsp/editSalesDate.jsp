<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.SalesItemModel"%>
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
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">
function fnGetSalesDtls(){
	var vRegex = /^\d{1,5}$/;
	if(!vRegex.test(document.forms[0].txtInvNo.value)){
		alert('Please provide a valid bill No.');
		return;
	}
	document.forms[0].txtInvoiceNo.value = document.forms[0].sAccessName.value+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "GET_SALES_DATE";
	fnFinalSubmit();
}

function fnCancel(){
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_SALES_LR_DTLS";
	fnFinalSubmit();
}

function fnUpdateEdit(){
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_SALES_DATE";
	fnFinalSubmit();
}

function fnCancelSales(){
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "CANCEL_SALES";
	fnFinalSubmit();
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Change / Cancel Sales</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td style="width: 120px;" align="right">Select Access Point :</td>
					<td>
						<select <%=(request.getAttribute(Constant.FORM_DATA) != null) ? "disabled=\"disabled\"" : "" %> name="sAccessName" style="width: 200px;" onchange="fnGetAccessPointDetails();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
								AccessPointModel temp = null;
								for(int i=0; i<accessPoints.length; i++){
									temp = accessPoints[i];
							%>
							<option value="<%=temp.getBillPrefix()%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
								((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId() == temp.getAccessId()) ? "selected=\"selected\"" : "" %>><%=temp.getAccessName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Invoice No<span style="color: red">*</span> :</td>
					<td><input <%=(request.getAttribute(Constant.FORM_DATA) != null) ? "disabled=\"disabled\"" : "" %> name="txtInvNo" <%=(request.getAttribute(Constant.FORM_DATA)!=null) ? "readOnly=\"readOnly\"" : "" %> value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() : "" ) %>" style="width: 195px;" maxlength="5">
					<%if(request.getAttribute(Constant.FORM_DATA)==null){ %>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/><%}%></td>
				</tr>
				<%if(request.getAttribute(Constant.FORM_DATA)!=null) {%>
				<tr><td colspan="2">
					<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
						<tr>
							<td style="padding-left: 20px;" align="right">Bill No</td>
							<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() %></b></td>
							<td align="right">Date</td>
							<td><input name="txtSalesDate" id="txtSalesDate" style="width: 80px;" readonly="readonly" onclick="scwShow(scwID('txtSalesDate'),event);" 
									value="<%=Utils.convertToAppDate(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSalesdate())%>"><img
				                                    src='images/date.gif'
				                                    title='Click Here' alt='Click Here'
				                                    onclick="scwShow(scwID('txtSalesDate'),event);" /></td>
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
					</td></tr>
					<tr><td colspan="2" align="center"><input type="button" value="Edit Sales Date" onclick="fnUpdateEdit();"/><input type="button" value="Cancel Sales" onclick="fnCancelSales();"/><input type="button" value="Cancel" onclick="fnGoToPage('SALES_HANDLER','INIT_GET_SALES_DATE');"/></td></tr>
				<%} %>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null)? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() : "" %>">
	<input name="txtAccessId" type="hidden" style="width: 195px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null)? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId() : "" %>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>