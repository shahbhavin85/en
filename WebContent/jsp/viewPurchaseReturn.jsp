<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.PurchaseReturnModel"%>
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
	if(!vRegex.test(document.forms[0].txtPurId.value)){
		alert('Please provide a valid bill No.');
		return;
	}
	document.forms[0].txtPurchaseId.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())+"PR"%>'+document.forms[0].txtPurId.value;
	document.forms[0].HANDLERS.value = "PURCHASE_RETURN_HANDLER";
	document.forms[0].ACTIONS.value = "GET_PURCHASE_RETURN";
	fnFinalSubmit();
}

function fnGetLinkPurchaseDtls(billNo){
	document.forms[0].txtPurchaseId.value = billNo;
	document.forms[0].HANDLERS.value = "PURCHASE_RETURN_HANDLER";
	document.forms[0].ACTIONS.value = "GET_PURCHASE_RETURN";
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
			<legend class="screenHeader">View / Edit Purchase Return Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Purchase Return Id<span style="color: red">*</span> :</td>
					<td><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())+"PR"%> - <input name="txtPurId" maxlength="5" style="width: 195px;">
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/></td>
				</tr>
			</table>
			<%
				PurchaseReturnModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (PurchaseReturnModel[]) request.getAttribute(Constant.FORM_DATA) : new PurchaseReturnModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%" border="1">
				<tr>
					<th colspan="4" align="center">Today's Purchase</th>
				</tr>
				<tr>
					<th style="width: 120px;">RETURN NO</th>
					<th style="width: 120px;">RETURN DT</th>
					<th>SUPPLIER</th>
					<th style="width: 120px;">TOTAL AMOUNT</th>
				</tr>
				<%
					double gTotal = 0;
					double roundOff = 0;
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+roundOff)%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+roundOff)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+roundOff)%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+roundOff);
				%>
					<tr>
						<td align="center"><a href="JavaScript:fnGetLinkPurchaseDtls('<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix()+"PR"+Utils.padBillNo(todayBills[i].getPurchaseId()) %>')"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix()+"PR"+Utils.padBillNo(todayBills[i].getPurchaseId()) %></a></td>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getReturnDt()) %></td>
						<td><%=todayBills[i].getSupplier().getCustomerName()+" - "+todayBills[i].getSupplier().getArea()+" - "+todayBills[i].getSupplier().getCity() %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+roundOff) %></td>
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
	<input name="txtPurchaseId" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>