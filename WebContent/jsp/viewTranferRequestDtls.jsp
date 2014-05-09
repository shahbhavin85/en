<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.TransferItemModel"%>
<%@page import="com.en.model.TransferModel"%>
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
<script type="text/javascript" src="js/transferRequest.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Transfer Request Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td width="15%" align="right">Transfer Request No</td>
					<td width="35%"><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getBillPrefix()+"TR"+((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferid() %></b></td>
					<td width="15%" align="right">Date</td>
					<td><b><%=Utils.convertToAppDateDDMMYY(((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferdate())%></b></td>
				</tr>
				<tr>
					<td align="right">From</td>
					<td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getAccessName() %></b></td>
					<td align="right">To</td>
					<td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getToBranch().getAccessName()%></b></td>
				</tr>
				<tr>
					<td align="right">Remarks</td>
					<td colspan="3"><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getRemark() %></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="margin-top: 10px; width: 950px;">
				<tr>
					<th  style="width:50px;">S No.</th>
					<th >Description of Goods</th>
					<th style="width:50px;">Qty</th>
				</tr>
				<%
					double qty = 0;
					TransferItemModel[] items = (request.getAttribute(Constant.FORM_DATA) != null) ? ((TransferItemModel[]) (((TransferModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new TransferItemModel[0]))) : new TransferItemModel[0];
					for(int i=0; i<items.length; i++){
						
				%>
				<tr align="center">
					<td align="center" style="width:50px;"><%=(i+1)%></td>
					<td align="left"><%=items[i].getItem().getItemNumber() + " / "+ items[i].getItem().getItemName() + items[i].getDesc()%></td>
					<td style="width:50px;"><%=items[i].getQty()%></td>
				</tr>
				<%
						qty = qty + (items[i].getQty());
					}
				%>
				<tr>
					<th align="right" colspan="2">GRAND TOTAL</th>
					<th style="width:50px;"><%=qty%></th>
				</tr>
			</table>
			<table style="width: 950px;" align="center">
				<tr>
					<td align="center"><input type="button" value="Close" onclick="window.close();"/></td> 
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