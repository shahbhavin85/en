<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.CreditNoteModel"%>
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
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>CN'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "CREDIT_NOTE_HANDLER";
	document.forms[0].ACTIONS.value = "GET_CREDIT_NOTE";
	fnFinalSubmit();
}

function fnGetLinkSalesDtls(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "CREDIT_NOTE_HANDLER";
	document.forms[0].ACTIONS.value = "GET_CREDIT_NOTE";
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
			<legend class="screenHeader">View / Edit Sales Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Invoice No<span style="color: red">*</span> :</td>
					<td><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>CN - <input name="txtInvNo" style="width: 195px;" maxlength="5">
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/></td>
				</tr>
			</table>
			<%
				CreditNoteModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (CreditNoteModel[]) request.getAttribute(Constant.FORM_DATA) : new CreditNoteModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%" border="1">
				<tr>
					<th colspan="3" align="center">Today's Bill</th>
				</tr>
				<tr>
					<th style="width: 120px;">BILL NO</th>
					<th>CUSTOMER</th>
					<th style="width: 120px;">TOTAL AMOUNT</th>
				</tr>
				<%
					double gTotal = 0;
					double roundOff = 0;
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()-todayBills[i].getLess())%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()-todayBills[i].getLess())%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()-todayBills[i].getLess()+roundOff);
				%>
					<tr>
						<td align="center"><a href="JavaScript:fnGetLinkSalesDtls('<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix()+"CN"+Utils.padBillNo(todayBills[i].getSaleid()) %>')"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix()+"CN"+Utils.padBillNo(todayBills[i].getSaleid()) %></a></td>
						<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()-todayBills[i].getLess()+roundOff) %></td>
					</tr>
				<%
					}
				%>
				<tr>
					<th colspan="2" align="right">GRAND TOTAL</th>
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