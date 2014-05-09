<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<script type="text/javascript">
function fnGetSalesDtls(){
	var vRegex = /^\d{1,5}$/;
	if(!vRegex.test(document.forms[0].txtInvNo.value)){
		alert('Please provide a valid bill No.');
		return;
	}
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>O'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ORDER";
	fnFinalSubmit();
}

function fnGetLinkSalesDtls(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ORDER";
	fnFinalSubmit();
}

function fnSendMultiEmail(){
	var isSelected = false;
	for(var i=0; i< document.forms[0].chkEmail.length;i++){
		if(document.forms[0].chkEmail[i].checked){
			isSelected = true;
			break;
		}
	}
	if(isSelected){
		document.forms[0].btnEmail.disabled = true;
		document.forms[0].HANDLERS.value = "ORDER_HANDLER";
		document.forms[0].ACTIONS.value = "MULTI_EMAIL";
		fnFinalSubmit();
	}
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View / Edit Order Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<% if(!((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("3") && !((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("1")){ %>
			<table cellpadding="3">
				<tr>
					<td align="right">Order No<span style="color: red">*</span> :</td>
					<td><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>O - <input name="txtInvNo" style="width: 195px;" maxlength="5">
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/></td>
				</tr>
			</table>
			<%
			}
				OrderModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (OrderModel[]) request.getAttribute(Constant.FORM_DATA) : new OrderModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%" border="1">
				<tr>
					<th colspan="7" align="center">Order Details</th>
				</tr>
				<tr>
					<th style="width: 50px;">EMAIL</th>
					<th style="width: 120px;">ORDER NO</th>
					<th>CUSTOMER</th>
					<th>IS EMAILED</th>
					<th style="width: 120px;">TOTAL AMOUNT</th>
					<th style="width: 120px;">ADVANCE</th>
					<th style="width: 120px;">STATUS</th>
				</tr>
				<%
					double gTotal = 0;
					double roundOff = 0;
					double advanceTotal = 0;
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff);
						advanceTotal = advanceTotal + todayBills[i].getAdvance();
				%>
					<tr>
						<td align="center"><input type="checkbox" name="chkEmail" value="<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId())%>"/></td>
						<td align="center"><a href="JavaScript:fnGetLinkSalesDtls('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>')"><%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %></a></td>
						<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="center"><%=(todayBills[i].isEmail()) ? "EMAILED" : "--" %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff) %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getAdvance()) %></td>
						<td align="center"><%=todayBills[i].getStatusString() %></td>
					</tr>
				<%
					}
				%>
				<tr>
					<th colspan="4" align="right">GRAND TOTAL</th>
					<th align="right"> Rs. <%=Utils.get2Decimal(gTotal) %></th>
					<th align="right"> Rs. <%=Utils.get2Decimal(advanceTotal) %></th>
					<th>&nbsp;</th>		
				</tr> 
				<tr>
					<td colspan="7" align="center"><input type="button" name="btnEmail" value="Email" onclick="fnSendMultiEmail();"/></td>
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
</html>