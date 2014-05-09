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
<script type="text/javascript">
function fnGetSalesDtls(){
	var vRegex = /^\d{1,5}$/;
	if(!vRegex.test(document.forms[0].txtInvNo.value)){
		alert('Please provide a valid bill No.');
		return;
	}
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>Q'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
	document.forms[0].ACTIONS.value = "GET_QUOTATION";
	fnFinalSubmit();
}

function fnGetLinkSalesDtls(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "QUOTATION_HANDLER";
	document.forms[0].ACTIONS.value = "GET_QUOTATION";
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
			<legend class="screenHeader">View / Edit Quotation Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
			<% if(!((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("3") && !((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("1")){ %>
				<tr>
					<td align="right">Quotation No<span style="color: red">*</span> :</td>
					<td><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>Q - <input name="txtInvNo" style="width: 195px;" maxlength="5">
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/></td>
				</tr>
			</table>
			<%
			}
				QuotationModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (QuotationModel[]) request.getAttribute(Constant.FORM_DATA) : new QuotationModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%" border="1">
				<tr>
					<th colspan="4" align="center">Quotation Details</th>
				</tr>
				<tr>
					<th style="width: 120px;">QUOTATION NO</th>
					<th style="width: 120px;">VALID TILL</th>
					<th>CUSTOMER</th>
					<th style="width: 120px;">TOTAL AMOUNT</th>
				</tr>
				<%
					double gTotal = 0;
					double roundOff = 0;
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff);
				%>
					<tr>
						<td align="center"><a href="JavaScript:fnGetLinkSalesDtls('<%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %>')"><%=todayBills[i].getBranch().getBillPrefix()+"Q"+Utils.padBillNo(todayBills[i].getQuotationId()) %></a></td>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getValidDate()) %></td>
						<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff) %></td>
					</tr>
				<%
					}
				%>
				<tr>
					<th colspan="3" align="right">GRAND TOTAL</th>
					<th align="right"> Rs. <%=Utils.get2Decimal(gTotal) %></th>
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