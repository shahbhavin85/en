<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.PurchaseItemModel"%>
<%@page import="com.en.model.PurchaseModel"%>
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
function editPurchaseBill(){
	document.forms[0].HANDLERS.value = "PURCHASE_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_PURCHASE";
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Purchase Invoice Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 width="90%" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Purchase No</td>
					<td><b><%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"P"+((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getPurchaseId() %></b></td>
					<td align="right" valign="top">Invoice No / Date</td>
					<td valign="top"><b><%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getInvNo() + " / " +((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getInvDt()%></b></td>
					<td align="right">Received Date</td>
					<td><b><%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getRecdDt()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" valign="top" align="right">Supplier</td>
					<td colspan="3"><b><%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getSupplier().getLabel()%></b></td>
					<td align="right">Bill Type</td><td><b><%if(((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBillType() == 1) {%>VAT BILL<%} else if(((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBillType() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></b></td>
				</tr>
				<tr>
					<td align="right">Remarks</td><td colspan="5"><b><%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getRemark() %></b></td>
				</tr>
			</table>
			<table width="90%" border="1" cellspacing="0" id="enqItem" align="center" style="margin-top: 10px;">
				<tr>
					<th  style="width:50px;">S No.</th>
					<th>Description of Goods</th>
					<th style="width:50px;">Qty</th>
					<th style="width:50px;">Rate<br/>(Rs.)</th>
					<th style="width:50px;">Tax<br/>(%)</th>
					<th style="width:65px;">Amount<br/>(Rs.)</th>
				</tr>
				<%
					double total = 0;
					double stotal = 0;
					double ttotal = 0;
					double qty = 0;
					PurchaseItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((PurchaseItemModel[]) (((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new PurchaseItemModel[0]))) : new PurchaseItemModel[0];
					for(int i=0; i<salesItems.length; i++){
						
				%>
				<tr align="center">
					<td align="center" style="width:50px;"><%=(i+1)%></td>
					<td align="left"><%=salesItems[i].getItem().getItemNumber() + " / "+ salesItems[i].getItem().getItemName()%></td>
					<td style="width:50px;"><%=salesItems[i].getQty()%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getRate())%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getTax())%></td>
					<td style="width:65px;"><%=Utils.get2Decimal(((100+salesItems[i].getTax())/100)*salesItems[i].getQty() * salesItems[i].getRate())%></td>
				</tr>
				<%
						qty = qty + (salesItems[i].getQty());
						total = total + (((100+salesItems[i].getTax())/100)*salesItems[i].getQty() * salesItems[i].getRate());
					}
					if(request.getAttribute(Constant.FORM_DATA) != null && ((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getExtra() >0){
						total = total + ((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getExtra();
				%>
				<tr align="center">
					<th align="right" colspan="2">Extra Charges(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getExtra())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getDiscount() >0){
						total = total - ((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getDiscount();
				%>
				<tr align="center">
					<th align="right" colspan="2">Discount (-)</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getDiscount())%></td>
				</tr>
				<% }
					double roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
					/*if(roundOff != 0){
						total = total + roundOff;*/
				%>
				<!--tr align="center">
					<th align="right" colspan="2">ROUNDOFF</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><!%=Utils.get2Decimal(roundOff)%></td>
				</tr-->
				<%// }  %>
				<tr>
					<th align="right" colspan="2">GRAND TOTAL</th>
					<th style="width:50px;"><%=qty%></th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<th style="width:65px;"><%=Utils.get2Decimal(total)%></th>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Edit Bill" <%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).isLock() ? "disabled=\"disabled\"" : "" %> onclick="editPurchaseBill()" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="txtInvoiceNo" value="<%=((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"P"+((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getPurchaseId()%>"/>
</form>

</body>
</html>