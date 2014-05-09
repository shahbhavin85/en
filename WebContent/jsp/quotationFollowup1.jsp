<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Quotation Followup</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Quotation No</td>
					<td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+Utils.padBillNo(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId())%></b></td>
					<td align="right">Date</td>
					<td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationdate()%></b></td>
					<td align="right" valign="top">Salesman</td>
					<td valign="top"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserName()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" valign="top" align="right">Customer</td>
					<td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getLabel()%></b></td>
					<td align="right">Tax Type</td><td colspan="3"><b><%if(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) {%>VAT BILL<%} else if(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">TIN/CST</td>
					<td colspan="2"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin() + " / " +((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst()%></b></td>
					<td align="right">Contact Person / Phone No</td>
					<td colspan="2"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getContactPerson()+ " / "+((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getPhone1()%></b></td>
				</tr>
				<tr>
					<td align="right">Valid Till</td><td><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getValidDate() %></b></td>
					<td align="right">Remarks</td><td colspan="3"><b><%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() %></b></td>
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
			<table style="width: 950px;" align="center">
				<tr>
					<td style="width: 120px;" align="right">Next Follow up Date</td>
					<td><input name="txtFollowDate" id="txtFollowDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtFollowDate'),event);" onfocus="scwShow(scwID('txtFollowDate'),event);"
					value=""><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFollowDate'),event);" /></td>
				</tr>
				<tr>
					<td valign="top" align="right">Remarks :</td>
					<td valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 550px; height : 100px;" maxlength="300"></textarea></td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Save Follow up Details" onclick="fnSave();"/><input type="button" value="Back" onclick="fnBack();"/></td>	 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="currDate" value="<%=DateUtil.getCurrDt()%>">
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="chkBill" value="<%=((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"Q"+Utils.padBillNo(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getQuotationId())%>"/>
</form>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
function fnSave(){
	if(document.forms[0].txtFollowDate.value == ''){
		alert('Please select the follow up date.');
		document.forms[0].txtFollowDate.focus();
		return;
	} else if(document.forms[0].txtRemarks.value == ''){
		alert('Please give some follow up remarks.');
		document.forms[0].txtRemarks.focus();
		return;
	}
	var vFollowDt = new Date(document.forms[0].txtFollowDate.value.substring(6), document.forms[0].txtFollowDate.value.substring(3,5)-1, document.forms[0].txtFollowDate.value.substring(0,2));
	var vCurrDt = new Date(document.forms[0].currDate.value.substring(6), document.forms[0].currDate.value.substring(3,5)-1, document.forms[0].currDate.value.substring(0,2));
	if(vFollowDt>=vCurrDt){
		document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
		document.forms[0].ACTIONS.value = "FOLLOWUP";
		fnFinalSubmit();
	} else {
		alert('Follow up date should be same or after '+document.forms[0].currDate.value);
		return;
	}
}
function fnBack(){
	document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_FOLLOWUP";
	fnFinalSubmit();
}
</script>
</html>