<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.TransferModel"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.OrderModel"%>
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
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Order Follow-ups</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			
			<%
				TransferModel[] requestBills = (request.getAttribute(Constant.TRANSFER_REQUEST_DATA)!=null) ? (TransferModel[]) request.getAttribute(Constant.TRANSFER_REQUEST_DATA) : new TransferModel[0];
				if(requestBills.length>0){
			%>
			<table width="100%" border="1" style="font-size: 12px; margin-bottom: 10px;">
				<tr>
					<th style="width: 80px;">REQUEST NO</th>
					<th style="width: 80px;">LAST FOLLOWUP</th>
					<th width="20%">REQUESTING BRANCH</th>
					<th style="width: 90px;">TOTAL QTY</th>
					<th style="width: 90px;">PENDING QTY</th>
					<th>FOLLOWUP REMARKS</th>
					<th style="width: 150px;">ACTION</th>
				</tr>
				<%
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
					for(int i=0; i<requestBills.length;i++){
				%>
					<tr style="<%=(((formatter.parse(requestBills[i].getFollowupDt())).getTime() - todayDate.getTime()) <= 0) ? "background-color : #FF6A6A; color : white;" : "" %>">
						<td align="center"><a href="JavaScript:fnGetLinkRequestDtls('<%=requestBills[i].getFromBranch().getBillPrefix()+"TR"+Utils.padBillNo(requestBills[i].getTransferid()) %>')"><%=requestBills[i].getFromBranch().getBillPrefix()+"TR"+Utils.padBillNo(requestBills[i].getTransferid()) %></a></td>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(requestBills[i].getFollowupDt()) %></td>
						<td width="20%"><%=requestBills[i].getFromBranch().getAccessName()+" - "+requestBills[i].getFromBranch().getCity() %></td>
						<td align="right"><%=Utils.get2Decimal(requestBills[i].getTotalQty()) %></td>
						<td align="right"><%=Utils.get2Decimal(requestBills[i].getTotalQty()-requestBills[i].getSentQty()) %></td>
						<td align="left"><%=(requestBills[i].getFollowupCnt() > 0) ?  "<b>Date</b>:"+requestBills[i].getFollowupDt()+"<br/><b>User</b>:"+requestBills[i].getFollowupUser()+"<br/><b>Remarks</b>:"+requestBills[i].getFollowupRemark() : ""%></td>
						<td align="center" valign="top"><input type="button" style="white-space:normal; font-size : 11px;" title="Open to give follow up" value="Open" onclick="fnOpenTransferFollowupScreen('<%=requestBills[i].getFromBranch().getBillPrefix()+"TR"+Utils.padBillNo(requestBills[i].getTransferid()) %>');"/><input type="button" style="white-space:normal; font-size : 11px;" title="Open to give follow up" <%=(((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("3")) ? "disabled=\"disabled\"" : ""%> value="Transfer" onclick="fnConvertTransfer('<%=requestBills[i].getFromBranch().getBillPrefix()+"TR"+Utils.padBillNo(requestBills[i].getTransferid()) %>');"/><br/><%if(requestBills[i].getFollowupCnt() >0){%><a href="javascript: fnShowTransferFollowupDtls('<%=requestBills[i].getFromBranch().getBillPrefix()+"TR"+Utils.padBillNo(requestBills[i].getTransferid()) %>');">Cnt : <%=(requestBills[i].getFollowupCnt() >0) ? requestBills[i].getFollowupCnt() : "--" %></a><%} else {%>Cnt : <%=(requestBills[i].getFollowupCnt() >0) ? requestBills[i].getFollowupCnt() : "--" %><%}%></td>
					</tr>
				<%
					}
				%>
			</table>
			<%
				}
			%>
			<%
				OrderModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (OrderModel[]) request.getAttribute(Constant.FORM_DATA) : new OrderModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%" border="1" style="font-size: 12px;">
				<tr>
					<th style="width: 80px;">ORDER NO</th>
					<th style="width: 80px;">LAST FOLLOWUP</th>
					<th width="20%">CUSTOMER</th>
					<th style="width: 90px;">TOTAL AMOUNT</th>
					<th style="width: 90px;">ADVANCE</th>
					<th style="width: 90px;">PENDING AMOUNT</th>
					<th style="width: 90px;">PENDING ADVANCE</th>
					<th>FOLLOWUP REMARKS</th>
					<th style="width: 150px;">ACTION</th>
				</tr>
				<%
					double gTotal = 0;
					double advTotal = 0;
					double roundOff = 0;
					double pendingTotal = 0;
					double pendingAdvance = 0;
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess()+roundOff);
						advTotal = advTotal + todayBills[i].getAdvance();
						pendingAdvance = pendingAdvance + todayBills[i].getAdvance() - todayBills[i].getUsedAdvance();
						pendingTotal = pendingTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess()+roundOff-todayBills[i].getSentAmt());
						
				%>
					<tr style="<%=(((formatter.parse(todayBills[i].getFollowupDt())).getTime() - todayDate.getTime()) <= 0) ? "background-color : #FF6A6A; color : white;" : "" %>">
						<td align="center"><a href="JavaScript:fnGetLinkSalesDtls('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>')"><%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %></a></td>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getFollowupDt()) %></td>
						<td width="20%"><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff) %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getAdvance()) %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff-todayBills[i].getSentAmt()) %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getAdvance()-todayBills[i].getUsedAdvance()) %></td>
						<td align="left"><%=(todayBills[i].getFollowupCnt() > 0) ?  "<b>Date</b>:"+todayBills[i].getFollowupDt()+"<br/><b>User</b>:"+todayBills[i].getFollowupUser()+"<br/><b>Remarks</b>:"+todayBills[i].getFollowupRemark() : ""%></td>
						<td align="center" valign="top"><input type="button" style="white-space:normal; font-size : 11px;" title="Open to give follow up" value="Open" onclick="fnOpenFollowupScreen('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>');"/><input type="button" style="white-space:normal; font-size : 11px;" title="Open to give follow up" <%=(((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("3")) ? "disabled=\"disabled\"" : ""%> value="Sales" onclick="fnConvertSales('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>');"/><input type="button" style="white-space:normal; font-size : 11px;" title="Complete the order" onclick="fnComplete('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>');" value="Completed"  /><input type="button" style="white-space:normal; font-size : 11px;" title="Cancel the order" value="Cancel" onclick="fnCancel('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>');" /><br/><%if(todayBills[i].getFollowupCnt() >0){%><a href="javascript: fnShowFollowupDtls('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>');">Cnt : <%=(todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupCnt() : "--" %></a><%} else {%>Cnt : <%=(todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupCnt() : "--" %><%}%></td>
					</tr>
				<%
					}
				%>
					<tr>
						<th align="right" colspan="3">TOTAL</th>
						<th align="right">Rs. <%=Utils.get2Decimal(gTotal) %></th>
						<th align="right">Rs. <%=Utils.get2Decimal(advTotal) %></th>
						<th align="right">Rs. <%=Utils.get2Decimal(pendingTotal) %></th>
						<th align="right">Rs. <%=Utils.get2Decimal(pendingAdvance) %></th>
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
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_FOLLOWUP_1";
	fnFinalSubmit();
}

function fnOpenTransferFollowupScreen(qId){
	document.forms[0].txtInvoiceNo.value = qId;
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_TRANSFER_FOLLOWUP";
	fnFinalSubmit();
}

function fnGetLinkSalesDtls(qId){
	window.open('/en/app?HANDLERS=ORDER_HANDLER&ACTIONS=SHOW_ORDER&txtInvoiceNo='+qId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnGetLinkRequestDtls(qId){
	window.open('/en/app?HANDLERS=TRANSFER_REQUEST_HANDLER&ACTIONS=SHOW_TRANSFER_REQUEST&txtInvoiceNo='+qId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowTransferFollowupDtls(qId){
	window.open('/en/app?HANDLERS=ORDER_HANDLER&ACTIONS=GET_TRANSFER_FOLLOWUP_DTLS&billNo='+qId,"","status=1,scrollbars=1,width=655,height=500");
}

function fnShowFollowupDtls(qId){
	window.open('/en/app?HANDLERS=ORDER_HANDLER&ACTIONS=GET_FOLLOWUP_DTLS&billNo='+qId,"","status=1,scrollbars=1,width=655,height=500");
}

function fnConvertSales(qId){
	document.forms[0].txtInvoiceNo.value = qId;
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "CONVERT_TO_SALES";
	fnFinalSubmit();
}

function fnConvertTransfer(qId){
	document.forms[0].txtInvoiceNo.value = qId;
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "CONVERT_TO_TRANSFER";
	fnFinalSubmit();
}

function fnComplete(qId){
	var r=confirm("Are you sure you completed the order?");
	if (r==true) {
		document.forms[0].txtInvoiceNo.value = qId;
		document.forms[0].HANDLERS.value = "ORDER_HANDLER";
		document.forms[0].ACTIONS.value = "COMPLETE_ORDER";
		fnFinalSubmit();
	}
}
function fnCancel(qId){
	var r=confirm("Are you sure you want to cancel the order?");
	if (r==true) {
		document.forms[0].txtInvoiceNo.value = qId;
		document.forms[0].HANDLERS.value = "ORDER_HANDLER";
		document.forms[0].ACTIONS.value = "CANCEL_ORDER";
		fnFinalSubmit();
	}
}
</script>
</html>