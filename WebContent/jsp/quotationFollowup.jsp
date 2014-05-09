<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.QuotationModel"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnGetLinkSalesDtls(){
	
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Quotation Follow-ups</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<%
				QuotationModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (QuotationModel[]) request.getAttribute(Constant.FORM_DATA) : new QuotationModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%" border="1" style="font-size: 12px;">
				<tr>
					<th style="width: 80px;">QUOTATION NO</th>
					<th style="width: 80px;">VALID TILL</th>
					<th width="20%">CUSTOMER</th>
					<th style="width: 90px;">SAMPLE INVOICE</th>
					<th style="width: 90px;">TOTAL AMOUNT</th>
					<th>FOLLOWUP REMARKS</th>
					<th style="width: 150px;">ACTION</th>
				</tr>
				<%
					double gTotal = 0;
					double roundOff = 0;
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff);
				%>
					<tr <%=(todayBills[i].getSample() == 2) ? "style=\"background-Color:#FFFF7E;color:blue;\"" : "" %>>
						<td align="center"><a href="JavaScript:fnGetLinkSalesDtls('<%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %>')"><%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %></a></td>
						<td align="center"><%=(todayBills[i].getFollowupDt() != null && !todayBills[i].getFollowupDt().equals("")) ? Utils.convertToAppDateDDMMYY(todayBills[i].getFollowupDt()) : Utils.convertToAppDateDDMMYY(todayBills[i].getValidDate()) %></td>
						<td width="35%"><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="center"><%=(todayBills[i].getSample()==1) ? "NO" : "YES"%></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff) %></td>
						<td align="left"><%=(todayBills[i].getFollowupCnt() > 0) ?  "<b>Date</b>:"+todayBills[i].getFollowupDt()+"<br/><b>User</b>:"+todayBills[i].getFollowupUser()+"<br/><b>Remarks</b>:"+todayBills[i].getFollowupRemark() : ""%></td>
						<td align="center" valign="top"><input type="button" style="white-space:normal; font-size : 11px;" title="Open to give follow up" value="Open" onclick="fnOpenFollowupScreen('<%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %>');"/><input type="button" style="white-space:normal; font-size : 11px;" title="Order of this Quotation" value="Order" onclick="fnConvertOrder('<%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %>');" /><input type="button" style="white-space:normal; font-size : 11px;" title="Close the quotation" value="Close" onclick="fnCloseQuotaion('<%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %>');"/><br/><%if(todayBills[i].getFollowupCnt() >0){%><a href="javascript: fnShowFollowupDtls('<%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %>');">Cnt : <%=(todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupCnt() : "--" %></a><%} else {%>Cnt : <%=(todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupCnt() : "--" %><%}%></td>
					</tr>
				<%
					}
				%>
					<tr>
						<th align="right" colspan="4">TOTAL</th>
						<th align="right">Rs. <%=Utils.get2Decimal(gTotal) %></th>
						<th colspan="2">&nbsp;</th>
					</tr>
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
function fnOpenFollowupScreen(qId){
	document.forms[0].txtInvoiceNo.value = qId;
	document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_FOLLOWUP_1";
	fnFinalSubmit();
}

function fnConvertOrder(qId){
	document.forms[0].txtInvoiceNo.value = qId;
	document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
	document.forms[0].ACTIONS.value = "CONVERT_TO_ORDER";
	fnFinalSubmit();
}

function fnGetLinkSalesDtls(qId){
	window.open('/en/app?HANDLERS=QUOTATION_HANDLER&ACTIONS=SHOW_QUOTATION&txtInvoiceNo='+qId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowFollowupDtls(qId){
	window.open('/en/app?HANDLERS=QUOTATION_HANDLER&ACTIONS=GET_FOLLOWUP_DTLS&billNo='+qId,"","status=1,scrollbars=1,width=655,height=500");
}

function fnCloseQuotaion(qId){
	var r=confirm("Are you sure you want to delete "+qId+"?");
	if (r==true) {
		document.forms[0].txtInvoiceNo.value = qId;
		document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
		document.forms[0].ACTIONS.value = "CLOSE_QUOTATION";
		fnFinalSubmit();
	}
}
</script>
</html>